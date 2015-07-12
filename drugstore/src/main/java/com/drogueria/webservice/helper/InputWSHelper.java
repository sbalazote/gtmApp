package com.drogueria.webservice.helper;

import com.drogueria.config.PropertyProvider;
import com.drogueria.model.Input;
import com.drogueria.model.InputDetail;
import com.drogueria.service.PropertyService;
import com.drogueria.util.DateUtils;
import com.drogueria.util.OperationResult;
import com.drogueria.util.StringUtility;
import com.drogueria.webservice.WebService;
import com.drogueria.webservice.WebServiceHelper;
import com.inssjp.mywebservice.business.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InputWSHelper {

	private static final Logger logger = Logger.getLogger(WebServiceHelper.class);

	@Autowired
	private PropertyService PropertyService;

	@Autowired
	private WebServiceHelper webServiceHelper;

	@Autowired
	private WebService webService;

	private static String ERROR_AGENT_HAS_NOT_INFORM = "No se ha podido informar el ingreso dado que los siguientes productos no fueron informados por el Agente de origen";
	private static String ERROR_CANNOT_CONNECT_TO_ANMAT_PENDING_TRANSACTION = "No se han podido obtener las transacciones pendientes";
	private static String ERROR_CANNOT_CONNECT_TO_ANMAT = "No se ha podido conectar con el Servidor de ANMAT";

	public OperationResult sendDrugInformationToAnmat(Input input) throws Exception {
		OperationResult operationResult = new OperationResult();
		operationResult.setResultado(false);
		WebServiceResult webServiceResult = null;
		List<String> errors = new ArrayList<>();
		String eventId = input.getEvent();

		if (eventId != null) {
			List<InputDetail> pendingProducts = new ArrayList<InputDetail>();
			List<ConfirmacionTransaccionDTO> toConfirm = new ArrayList<>();
			Boolean isProducion = Boolean.valueOf(PropertyProvider.getInstance().getProp(PropertyProvider.IS_PRODUCTION));
			boolean hasChecked = this.getPendingTransactions(input.getInputDetails(), pendingProducts, errors, isProducion,toConfirm,input.getDate());
			// Si la lista esta vacia es porque de los productos que informan ninguno esta pendiente de informar por el agente de origen
			if ((pendingProducts.isEmpty()) && hasChecked) {
				webServiceResult = this.confirmDrugs(toConfirm,errors);
			} else {
				if (hasChecked) {
					errors.add(ERROR_AGENT_HAS_NOT_INFORM);
					for (InputDetail inputDetail : pendingProducts) {
						errors.add(inputDetail.toString());
					}
				} else {
					errors.add(ERROR_CANNOT_CONNECT_TO_ANMAT_PENDING_TRANSACTION);
				}
			}

		} else {
			String error = "No ha podido obtenerse el evento a informar dado el concepto y el cliente/provedor seleccionados (Concepto: '"
					+ input.getConcept().toString() + "' Cliente/Proveedor '" + input.getClientOrProviderDescription() + "' Tipo de Agente: '"
					+ input.getClientOrProviderAgentDescription() + "'). El ingreso no fue informado.";
			errors.add(error);
		}
		operationResult.setMyOwnErrors(errors);
		if (webServiceResult != null) {
			operationResult.setFromWebServiceResult(webServiceResult);
		}
		logger.info(errors);
		return operationResult;
	}

	private WebServiceResult sendDrugs(Input input, List<MedicamentosDTO> medicines, List<String> errors, String eventId) {
		WebServiceResult webServiceResult = null;
		for (InputDetail inputDetail : input.getInputDetails()) {
			// Solo si el producto informa anmat se hace el servicio
			if (inputDetail.getProduct().isInformAnmat()
					&& ("PS".equals(inputDetail.getProduct().getType()) || "SS".equals(inputDetail.getProduct().getType()))) {
				if (inputDetail.getGtin() != null) {
					MedicamentosDTO drug = this.setDrug(input, eventId, inputDetail);
					medicines.add(drug);
				} else {
					String error = "El producto " + inputDetail.getProduct().getDescription() + " no registra GTIN, no puede ser informado.";
					errors.add(error);
				}
			}
		}
		if (!medicines.isEmpty() && errors.isEmpty()) {
			logger.info("Iniciando consulta con ANMAT");
			webServiceResult = this.webServiceHelper.run(medicines, this.PropertyService.get().getANMATName(), this.PropertyService.get().getDecryptPassword(),
					errors);
			if (webServiceResult == null) {
				errors.add(ERROR_CANNOT_CONNECT_TO_ANMAT);
			}
		}
		logger.info(errors);
		return webServiceResult;
	}

	private WebServiceResult confirmDrugs(List<ConfirmacionTransaccionDTO> toConfirm, List<String> errors) {
		WebServiceResult webServiceResult = null;
		if (!toConfirm.isEmpty() && errors.isEmpty()) {
			logger.info("Iniciando consulta con ANMAT");
			ConfirmacionTransaccionDTO[] toConfirmArray = new ConfirmacionTransaccionDTO[toConfirm.size()];
            toConfirmArray = toConfirm.toArray(toConfirmArray);
            try {
                webServiceResult = this.webService.confirmarTransaccion(toConfirmArray,this.PropertyService.get().getANMATName(),this.PropertyService.get().getDecryptPassword());
            } catch (Exception e) {
                errors.add(ERROR_CANNOT_CONNECT_TO_ANMAT);
                e.printStackTrace();
            }
            if (webServiceResult == null) {
				errors.add(ERROR_CANNOT_CONNECT_TO_ANMAT);
			}
		}
		logger.info(errors);
		return webServiceResult;
	}

	private MedicamentosDTO setDrug(Input input, String eventId, InputDetail inputDetail) {
		MedicamentosDTO drug = new MedicamentosDTO();
		String deliveryNote = "R" + StringUtility.addLeadingZeros(input.getDeliveryNoteNumber(), 12);
		String expirationDate = new SimpleDateFormat("dd/MM/yyyy").format(inputDetail.getExpirationDate()).toString();

		if ("SS".equals(inputDetail.getProduct().getType())) {
			String startTraceEvent = this.PropertyService.get().getStartTraceConcept().getEventOnInput(input.getClientOrProviderAgent());
			if (startTraceEvent != null) {
				eventId = startTraceEvent;
			}
		}
		this.webServiceHelper.setDrug(drug, input.getOriginGln(), this.PropertyService.get().getGln(), input.getOriginTax(), this.PropertyService.get()
				.getTaxId(), deliveryNote, expirationDate, inputDetail.getGtin().getNumber(), eventId, inputDetail.getSerialNumber(), inputDetail.getBatch(),
				input.getDate(), false, null, null, null, null, null);

		return drug;
	}

	public boolean getPendingTransactions(List<InputDetail> details, List<InputDetail> pendingProducts, List<String> errors, boolean isProduction, List<ConfirmacionTransaccionDTO> toConfirm, Date eventDate) {
		boolean toReturn = false;
		try {
			for (InputDetail inputDetail : details) {
				boolean found = false;
				if (inputDetail.getProduct().isInformAnmat() && "PS".equals(inputDetail.getProduct().getType())) {
					if (!isProduction) {
						toReturn = true;
					} else {
						toReturn = this.checkPendingTransactions(pendingProducts, errors, inputDetail, found, toConfirm, eventDate);
					}
				}
			}
		} catch (Exception e) {
			logger.info(ERROR_CANNOT_CONNECT_TO_ANMAT_PENDING_TRANSACTION);
		}
		return toReturn;
	}

	private boolean checkPendingTransactions(List<InputDetail> pendingProducts, List<String> errors, InputDetail inputDetail, boolean found, List<ConfirmacionTransaccionDTO> toConfirm, Date eventDate) throws Exception {
		boolean toReturn = false;
		TransaccionesNoConfirmadasWSResult pendingTransactions = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = DateUtils.getDateFrom(-this.PropertyService.get().getDaysAgoPendingTransactions());
		long nullValue = -1;
		String gtin = StringUtility.addLeadingZeros(inputDetail.getProduct().getLastGtin(), 14);
		String serie = inputDetail.getSerialNumber();
		pendingTransactions = this.webService.getTransaccionesNoConfirmadas(this.PropertyService.get().getANMATName(), this.PropertyService.get()
				.getDecryptPassword(), nullValue, null, null, null, gtin, nullValue, null, null, simpleDateFormat.format(date), simpleDateFormat
				.format(new Date()), null, null, null, null, nullValue, null, serie, new Long(1), new Long(100));
		if (pendingTransactions != null) {
			if (pendingTransactions.getErrores() == null) {
				if (pendingTransactions.getList() != null) {
					for (TransaccionPlainWS transaccionPlainWS : pendingTransactions.getList()) {
						if (transaccionPlainWS.get_numero_serial().equals(inputDetail.getSerialNumber()) && transaccionPlainWS.get_gtin().equals(gtin)) {
							found = true;
							ConfirmacionTransaccionDTO confirmacionTransaccionDTO = new ConfirmacionTransaccionDTO();
							confirmacionTransaccionDTO.setF_operacion(simpleDateFormat.format(eventDate));
							confirmacionTransaccionDTO.setP_ids_transac(Long.valueOf(transaccionPlainWS.get_id_transaccion()));
							toConfirm.add(confirmacionTransaccionDTO);
						}
					}
					toReturn = true;
					if (found == false) {
						pendingProducts.add(inputDetail);
					}
				}
			} else {
				for (WebServiceError error : pendingTransactions.getErrores()) {
					errors.add("Errores informados por ANMAT: " + error.get_c_error() + " - " + error.get_d_error());
				}
			}
		}
		return toReturn;
	}

}

package com.drogueria.webservice.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.drogueria.constant.Constants;
import com.drogueria.helper.EncryptionHelper;
import com.drogueria.model.Input;
import com.drogueria.model.InputDetail;
import com.drogueria.service.DrugstorePropertyService;
import com.drogueria.util.OperationResult;
import com.drogueria.util.StringUtils;
import com.drogueria.webservice.WebService;
import com.drogueria.webservice.WebServiceHelper;
import com.inssjp.mywebservice.business.MedicamentosDTO;
import com.inssjp.mywebservice.business.TransaccionPlainWS;
import com.inssjp.mywebservice.business.TransaccionesNoConfirmadasWSResult;
import com.inssjp.mywebservice.business.WebServiceError;
import com.inssjp.mywebservice.business.WebServiceResult;

public class InputWSHelper {

	private static final Logger logger = Logger.getLogger(WebServiceHelper.class);

	@Autowired
	private DrugstorePropertyService drugstorePropertyService;

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
		List<MedicamentosDTO> medicines = new ArrayList<>();
		List<String> errors = new ArrayList<>();
		String eventId = input.getEvent();

		if (eventId != null) {
			List<InputDetail> pendingTransations = new ArrayList<InputDetail>();
			boolean hasChecked = this.getPendingTransactions(input.getInputDetails(), pendingTransations, errors);
			// Si la lista esta vacia es porque de los productos que informan ninguno esta pendiente de informar por el agente de origen
			if (((pendingTransations.isEmpty()) && hasChecked) || input.hasNotProviderSerialized()) {
				webServiceResult = this.sendDrugs(input, medicines, errors, eventId);
			} else {
				if (hasChecked) {
					errors.add(ERROR_AGENT_HAS_NOT_INFORM);
					for (InputDetail inputDetail : pendingTransations) {
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
					MedicamentosDTO drug = this.setDrug(input, medicines, eventId, inputDetail);
					medicines.add(drug);
				} else {
					String error = "El producto " + inputDetail.getProduct().getDescription() + " no registra GTIN, no puede ser informado.";
					errors.add(error);
				}
			}
		}
		if (!medicines.isEmpty() && errors.isEmpty()) {
			logger.info("Iniciando consulta con ANMAT");
			webServiceResult = this.webServiceHelper.run(medicines, this.drugstorePropertyService.get().getANMATName(),
					EncryptionHelper.AESDecrypt(this.drugstorePropertyService.get().getANMATPassword()), errors);
			if (webServiceResult == null) {
				errors.add(ERROR_CANNOT_CONNECT_TO_ANMAT);
			}
		}
		logger.info(errors);
		return webServiceResult;
	}

	private MedicamentosDTO setDrug(Input input, List<MedicamentosDTO> medicines, String eventId, InputDetail inputDetail) {
		MedicamentosDTO drug = new MedicamentosDTO();
		String deliveryNote = "R" + StringUtils.addLeadingZeros(input.getDeliveryNoteNumber(), 12);
		String expirationDate = new SimpleDateFormat("dd/MM/yyyy").format(inputDetail.getExpirationDate()).toString();

		if ("SS".equals(inputDetail.getProduct().getType())) {
			String startTraceEvent = this.drugstorePropertyService.get().getStartTraceConcept().getEventOnInput(input.getClientOrProviderAgent());
			if (startTraceEvent != null) {
				eventId = startTraceEvent;
			}
		}
		this.webServiceHelper.setDrug(drug, input.getOriginGln(), this.drugstorePropertyService.get().getGln(), input.getOriginTax(),
				this.drugstorePropertyService.get().getTaxId(), deliveryNote, expirationDate, inputDetail.getGtin().getNumber(), eventId,
				inputDetail.getSerialNumber(), inputDetail.getBatch(), input.getDate(), false);

		return drug;
	}

	public boolean getPendingTransactions(List<InputDetail> details, List<InputDetail> pendingProducts, List<String> errors) throws Exception {
		long nullValue = -1;
		boolean toReturn = false;
		TransaccionesNoConfirmadasWSResult pendingTransactions = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, -this.drugstorePropertyService.get().getDaysAgoPendingTransactions());
			date.setTime(c.getTime().getTime());
			for (InputDetail inputDetail : details) {
				boolean found = false;
				if (inputDetail.getProduct().isInformAnmat() && "PS".equals(inputDetail.getProduct().getType())) {
					if (Constants.TEST) {
						toReturn = true;
					} else {
						String gtin = StringUtils.addLeadingZeros(inputDetail.getProduct().getLastGtin(), 14);
						pendingTransactions = this.webService.getTransaccionesNoConfirmadas(this.drugstorePropertyService.get().getANMATName(),
								EncryptionHelper.AESDecrypt(this.drugstorePropertyService.get().getANMATPassword()), nullValue, null, null, null, gtin,
								nullValue, null, null, simpleDateFormat.format(date), simpleDateFormat.format(new Date()), null, null, null, null, nullValue,
								null, null);
						toReturn = this.checkPendingTransactions(pendingProducts, errors, pendingTransactions, inputDetail, found, gtin);
					}
				}
			}
		} catch (Exception e) {
			logger.info(ERROR_CANNOT_CONNECT_TO_ANMAT_PENDING_TRANSACTION);
		}
		return toReturn;
	}

	private boolean checkPendingTransactions(List<InputDetail> pendingProducts, List<String> errors, TransaccionesNoConfirmadasWSResult pendingTransactions,
			InputDetail inputDetail, boolean found, String gtin) {
		boolean toReturn = false;
		if (pendingTransactions != null) {
			if (pendingTransactions.getErrores() == null) {
				if (pendingTransactions.getList() != null) {
					for (TransaccionPlainWS transaccionPlainWS : pendingTransactions.getList()) {
						if (transaccionPlainWS.get_numero_serial().equals(inputDetail.getSerialNumber()) && transaccionPlainWS.get_gtin().equals(gtin)) {
							found = true;
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

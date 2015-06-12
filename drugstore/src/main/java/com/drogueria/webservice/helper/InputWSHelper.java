package com.drogueria.webservice.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.drogueria.config.PropertyProvider;
import com.drogueria.model.Input;
import com.drogueria.model.InputDetail;
import com.drogueria.service.PropertyService;
import com.drogueria.util.DateUtils;
import com.drogueria.util.OperationResult;
import com.drogueria.util.StringUtility;
import com.drogueria.webservice.WebService;
import com.drogueria.webservice.WebServiceHelper;
import com.inssjp.mywebservice.business.ConfirmacionTransaccionDTO;
import com.inssjp.mywebservice.business.MedicamentosDTO;
import com.inssjp.mywebservice.business.TransaccionPlainWS;
import com.inssjp.mywebservice.business.TransaccionesNoConfirmadasWSResult;
import com.inssjp.mywebservice.business.WebServiceError;
import com.inssjp.mywebservice.business.WebServiceResult;

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

	public OperationResult confirmPendings(Input input) throws Exception {
		List<String> errors = new ArrayList<String>();
		WebServiceResult webServiceResultSelfSerialized = null;
		WebServiceResult webServiceResultProviderSerialized = null;
		boolean selfSerializedCheck = false;
		boolean providerSerializedCheck = false;
		OperationResult operationResult = new OperationResult();
		operationResult.setResultado(false);
		if (input.hasSelfSerialized()) {
			webServiceResultSelfSerialized = this.sendSelfSerializedDrugs(input, errors);
		} else {
			selfSerializedCheck = true;
		}
		if (!input.hasNotProviderSerialized()) {
			webServiceResultProviderSerialized = this.confirmPendingTransactions(input, errors);
		} else {
			providerSerializedCheck = true;
		}
		if (input.hasSelfSerialized() && webServiceResultSelfSerialized != null && webServiceResultSelfSerialized.getResultado()) {
			this.updateSelfSerializedDetails(input.getInputDetails(), webServiceResultSelfSerialized);
			selfSerializedCheck = true;
		} else {
			if (input.hasSelfSerialized() && webServiceResultSelfSerialized == null) {
				errors.add(ERROR_CANNOT_CONNECT_TO_ANMAT);
			}
		}
		if (!input.hasNotProviderSerialized() && webServiceResultProviderSerialized != null && webServiceResultProviderSerialized.getResultado()) {
			this.updateProviderSerializedDetails(input.getInputDetails(), webServiceResultProviderSerialized);
			providerSerializedCheck = true;
		}
		if (!input.hasNotProviderSerialized() && webServiceResultProviderSerialized == null) {
			errors.add(ERROR_CANNOT_CONNECT_TO_ANMAT);
		}
		if (providerSerializedCheck && selfSerializedCheck) {
			operationResult.setResultado(true);
		}
		if (errors.size() > 0) {
			operationResult.setMyOwnErrors(errors);
		}
		return operationResult;
	}

	private WebServiceResult sendSelfSerializedDrugs(Input input, List<String> errors) throws Exception {
		WebServiceResult webServiceResult = null;
		List<MedicamentosDTO> medicines = new ArrayList<>();
        if(this.PropertyService.get().getStartTraceConcept() != null) {
            String eventId = this.PropertyService.get().getStartTraceConcept().getEventOnInput(input.getClientOrProviderAgent());
            if (eventId != null) {
                for (InputDetail inputDetail : input.getInputDetails()) {
                    if (inputDetail.isInformAnmat() && "SS".equals(inputDetail.getProduct().getType()) && !inputDetail.isInformed()) {
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
                    webServiceResult = this.webServiceHelper.run(medicines, this.PropertyService.get().getANMATName(), this.PropertyService.get()
                            .getDecryptPassword(), errors);
                }
            } else {
                errors.add("No fue posible obtener el evento de inicio de traza.");
            }
        }else{
            errors.add("El concepto de inicio de traza no fue configurado.");
        }

		logger.info(errors);
		return webServiceResult;
	}

	private WebServiceResult confirmPendingTransactions(Input input, List<String> errors) throws Exception {
		WebServiceResult webServiceResult = null;
		List<ConfirmacionTransaccionDTO> pendingConfirmation = new ArrayList<ConfirmacionTransaccionDTO>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		OperationResult operationResult = new OperationResult();
		operationResult.setResultado(false);

		List<InputDetail> pendingProducts = new ArrayList<InputDetail>();
		Boolean isProducion = Boolean.valueOf(PropertyProvider.getInstance().getProp(PropertyProvider.IS_PRODUCTION));
		boolean hasChecked = this.getPendingTransactions(input.getInputDetails(), pendingProducts, errors, isProducion);

		if (hasChecked) {
			for (InputDetail inputDetail : input.getInputDetails()) {
				if (inputDetail.isInformAnmat() && "PS".equals(inputDetail.getProduct().getType()) && !inputDetail.isInformed()) {
					if (inputDetail.getGtin() != null) {
						ConfirmacionTransaccionDTO confirmacionTransaccionDTO = new ConfirmacionTransaccionDTO();
						if (inputDetail.getTransactionCodeANMAT() == null) {
							errors.add("No se encontro codigo de transaccion para el producto: " + inputDetail.getProduct().getDescription());
						} else {
							confirmacionTransaccionDTO.setF_operacion(simpleDateFormat.format(new Date()));
							confirmacionTransaccionDTO.setP_ids_transac(Long.valueOf(inputDetail.getTransactionCodeANMAT()));
						}
						pendingConfirmation.add(confirmacionTransaccionDTO);
					} else {
						String error = "El producto " + inputDetail.getProduct().getDescription() + " no registra GTIN, no puede ser informado.";
						errors.add(error);
					}
				}
			}
			if (!pendingConfirmation.isEmpty() && errors.isEmpty()) {
				logger.info("Iniciando consulta con ANMAT");
				ConfirmacionTransaccionDTO[] confirmations = new ConfirmacionTransaccionDTO[pendingConfirmation.size()];
				confirmations = pendingConfirmation.toArray(confirmations);
                if(!isProducion){
                    webServiceResult = new WebServiceResult();
                    webServiceResult.setResultado(false);
                    WebServiceError webServiceError = new WebServiceError("9999", "error de prueba");
                    WebServiceError[] errores = new WebServiceError[1];
                    errores[0] = webServiceError;
                    webServiceResult.setErrores(errores);
                    webServiceResult.setCodigoTransaccion("1111111");
                }else {
                    webServiceResult = this.webService.confirmarTransaccion(confirmations, this.PropertyService.get().getANMATName(), this.PropertyService.get()
                            .getDecryptPassword());
                }
			}
		} else {
			errors.add("No se han podido obtener las transacciones pendientes.");
		}

		logger.info(errors);
		return webServiceResult;
	}

	private void updateSelfSerializedDetails(List<InputDetail> inputDetails, WebServiceResult webServiceResult) {
		for (InputDetail inputDetail : inputDetails) {
			if (inputDetail.isInformAnmat() && "SS".equals(inputDetail.getProduct().getType())) {
				inputDetail.setTransactionCodeANMAT(webServiceResult.getCodigoTransaccion());
				inputDetail.setInformed(true);
			}
		}
	}

	private void updateProviderSerializedDetails(List<InputDetail> inputDetails, WebServiceResult webServiceResult) {
		for (InputDetail inputDetail : inputDetails) {
			if (inputDetail.isInformAnmat() && "PS".equals(inputDetail.getProduct().getType())) {
				inputDetail.setTransactionCodeANMAT(webServiceResult.getCodigoTransaccion());
				inputDetail.setInformed(true);
			}
		}
	}

	private MedicamentosDTO setDrug(Input input, String eventId, InputDetail inputDetail) {
		MedicamentosDTO drug = new MedicamentosDTO();
		String deliveryNote = "R" + StringUtility.addLeadingZeros(input.getDeliveryNoteNumber(), 12);
		String expirationDate = new SimpleDateFormat("dd/MM/yyyy").format(inputDetail.getExpirationDate()).toString();

		this.webServiceHelper.setDrug(drug, input.getOriginGln(), this.PropertyService.get().getGln(), input.getOriginTax(), this.PropertyService.get()
				.getTaxId(), deliveryNote, expirationDate, inputDetail.getGtin().getNumber(), eventId, inputDetail.getSerialNumber(), inputDetail.getBatch(),
				input.getDate(), false, null, null, null, null, null);

		return drug;
	}

	public boolean getPendingTransactions(List<InputDetail> details, List<InputDetail> pendingProducts, List<String> errors, boolean isProduction) {
		boolean toReturn = false;
		try {
			for (InputDetail inputDetail : details) {
				boolean found = false;
				if (inputDetail.getProduct().isInformAnmat() && "PS".equals(inputDetail.getProduct().getType())) {
					if (!isProduction) {
						inputDetail.setTransactionCodeANMAT("21590216");
						toReturn = true;
					} else {
						toReturn = this.checkPendingTransactions(pendingProducts, errors, inputDetail, found);
					}
				}
			}
		} catch (Exception e) {
			logger.info(ERROR_CANNOT_CONNECT_TO_ANMAT_PENDING_TRANSACTION);
		}
		return toReturn;
	}

	private boolean checkPendingTransactions(List<InputDetail> pendingProducts, List<String> errors, InputDetail inputDetail, boolean found) throws Exception {
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
							inputDetail.setTransactionCodeANMAT(transaccionPlainWS.get_id_transaccion());
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

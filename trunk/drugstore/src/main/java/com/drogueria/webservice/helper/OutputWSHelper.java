package com.drogueria.webservice.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.drogueria.helper.EncryptionHelper;
import com.drogueria.model.Output;
import com.drogueria.model.OutputDetail;
import com.drogueria.service.DrugstorePropertyService;
import com.drogueria.util.OperationResult;
import com.drogueria.webservice.WebServiceHelper;
import com.inssjp.mywebservice.business.MedicamentosDTO;
import com.inssjp.mywebservice.business.WebServiceResult;

public class OutputWSHelper {
	private static final Logger logger = Logger.getLogger(WebServiceHelper.class);

	@Autowired
	private DrugstorePropertyService drugstorePropertyService;

	@Autowired
	private WebServiceHelper webServiceHelper;

	public OperationResult sendDrugInformationToAnmat(Output output) throws Exception {
		OperationResult operationResult = new OperationResult();
		operationResult.setResultado(false);
		WebServiceResult webServiceResult = null;
		List<MedicamentosDTO> medicines = new ArrayList<>();
		String eventId = output.getEvent();
		List<String> errors = new ArrayList<>();
		if (eventId != null) {
			webServiceResult = this.sendDrugs(output, webServiceResult, medicines, eventId, errors);
		} else {
			String error = "No ha podido obtenerse el evento a informar dado el concepto y el cliente/provedor seleccionados (Concepto: '"
					+ output.getConcept().getCode() + " - " + output.getConcept().getDescription() + "' Cliente/Proveedor '"
					+ output.getClientOrProviderDescription() + "' Tipo de Proveedor: '" + output.getClientOrProviderAgentDescription()
					+ "'). El ingreso no fue informado.";
			logger.info(error);
			System.out.println(error);
			errors.add(error);
		}
		operationResult.setMyOwnErrors(errors);
		if (webServiceResult != null) {
			operationResult.setFromWebServiceResult(webServiceResult);
		}
		return operationResult;
	}

	private WebServiceResult sendDrugs(Output output, WebServiceResult webServiceResult, List<MedicamentosDTO> medicines, String eventId, List<String> errors) {
		for (OutputDetail outputDetail : output.getOutputDetails()) {
			// Solo si el producto informa anmat se hace el servicio
			if (outputDetail.getProduct().isInformAnmat()
					&& ("PS".equals(outputDetail.getProduct().getType()) || "SS".equals(outputDetail.getProduct().getType()))) {
				if (outputDetail.getProduct().getLastGtin() != null) {
					MedicamentosDTO drug = new MedicamentosDTO();
					String expirationDate = new SimpleDateFormat("dd/MM/yyyy").format(outputDetail.getExpirationDate()).toString();
					this.webServiceHelper.setDrug(drug, this.drugstorePropertyService.get().getGln(), output.getDestinationGln(), this.drugstorePropertyService
							.get().getTaxId(), output.getDestinationTax(), "R000100001234", expirationDate, outputDetail.getGtin().getNumber(), eventId,
							outputDetail.getSerialNumber(), outputDetail.getBatch(), output.getDate(), false);
					medicines.add(drug);
				} else {
					String error = "El producto " + outputDetail.getProduct().getDescription() + " no registra GTIN, no puede ser informado.";
					logger.info(error);
					errors.add(error);
				}
			}
		}

		if (!medicines.isEmpty()) {
			logger.info("Iniciando consulta con ANMAT");
			try {
				webServiceResult = this.webServiceHelper.run(medicines, this.drugstorePropertyService.get().getANMATName(),
						EncryptionHelper.AESDecrypt(this.drugstorePropertyService.get().getANMATPassword()), errors);
			} catch (Exception e) {
				String error = "Error de conexion con el Servicio de ANMAT";
				errors.add(error);
				logger.info(error);
			}
		}
		return webServiceResult;
	}

}

package com.drogueria.webservice.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.drogueria.helper.EncryptionHelper;
import com.drogueria.model.DeliveryNote;
import com.drogueria.model.DeliveryNoteDetail;
import com.drogueria.model.Order;
import com.drogueria.model.Output;
import com.drogueria.service.AgreementService;
import com.drogueria.service.DrugstorePropertyService;
import com.drogueria.util.OperationResult;
import com.drogueria.util.StringUtils;
import com.drogueria.webservice.WebServiceHelper;
import com.inssjp.mywebservice.business.MedicamentosDTO;
import com.inssjp.mywebservice.business.WebServiceResult;

public class DeliveryNoteWSHelper {
	private static final Logger logger = Logger.getLogger(WebServiceHelper.class);

	@Autowired
	private DrugstorePropertyService drugstorePropertyService;

	@Autowired
	private WebServiceHelper webServiceHelper;

	@Autowired
	private AgreementService agreementService;

	public OperationResult sendDrugInformationToAnmat(DeliveryNote deliveryNote, Order order, Output output) {
		OperationResult operationResult = new OperationResult();
		operationResult.setResultado(false);
		List<MedicamentosDTO> medicines = new ArrayList<>();
		WebServiceResult webServiceResult = null;
		List<String> errors = new ArrayList<>();
		// TODO aca chequeo si informo anmat en order u output??, dado que el concepto esta en agreement no en drugstoreproperty.-
		boolean isInformAnmat = order.getProvisioningRequest().getAgreement().getDeliveryNoteConcept().isInformAnmat()
				|| output.getAgreement().getDeliveryNoteConcept().isInformAnmat();
		String eventId = this.getEvent(output, order);

		if (isInformAnmat) {
			if (eventId != null) {
				webServiceResult = this.sendDrugs(deliveryNote, order, output, medicines, errors, eventId);
			} else {
				String clientOrProvider = "";
				String clientOrProviderAgent = "";
				Integer code = null;
				String conceptDescription = "";
				if (output != null) {
					clientOrProvider = output.getClientOrProviderDescription();
					clientOrProviderAgent = output.getClientOrProviderAgentDescription();
					conceptDescription = output.getConcept().getDescription();
					code = output.getConcept().getCode();
				}
				if (order != null) {
					clientOrProvider = order.getProvisioningRequest().getDeliveryLocation().getCorporateName();
					clientOrProviderAgent = order.getProvisioningRequest().getDeliveryLocation().getAgent().getDescription();
					conceptDescription = order.getProvisioningRequest().getAgreement().getDeliveryNoteConcept().getDescription();
				}
				String error = "No ha podido obtenerse el evento a informar dado el concepto y el cliente/provedor seleccionados (Concepto: '" + code + " - "
						+ conceptDescription + "' Cliente/Proveedor '" + clientOrProvider + "' Tipo de Agente: '" + clientOrProviderAgent
						+ "'). El ingreso no fue informado.";
				logger.info(error);
				errors.add(error);
			}
		} else {
			String error = "El concepto asociado a remitos no informa a ANMAT";
			logger.info(error);
			errors.add(error);
		}
		operationResult.setMyOwnErrors(errors);
		if (webServiceResult != null) {
			operationResult.setFromWebServiceResult(webServiceResult);
		}
		return operationResult;
	}

	private WebServiceResult sendDrugs(DeliveryNote deliveryNote, Order order, Output output, List<MedicamentosDTO> medicines, List<String> errors,
			String eventId) {
		for (DeliveryNoteDetail deliveryNoteDetail : deliveryNote.getDeliveryNoteDetails()) {
			// Solo si el producto informa anmat se hace el servicio
			MedicamentosDTO drug = new MedicamentosDTO();
			String deliveryNoteFormated = "R" + StringUtils.addLeadingZeros(deliveryNote.getNumber(), 12);
			if (deliveryNoteDetail.getOrderDetail() != null) {
				if (deliveryNoteDetail.getOrderDetail().getProduct().isInformAnmat()
						&& ("PS".equals(deliveryNoteDetail.getOrderDetail().getProduct().getType()) || "SS".equals(deliveryNoteDetail.getOrderDetail()
								.getProduct().getType()))) {
					String expirationDate = new SimpleDateFormat("dd/MM/yyyy").format(deliveryNoteDetail.getOrderDetail().getExpirationDate()).toString();

					this.webServiceHelper.setDrug(drug, this.drugstorePropertyService.get().getGln(), order.getProvisioningRequest().getDeliveryLocation()
							.getGln(), this.drugstorePropertyService.get().getTaxId(), order.getProvisioningRequest().getDeliveryLocation().getTaxId(),
							deliveryNoteFormated, expirationDate, deliveryNoteDetail.getOrderDetail().getGtin().getNumber(), eventId, deliveryNoteDetail
							.getOrderDetail().getSerialNumber(), deliveryNoteDetail.getOrderDetail().getBatch(), deliveryNote.getDate(), true);
					medicines.add(drug);
				}
			}
			if (deliveryNoteDetail.getOutputDetail() != null) {
				if (deliveryNoteDetail.getOutputDetail().getProduct().isInformAnmat()
						&& ("PS".equals(deliveryNoteDetail.getOutputDetail().getProduct().getType()) || "SS".equals(deliveryNoteDetail.getOutputDetail()
								.getProduct().getType()))) {
					String expirationDate = new SimpleDateFormat("dd/MM/yyyy").format(deliveryNoteDetail.getOutputDetail().getExpirationDate()).toString();

					this.webServiceHelper.setDrug(drug, this.drugstorePropertyService.get().getGln(), output.getDestinationGln(), this.drugstorePropertyService
							.get().getTaxId(), output.getDestinationTax(), deliveryNoteFormated, expirationDate, deliveryNoteDetail.getOutputDetail().getGtin()
							.getNumber(), eventId, deliveryNoteDetail.getOutputDetail().getSerialNumber(), deliveryNoteDetail.getOutputDetail().getBatch(),
							deliveryNote.getDate(), true);
					medicines.add(drug);
				}
			}
		}

		if (!medicines.isEmpty()) {
			logger.info("Iniciando consulta con ANMAT");
			WebServiceResult result = this.webServiceHelper.run(medicines, this.drugstorePropertyService.get().getANMATName(),
					EncryptionHelper.AESDecrypt(this.drugstorePropertyService.get().getANMATPassword()), errors);
			return result;
		}
		return null;
	}

	public String getEvent(Output output, Order order) {
		String eventId = null;
		if (output != null) {
			if (output.getDeliveryLocation() != null) {
				eventId = output.getAgreement().getDeliveryNoteConcept().getEventOnOutput(output.getDeliveryLocation().getAgent().getId());
			}
			if (output.getProvider() != null) {
				eventId = output.getAgreement().getDeliveryNoteConcept().getEventOnOutput(output.getProvider().getAgent().getId());
			}
		} else {
			eventId = order.getProvisioningRequest().getAgreement().getDeliveryNoteConcept()
					.getEventOnOutput(order.getProvisioningRequest().getDeliveryLocation().getAgent().getId());
		}
		return eventId;
	}

}

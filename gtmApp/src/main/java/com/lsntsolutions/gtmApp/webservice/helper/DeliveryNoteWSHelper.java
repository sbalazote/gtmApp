package com.lsntsolutions.gtmApp.webservice.helper;

import com.inssjp.mywebservice.business.MedicamentosDTO;
import com.inssjp.mywebservice.business.WebServiceResult;
import com.lsntsolutions.gtmApp.helper.EncryptionHelper;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.service.PropertyService;
import com.lsntsolutions.gtmApp.util.OperationResult;
import com.lsntsolutions.gtmApp.webservice.WebServiceHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DeliveryNoteWSHelper {
	private static final Logger logger = Logger.getLogger(WebServiceHelper.class);
	//TODO HACER UN enum o algo para esto.
	public static final int ESTABLECIMIENTO_ASISTENCIAL = 6;
	public static final int FARMACIA = 7;

	@Autowired
	private PropertyService propertyService;

	@Autowired
	private WebServiceHelper webServiceHelper;

	public OperationResult sendDrugInformationToAnmat(DeliveryNote deliveryNote, Order order, Output output, Supplying supplying) {
		OperationResult operationResult = new OperationResult();
		operationResult.setResultado(false);
		List<MedicamentosDTO> medicines = new ArrayList<>();
		WebServiceResult webServiceResult = null;
		List<String> errors = new ArrayList<>();
		boolean isInformAnmat = false;
		String entityId = "";
		if (output != null) {
			isInformAnmat = output.getConcept().isInformAnmat();
			entityId = "el Egreso Nro.: " + output.getFormatId();
		}
		if (order != null) {
			isInformAnmat = order.getProvisioningRequest().getAgreement().getDeliveryNoteConcept().isInformAnmat();
			entityId = "el Armado Nro.: " + order.getFormatId();
		}
		if (supplying != null) {
			isInformAnmat = this.propertyService.get().getSupplyingConcept().isInformAnmat();
			entityId = "la Dispensa Nro.: " + supplying.getFormatId();
		}
		String eventId = this.getEvent(output, order, supplying);

		if (isInformAnmat) {
			logger.info("Se procede a informar a ANMAT " + entityId);
			if (eventId != null) {
				logger.info("Se informa a ANMAT con Id de Evento Nro.: " + eventId);
				webServiceResult = this.sendDrugs(deliveryNote, order, output, medicines, errors, eventId, supplying);
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
				if (supplying != null) {
					code = this.propertyService.get().getSupplyingConcept().getCode();
					conceptDescription = this.propertyService.get().getSupplyingConcept().getDescription();
				}
				String error = "No ha podido obtenerse el evento a informar dado el concepto y el cliente/provedor seleccionados (Concepto: '" + code + " - "
						+ conceptDescription + "' Cliente/Proveedor '" + clientOrProvider + "' Tipo de Agente: '" + clientOrProviderAgent
						+ "'). El ingreso no fue informado.";
				logger.warn(error);
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
			String eventId, Supplying supplying) {
		for (DeliveryNoteDetail deliveryNoteDetail : deliveryNote.getDeliveryNoteDetails()) {
			// Solo si el producto informa anmat se hace el servicio
			MedicamentosDTO drug = new MedicamentosDTO();
			String deliveryNoteFormated = deliveryNote.getNumber();
            deliveryNoteFormated = deliveryNoteFormated.replace("-", "");
            if (deliveryNote.isFake()) {
                deliveryNoteFormated = "X" + deliveryNoteFormated;
            } else {
                deliveryNoteFormated = "R" + deliveryNoteFormated;
            }
            if (deliveryNoteDetail.getOrderDetail() != null) {
				OrderDetail orderDetail = deliveryNoteDetail.getOrderDetail();
				if (orderDetail.getProduct().isInformAnmat()
						&& ("PS".equals(orderDetail.getProduct().getType()) || "SS".equals(orderDetail
								.getProduct().getType()))) {
					String expirationDate = new SimpleDateFormat("dd/MM/yyyy").format(orderDetail.getExpirationDate()).toString();

					String associateNumber = null;
					if(order.getProvisioningRequest().getClient() != null) {
						for (ClientAffiliate clientAffiliate : order.getProvisioningRequest().getAffiliate().getClientAffiliates()) {
							if (clientAffiliate.getClient().getId() == order.getProvisioningRequest().getClient().getId()) {
								associateNumber = clientAffiliate.getAssociateNumber();
							}
						}
					}
					if(this.propertyService.get().getAgent().getId() == ESTABLECIMIENTO_ASISTENCIAL || this.propertyService.get().getAgent().getId() == FARMACIA){
						this.webServiceHelper.setDrug(drug, this.propertyService.get().getGln(), null, this.propertyService.get().getTaxId(), null,
								deliveryNoteFormated, expirationDate, orderDetail.getGtin().getNumber(), eventId, deliveryNoteDetail
										.getOrderDetail().getSerialNumber(), orderDetail.getBatch(), deliveryNote.getDate(), true,
								order.getProvisioningRequest().getAffiliate(), order.getProvisioningRequest().getClient().getMedicalInsuranceCode(), associateNumber);
					}else{
						this.webServiceHelper.setDrug(drug, this.propertyService.get().getGln(), order.getProvisioningRequest().getDeliveryLocation().getGln(),
								this.propertyService.get().getTaxId(), order.getProvisioningRequest().getDeliveryLocation().getTaxId(), deliveryNoteFormated,
								expirationDate, orderDetail.getGtin().getNumber(), eventId, orderDetail.getSerialNumber(), orderDetail.getBatch(), deliveryNote.getDate(), true, order.getProvisioningRequest().getAffiliate(),
								null, associateNumber);
					}
					medicines.add(drug);
				}
			}
			if (deliveryNoteDetail.getOutputDetail() != null) {
				OutputDetail outputDetail = deliveryNoteDetail.getOutputDetail();
				if (outputDetail.getProduct().isInformAnmat()
						&& ("PS".equals(outputDetail.getProduct().getType()) || "SS".equals(outputDetail
								.getProduct().getType()))) {
					String expirationDate = new SimpleDateFormat("dd/MM/yyyy").format(outputDetail.getExpirationDate()).toString();
					String destinationGln = output.getConcept().isDestruction() ? null : output.getDestinationGln();
					String destinationTax = output.getConcept().isDestruction() ? null : output.getDestinationTax();
					this.webServiceHelper.setDrug(drug, this.propertyService.get().getGln(), destinationGln , this.propertyService.get().getTaxId(), destinationTax
							, deliveryNoteFormated, expirationDate, outputDetail.getGtin().getNumber(),	eventId, outputDetail.getSerialNumber(), outputDetail.getBatch(),
							output.getDate(), true, null, null, null);
					medicines.add(drug);
				}
			}
			if (deliveryNoteDetail.getSupplyingDetail() != null) {
				SupplyingDetail supplyingDetail = deliveryNoteDetail.getSupplyingDetail();
				if (supplyingDetail.getProduct().isInformAnmat()
						&& supplyingDetail.getInStock()
						&& ("PS".equals(supplyingDetail.getProduct().getType()) || "SS".equals(supplyingDetail
								.getProduct().getType()))) {
					String expirationDate = new SimpleDateFormat("dd/MM/yyyy").format(supplyingDetail.getExpirationDate()).toString();
					String associateNumber = null;
					if(supplying.getClient() != null) {
						for (ClientAffiliate clientAffiliate : supplying.getAffiliate().getClientAffiliates()) {
							if (clientAffiliate.getClient().getId() == supplying.getClient().getId()) {
								associateNumber = clientAffiliate.getAssociateNumber();
							}
						}
					}
					this.webServiceHelper.setDrug(drug, this.propertyService.get().getGln(), null, this.propertyService.get().getTaxId(), null,
							deliveryNoteFormated, expirationDate, supplyingDetail.getGtin().getNumber(), eventId, deliveryNoteDetail
									.getSupplyingDetail().getSerialNumber(), supplyingDetail.getBatch(), supplying.getDate(), true,
							supplying.getAffiliate(), supplying.getClient().getMedicalInsuranceCode(), associateNumber);
					medicines.add(drug);
				}
			}
		}

		if (!medicines.isEmpty()) {
			logger.info("Iniciando consulta con ANMAT");
			WebServiceResult result = this.webServiceHelper.run(medicines, this.propertyService.get().getANMATName(),
					EncryptionHelper.AESDecrypt(this.propertyService.get().getANMATPassword()), errors);
			return result;
		}
		return null;
	}

	public String getEvent(Output output, Order order, Supplying supplying) {
		String eventId = null;
		if (output != null) {
			if (output.getDeliveryLocation() != null) {
				eventId = output.getConcept().getEventOnOutput(output.getDeliveryLocation().getAgent().getId());
			}
			if (output.getProvider() != null) {
				if(output.getLogisticsOperator()!= null){
					eventId = output.getConcept().getEventOnOutput(output.getLogisticsOperator().getAgent().getId());
				}else {
					eventId = output.getConcept().getEventOnOutput(output.getProvider().getAgent().getId());
				}
			}
		}
		if (order != null) {
			if(this.propertyService.get().getAgent().getId() == ESTABLECIMIENTO_ASISTENCIAL || this.propertyService.get().getAgent().getId() == FARMACIA){
				if (this.propertyService.get().getSupplyingConcept().getEvents().size() > 0) {
					eventId = order.getProvisioningRequest().getAgreement().getDeliveryNoteConcept()
						.getEvents().get(0).getCode().toString();
				}
			}else {
				eventId = order.getProvisioningRequest().getAgreement().getDeliveryNoteConcept()
						.getEventOnOutput(order.getProvisioningRequest().getDeliveryLocation().getAgent().getId());
			}
		}
		if (supplying != null) {
			if (this.propertyService.get().getSupplyingConcept().getEvents().size() > 0) {
				eventId = this.propertyService.get().getSupplyingConcept().getEvents().get(0).getCode().toString();
			}
		}
		return eventId;
	}

}

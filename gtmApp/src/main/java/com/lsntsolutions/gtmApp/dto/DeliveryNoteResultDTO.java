package com.lsntsolutions.gtmApp.dto;

import com.ibm.icu.text.SimpleDateFormat;
import com.lsntsolutions.gtmApp.model.*;

import java.util.ArrayList;
import java.util.List;

public class DeliveryNoteResultDTO {
	private String agreement;
	private String deliveryLocation;
	private String date;
	private String number;
	private String client;
	private Boolean informAnmat;
	private String transactionCodeANMAT;
	private boolean cancelled;
	private String affiliate;
	private String supplyingId;
	private String outputId;
	private String orderId;
	private String provisioningRequestId;
	private String concept;

	private List<DeliveryNoteDetailResultDTO> deliveryNoteDetails;

	public List<DeliveryNoteDetailResultDTO> getDeliveryNoteDetails() {
		return this.deliveryNoteDetails;
	}

	public void setDeliveryNoteDetails(List<DeliveryNoteDetailResultDTO> deliveryNoteDetails) {
		this.deliveryNoteDetails = deliveryNoteDetails;
	}

	public String getAgreement() {
		return this.agreement;
	}

	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}

	public String getDeliveryLocation() {
		return this.deliveryLocation;
	}

	public void setDeliveryLocation(String deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getClient() {
		return this.client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getTransactionCodeANMAT() {
		return this.transactionCodeANMAT;
	}

	public void setTransactionCodeANMAT(String transactionCodeANMAT) {
		this.transactionCodeANMAT = transactionCodeANMAT;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}


	public String getSupplyingId() {
		return supplyingId;
	}

	public void setSupplyingId(String supplyingId) {
		this.supplyingId = supplyingId;
	}

	public String getOutputId() {
		return outputId;
	}

	public void setOutputId(String outputId) {
		this.outputId = outputId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getProvisioningRequestId() {
		return provisioningRequestId;
	}

	public void setProvisioningRequestId(String provisioningRequestId) {
		this.provisioningRequestId = provisioningRequestId;
	}

	public String getAffiliate() {
		return affiliate;
	}

	public void setAffiliate(String affiliate) {
		this.affiliate = affiliate;
	}

	public Boolean getInformAnmat() {
		return informAnmat;
	}

	public void setInformAnmat(Boolean informAnmat) {
		this.informAnmat = informAnmat;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public void setFromDeliveryNote(DeliveryNote deliveryNote, Order order, Output output, Supplying supplying) {
		SimpleDateFormat stringDate = new SimpleDateFormat("dd/MM/yyyy");
		this.number = deliveryNote.getNumber();
		List<DeliveryNoteDetailResultDTO> deliveryNoteDetailResultDTO = new ArrayList<DeliveryNoteDetailResultDTO>();
		if (output != null) {
			String code = output.getAgreement().getFormatCode();
			this.setAgreement(code + " - " + output.getAgreement().getDescription());
			this.setDate(stringDate.format(output.getDate()));
			if (output.getDeliveryLocation() != null) {
				code = output.getDeliveryLocation().getFormatCode();
				this.setDeliveryLocation(code + " - " +  output.getDeliveryLocation().getName());
			}
			if (output.getProvider() != null) {
				code = output.getProvider().getFormatCode();
				this.setDeliveryLocation(code + " - " +output.getProvider().getName());
			}
			this.setClient("NO SE INFORMA");
			String id = output.getFormatId();
			String affiliate = "NO SE INFORMA";
			this.setAffiliate(affiliate);
			this.setOutputId(id);
			this.setConcept(output.getConcept().getCode() + " - " + output.getConcept().getDescription());
		}
		if (order != null) {
			String code = order.getProvisioningRequest().getAgreement().getFormatCode();
			this.setAgreement(code + " - " + order.getProvisioningRequest().getAgreement().getDescription());
			this.setDate(stringDate.format(order.getProvisioningRequest().getDeliveryDate()));
			code = order.getProvisioningRequest().getDeliveryLocation().getFormatCode();
			this.setDeliveryLocation(code + " - " + order.getProvisioningRequest().getDeliveryLocation().getName());
			this.setClient(order.getProvisioningRequest().getClient().getCode() + " - " + order.getProvisioningRequest().getClient().getName());
			String id = order.getFormatId();
			String provisioningRequestId = order.getProvisioningRequest().getFormatId();
			String affiliate = order.getProvisioningRequest().getAffiliate().getCode() + " - " + order.getProvisioningRequest().getAffiliate().getSurname() + " " +
					order.getProvisioningRequest().getAffiliate().getName();
			this.setAffiliate(affiliate);
			this.setOrderId(id);
			this.setProvisioningRequestId(provisioningRequestId);
			this.setConcept(null);
		}
        if(supplying != null){
			String code = supplying.getAgreement().getFormatCode();
            this.setAgreement(code + " - " + supplying.getAgreement().getDescription());
            this.setDate(stringDate.format(supplying.getDate()));
			code = supplying.getClient().getFormatCode();
			this.setClient(code + " - " +supplying.getClient().getName());
            this.setDeliveryLocation("NO SE INFORMA");
			String id = supplying.getFormatId();
			String affiliate = supplying.getAffiliate().getCode() + " - " + supplying.getAffiliate().getSurname() + " " +
					supplying.getAffiliate().getName();
			this.setAffiliate(affiliate);
			this.setSupplyingId(id);
			this.setConcept(null);
        }

		if (deliveryNote.getTransactionCodeANMAT() != null) {
			this.setTransactionCodeANMAT(deliveryNote.getTransactionCodeANMAT());
		}
		if(deliveryNote.isCancelled()){
			this.setCancelled(deliveryNote.isCancelled());
		}
		this.setInformAnmat(deliveryNote.isInformAnmat());

		for (DeliveryNoteDetail deliveryNoteDetail : deliveryNote.getDeliveryNoteDetails()) {
			String gtin = "";
			if (deliveryNoteDetail.getOutputDetail() != null) {
				OutputDetail od = deliveryNoteDetail.getOutputDetail();
				if(od.getGtin()!=null){
					gtin = od.getGtin().getNumber();
				}
				DeliveryNoteDetailResultDTO outputDetailDTO = new DeliveryNoteDetailResultDTO(od.getProduct().getId(), od.getProduct().getCode(), od.getProduct().getDescription(), od.getSerialNumber(),
						od.getBatch(), stringDate.format(od.getExpirationDate()), od.getAmount(), null, od.getProduct().getId(),gtin);
				deliveryNoteDetailResultDTO.add(outputDetailDTO);
			}
			if (deliveryNoteDetail.getOrderDetail() != null) {
				OrderDetail od = deliveryNoteDetail.getOrderDetail();
				if(od.getGtin()!=null){
					gtin = od.getGtin().getNumber();
				}
				DeliveryNoteDetailResultDTO outputDetailDTO = new DeliveryNoteDetailResultDTO(od.getProduct().getId(), od.getProduct().getCode(), od.getProduct().getDescription(), od.getSerialNumber(),
						od.getBatch(), stringDate.format(od.getExpirationDate()), od.getAmount(), null, od.getProduct().getId(), gtin);
				deliveryNoteDetailResultDTO.add(outputDetailDTO);
			}
            if (deliveryNoteDetail.getSupplyingDetail() != null) {
                SupplyingDetail sd = deliveryNoteDetail.getSupplyingDetail();
				if(sd.getGtin()!=null){
					gtin = sd.getGtin().getNumber();
				}
                DeliveryNoteDetailResultDTO outputDetailDTO = new DeliveryNoteDetailResultDTO(sd.getProduct().getId(), sd.getProduct().getCode(), sd.getProduct().getDescription(), sd.getSerialNumber(),
                        sd.getBatch(), stringDate.format(sd.getExpirationDate()), sd.getAmount(), sd.getInStock(), sd.getProduct().getId(), gtin);
                deliveryNoteDetailResultDTO.add(outputDetailDTO);
            }
		}
		this.setDeliveryNoteDetails(deliveryNoteDetailResultDTO);
	}
}

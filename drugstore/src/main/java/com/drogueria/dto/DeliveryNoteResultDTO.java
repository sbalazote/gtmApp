package com.drogueria.dto;

import java.util.ArrayList;
import java.util.List;

import com.drogueria.model.*;
import com.drogueria.util.StringUtility;
import com.ibm.icu.text.SimpleDateFormat;

public class DeliveryNoteResultDTO {
	private String agreement;
	private String deliveryLocation;
	private String date;
	private String number;
	private String client;
	private String transactionCodeANMAT;
	private boolean cancelled;
	private List<OutputOrderDetailResultDTO> orderOutputDetails;

	public List<OutputOrderDetailResultDTO> getOrderOutputDetails() {
		return this.orderOutputDetails;
	}

	public void setOrderOutputDetails(List<OutputOrderDetailResultDTO> orderOutputDetails) {
		this.orderOutputDetails = orderOutputDetails;
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

	public void setFromDeliveryNote(DeliveryNote deliveryNote, Order order, Output output, Supplying supplying) {
		SimpleDateFormat stringDate = new SimpleDateFormat("dd/MM/yyyy");
		this.number = deliveryNote.getNumber();
		List<OutputOrderDetailResultDTO> outpuOrderResultDTO = new ArrayList<OutputOrderDetailResultDTO>();
		if (output != null) {
			String code = StringUtility.addLeadingZeros(output.getAgreement().getCode().toString(), 4);
			this.setAgreement(code + " - " + output.getAgreement().getDescription());
			this.setDate(stringDate.format(output.getDate()));
			if (output.getDeliveryLocation() != null) {
				code = StringUtility.addLeadingZeros(output.getDeliveryLocation().getCode().toString(), 5);
				this.setDeliveryLocation(code + " - " +  output.getDeliveryLocation().getCorporateName());
			}
			if (output.getProvider() != null) {
				code = StringUtility.addLeadingZeros(output.getProvider().getCode().toString(), 5);
				this.setDeliveryLocation(code + " - " +output.getProvider().getCorporateName());
			}
		}
		if (deliveryNote.getTransactionCodeANMAT() != null) {
			this.setTransactionCodeANMAT(deliveryNote.getTransactionCodeANMAT());
		}
		if(deliveryNote.isCancelled()){
			this.setCancelled(deliveryNote.isCancelled());
		}

		if (order != null) {
			String code = StringUtility.addLeadingZeros(order.getProvisioningRequest().getAgreement().getCode().toString(), 4);
			this.setAgreement(code + " - " + order.getProvisioningRequest().getAgreement().getDescription());
			this.setDate(stringDate.format(order.getProvisioningRequest().getDeliveryDate()));
			code = StringUtility.addLeadingZeros(order.getProvisioningRequest().getDeliveryLocation().getCode().toString(), 5);
			this.setDeliveryLocation(code + " - " + order.getProvisioningRequest().getDeliveryLocation().getCorporateName());
		}
        if(supplying != null){
			String code = StringUtility.addLeadingZeros(supplying.getAgreement().getCode().toString(), 4);
            this.setAgreement(code + " - " + supplying.getAgreement().getDescription());
            this.setDate(stringDate.format(supplying.getDate()));
			code = StringUtility.addLeadingZeros(supplying.getClient().getCode().toString(), 5);
            this.setDeliveryLocation(code + " - " +supplying.getClient().getCorporateName());
        }
		for (DeliveryNoteDetail deliveryNoteDetail : deliveryNote.getDeliveryNoteDetails()) {
			if (deliveryNoteDetail.getOutputDetail() != null) {
				OutputDetail od = deliveryNoteDetail.getOutputDetail();
				OutputOrderDetailResultDTO outputDetailDTO = new OutputOrderDetailResultDTO(od.getProduct().getDescription(), od.getSerialNumber(),
						od.getBatch(), stringDate.format(od.getExpirationDate()), od.getAmount());
				outpuOrderResultDTO.add(outputDetailDTO);
			}
			if (deliveryNoteDetail.getOrderDetail() != null) {
				OrderDetail od = deliveryNoteDetail.getOrderDetail();
				OutputOrderDetailResultDTO outputDetailDTO = new OutputOrderDetailResultDTO(od.getProduct().getDescription(), od.getSerialNumber(),
						od.getBatch(), stringDate.format(od.getExpirationDate()), od.getAmount());
				outpuOrderResultDTO.add(outputDetailDTO);
			}
            if (deliveryNoteDetail.getSupplyingDetail() != null) {
                SupplyingDetail sd = deliveryNoteDetail.getSupplyingDetail();
                OutputOrderDetailResultDTO outputDetailDTO = new OutputOrderDetailResultDTO(sd.getProduct().getDescription(), sd.getSerialNumber(),
                        sd.getBatch(), stringDate.format(sd.getExpirationDate()), sd.getAmount());
                outpuOrderResultDTO.add(outputDetailDTO);
            }
		}
		this.setOrderOutputDetails(outpuOrderResultDTO);
	}
}

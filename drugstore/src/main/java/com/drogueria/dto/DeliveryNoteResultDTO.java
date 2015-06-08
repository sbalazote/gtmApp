package com.drogueria.dto;

import java.util.ArrayList;
import java.util.List;

import com.drogueria.model.*;
import com.ibm.icu.text.SimpleDateFormat;

public class DeliveryNoteResultDTO {
	private String agreement;
	private String deliveryLocation;
	private String date;
	private String number;
	private String client;
	private String transactionCodeANMAT;
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

	public void setFromDeliveryNote(DeliveryNote deliveryNote, Order order, Output output, Supplying supplying) {
		SimpleDateFormat stringDate = new SimpleDateFormat("dd/MM/yyyy");
		this.number = deliveryNote.getNumber();
		List<OutputOrderDetailResultDTO> outpuOrderResultDTO = new ArrayList<OutputOrderDetailResultDTO>();
		if (output != null) {
			this.setAgreement(output.getAgreement().getDescription());
			this.setDate(stringDate.format(output.getDate()));
			if (output.getDeliveryLocation() != null) {
				this.setDeliveryLocation(output.getDeliveryLocation().getCorporateName());
			}
			if (output.getProvider() != null) {
				this.setDeliveryLocation(output.getProvider().getCorporateName());
			}
		}
		if (deliveryNote.getTransactionCodeANMAT() != null) {
			this.setTransactionCodeANMAT(deliveryNote.getTransactionCodeANMAT());
		}
		if (order != null) {
			this.setAgreement(order.getProvisioningRequest().getAgreement().getDescription());
			this.setDate(stringDate.format(order.getProvisioningRequest().getDeliveryDate()));
			this.setDeliveryLocation(order.getProvisioningRequest().getDeliveryLocation().getCorporateName());
		}
        if(supplying != null){
            this.setAgreement(supplying.getAgreement().getDescription());
            this.setDate(stringDate.format(supplying.getDate()));
            this.setDeliveryLocation(supplying.getClient().getCorporateName());
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

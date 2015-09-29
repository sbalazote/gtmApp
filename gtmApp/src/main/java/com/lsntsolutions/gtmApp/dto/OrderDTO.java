package com.lsntsolutions.gtmApp.dto;

import java.io.Serializable;
import java.util.List;

public class OrderDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer provisioningRequestId;
	private String deliveryNoteNumber;
	private List<OrderDetailDTO> orderDetails;
	private boolean cancelled;

	public Integer getProvisioningRequestId() {
		return this.provisioningRequestId;
	}

	public void setProvisioningRequestId(Integer provisioningRequestId) {
		this.provisioningRequestId = provisioningRequestId;
	}

	public String getDeliveryNoteNumber() {
		return this.deliveryNoteNumber;
	}

	public void setDeliveryNoteNumber(String deliveryNoteNumber) {
		this.deliveryNoteNumber = deliveryNoteNumber;
	}

	public List<OrderDetailDTO> getOrderDetails() {
		return this.orderDetails;
	}

	public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}

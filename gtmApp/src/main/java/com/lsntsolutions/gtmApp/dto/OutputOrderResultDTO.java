package com.lsntsolutions.gtmApp.dto;

public class OutputOrderResultDTO {

	private Integer outputId;
	private Integer orderId;
	private boolean found;

	public Integer getOutputId() {
		return this.outputId;
	}

	public void setOutputId(Integer outputId) {
		this.outputId = outputId;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}
}
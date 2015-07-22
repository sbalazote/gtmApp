package com.drogueria.dto;

import java.util.List;

public class AuditResultDTO {
	private List<AuditDTO> inputs;
	private List<AuditDTO> outputs;
	private List<AuditDTO> orders;
	private List<AuditDTO> deliveryNotes;
	private List<AuditDTO> supplyings;

	public List<AuditDTO> getInputs() {
		return this.inputs;
	}

	public void setInputs(List<AuditDTO> inputs) {
		this.inputs = inputs;
	}

	public List<AuditDTO> getOutputs() {
		return this.outputs;
	}

	public void setOutputs(List<AuditDTO> outputs) {
		this.outputs = outputs;
	}

	public List<AuditDTO> getOrders() {
		return this.orders;
	}

	public void setOrders(List<AuditDTO> orders) {
		this.orders = orders;
	}

	public List<AuditDTO> getDeliveryNotes() {
		return this.deliveryNotes;
	}

	public void setDeliveryNotes(List<AuditDTO> deliveryNotes) {
		this.deliveryNotes = deliveryNotes;
	}

	public List<AuditDTO> getSupplyings() {	return this.supplyings;	}

	public void setSupplyings(List<AuditDTO> supplyings) {
		this.supplyings = supplyings;
	}
}

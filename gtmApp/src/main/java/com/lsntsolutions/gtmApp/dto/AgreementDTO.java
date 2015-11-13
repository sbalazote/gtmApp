package com.lsntsolutions.gtmApp.dto;

import java.io.Serializable;

public class AgreementDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer code;
	private String description;
	private boolean active;
	private Integer numberOfDeliveryNoteDetailsPerPage;
	private String orderLabelPrinter;
	private String deliveryNotePrinter;
	private Integer deliveryNoteConceptId;
	private Integer destructionConceptId;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCode() {
		return this.code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Integer getNumberOfDeliveryNoteDetailsPerPage() {
		return this.numberOfDeliveryNoteDetailsPerPage;
	}

	public void setNumberOfDeliveryNoteDetailsPerPage(Integer numberOfDeliveryNoteDetailsPerPage) {
		this.numberOfDeliveryNoteDetailsPerPage = numberOfDeliveryNoteDetailsPerPage;
	}

	public String getOrderLabelPrinter() {
		return this.orderLabelPrinter;
	}

	public void setOrderLabelPrinter(String orderLabelPrinter) {
		this.orderLabelPrinter = orderLabelPrinter;
	}

	public String getDeliveryNotePrinter() {
		return this.deliveryNotePrinter;
	}

	public void setDeliveryNotePrinter(String deliveryNotePrinter) {
		this.deliveryNotePrinter = deliveryNotePrinter;
	}

	public Integer getDeliveryNoteConceptId() {
		return this.deliveryNoteConceptId;
	}

	public void setDeliveryNoteConceptId(Integer deliveryNoteConceptId) {
		this.deliveryNoteConceptId = deliveryNoteConceptId;
	}

	public Integer getDestructionConceptId() {
		return this.destructionConceptId;
	}

	public void setDestructionConceptId(Integer destructionConceptId) {
		this.destructionConceptId = destructionConceptId;
	}
}
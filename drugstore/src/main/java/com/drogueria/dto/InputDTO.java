package com.drogueria.dto;

import java.io.Serializable;
import java.util.List;

public class InputDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer conceptId;
	private Integer providerId;
	private Integer deliveryLocationId;
	private Integer agreementId;
	private String deliveryNoteNumber;
	private String purchaseOrderNumber;
	private String date;

	private List<InputDetailDTO> inputDetails;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getConceptId() {
		return this.conceptId;
	}

	public void setConceptId(Integer conceptId) {
		this.conceptId = conceptId;
	}

	public Integer getProviderId() {
		return this.providerId;
	}

	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}

	public Integer getAgreementId() {
		return this.agreementId;
	}

	public void setAgreementId(Integer agreementId) {
		this.agreementId = agreementId;
	}

	public String getDeliveryNoteNumber() {
		return this.deliveryNoteNumber;
	}

	public void setDeliveryNoteNumber(String deliveryNoteNumber) {
		this.deliveryNoteNumber = deliveryNoteNumber;
	}

	public String getPurchaseOrderNumber() {
		return this.purchaseOrderNumber;
	}

	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<InputDetailDTO> getInputDetails() {
		return this.inputDetails;
	}

	public void setInputDetails(List<InputDetailDTO> inputDetails) {
		this.inputDetails = inputDetails;
	}

	public Integer getDeliveryLocationId() {
		return this.deliveryLocationId;
	}

	public void setDeliveryLocationId(Integer deliveryLocationId) {
		this.deliveryLocationId = deliveryLocationId;
	}

}

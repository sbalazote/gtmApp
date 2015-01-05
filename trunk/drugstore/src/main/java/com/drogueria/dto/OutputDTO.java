package com.drogueria.dto;

import java.io.Serializable;
import java.util.List;

public class OutputDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer conceptId;
	private Integer providerId;
	private Integer deliveryLocationId;
	private Integer agreementId;
	private String date;
	private List<OutputDetailDTO> outputDetails;

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

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<OutputDetailDTO> getOutputDetails() {
		return this.outputDetails;
	}

	public void setOutputDetails(List<OutputDetailDTO> outputDetails) {
		this.outputDetails = outputDetails;
	}

	public Integer getDeliveryLocationId() {
		return this.deliveryLocationId;
	}

	public void setDeliveryLocationId(Integer deliveryLocationId) {
		this.deliveryLocationId = deliveryLocationId;
	}

}

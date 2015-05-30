package com.drogueria.dto;

import java.util.ArrayList;
import java.util.List;

public class InputDTOBuilder {

	private final InputDTO dto;

	public InputDTOBuilder() {
		this.dto = new InputDTO();
	}

	public InputDTOBuilder id(Integer id) {
		this.dto.setId(id);
		return this;
	}

	public InputDTOBuilder conceptId(Integer conceptId) {
		this.dto.setConceptId(conceptId);
		return this;
	}

	public InputDTOBuilder providerId(Integer providerId) {
		this.dto.setProviderId(providerId);
		return this;
	}

	public InputDTOBuilder deliveryLocationId(Integer deliveryLocationId) {
		this.dto.setDeliveryLocationId(deliveryLocationId);
		return this;
	}

	public InputDTOBuilder agreementId(Integer agreementId) {
		this.dto.setAgreementId(agreementId);
		return this;
	}

	public InputDTOBuilder deliveryNoteNumber(String deliveryNoteNumber) {
		this.dto.setDeliveryNoteNumber(deliveryNoteNumber);
		return this;
	}

	public InputDTOBuilder purchaseOrderNumber(String purchaseOrderNumber) {
		this.dto.setPurchaseOrderNumber(purchaseOrderNumber);
		return this;
	}

	public InputDTOBuilder date(String date) {
		this.dto.setDate(date);
		return this;
	}

	public InputDTOBuilder inputDetails(List<InputDetailDTO> inputDetails) {
		this.dto.setInputDetails(inputDetails);
		return this;
	}

	public InputDTO build() {
		return this.dto;
	}

	public InputDTOBuilder addInputDetail(InputDetailDTO iddto) {
		List<InputDetailDTO> inputDetails = this.dto.getInputDetails();
		if (inputDetails != null) {
			inputDetails.add(iddto);
		} else {
			inputDetails = new ArrayList<InputDetailDTO>();
			inputDetails.add(iddto);
			this.dto.setInputDetails(inputDetails);
		}
		return this;
	}
}
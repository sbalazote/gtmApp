package com.drogueria.dto;

import java.util.ArrayList;
import java.util.List;

public class OutputDTOBuilder {
	private final OutputDTO dto;

	public OutputDTOBuilder() {
		this.dto = new OutputDTO();
	}

	public OutputDTOBuilder id(Integer id) {
		this.dto.setId(id);
		return this;
	}

	public OutputDTOBuilder conceptId(Integer conceptId) {
		this.dto.setConceptId(conceptId);
		return this;
	}

	public OutputDTOBuilder providerId(Integer providerId) {
		this.dto.setProviderId(providerId);
		return this;
	}

	public OutputDTOBuilder deliveryLocationId(Integer deliveryLocationId) {
		this.dto.setDeliveryLocationId(deliveryLocationId);
		return this;
	}

	public OutputDTOBuilder agreementId(Integer agreementId) {
		this.dto.setAgreementId(agreementId);
		return this;
	}

	public OutputDTOBuilder date(String date) {
		this.dto.setDate(date);
		return this;
	}

	public OutputDTOBuilder inputDetails(List<OutputDetailDTO> outputDetails) {
		this.dto.setOutputDetails(outputDetails);
		return this;
	}

	public OutputDTO build() {
		return this.dto;
	}

	public OutputDTOBuilder addInputDetail(OutputDetailDTO iddto) {
		List<OutputDetailDTO> outputDetails = this.dto.getOutputDetails();
		if (outputDetails != null) {
			outputDetails.add(iddto);
		} else {
			outputDetails = new ArrayList<OutputDetailDTO>();
			outputDetails.add(iddto);
			this.dto.setOutputDetails(outputDetails);
		}
		return this;
	}
}

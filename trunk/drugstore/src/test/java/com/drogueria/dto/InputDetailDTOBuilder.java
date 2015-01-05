package com.drogueria.dto;

public class InputDetailDTOBuilder {

	private final InputDetailDTO dto;

	public InputDetailDTOBuilder() {
		this.dto = new InputDetailDTO();
	}

	public InputDetailDTOBuilder productId(Integer productId) {
		this.dto.setProductId(productId);
		return this;
	}

	public InputDetailDTOBuilder serialNumber(String serialNumber) {
		this.dto.setSerialNumber(serialNumber);
		return this;
	}

	public InputDetailDTOBuilder batch(String batch) {
		this.dto.setBatch(batch);
		return this;
	}

	public InputDetailDTOBuilder expirationDate(String expirationDate) {
		this.dto.setExpirationDate(expirationDate);
		return this;
	}

	public InputDetailDTOBuilder amount(Integer amount) {
		this.dto.setAmount(amount);
		return this;
	}

	public InputDetailDTOBuilder productType(String productType) {
		this.dto.setProductType(productType);
		return this;
	}

	public InputDetailDTO build() {
		return this.dto;
	}
}

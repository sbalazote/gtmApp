package com.drogueria.dto;

public class OutputDetailDTOBuilder {
	private final OutputDetailDTO dto;

	public OutputDetailDTOBuilder() {
		this.dto = new OutputDetailDTO();
	}

	public OutputDetailDTOBuilder productId(Integer productId) {
		this.dto.setProductId(productId);
		return this;
	}

	public OutputDetailDTOBuilder serialNumber(String serialNumber) {
		this.dto.setSerialNumber(serialNumber);
		return this;
	}

	public OutputDetailDTOBuilder batch(String batch) {
		this.dto.setBatch(batch);
		return this;
	}

	public OutputDetailDTOBuilder expirationDate(String expirationDate) {
		this.dto.setExpirationDate(expirationDate);
		return this;
	}

	public OutputDetailDTOBuilder amount(Integer amount) {
		this.dto.setAmount(amount);
		return this;
	}

	public OutputDetailDTOBuilder productType(String productType) {
		this.dto.setProductType(productType);
		return this;
	}

	public OutputDetailDTO build() {
		return this.dto;
	}
}

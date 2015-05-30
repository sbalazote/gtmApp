package com.drogueria.dto;

public class OutputOrderDetailResultDTO {
	private String product;
	private String serialNumber;
	private String batch;
	private String expirationDate;
	private Integer amount;

	public String getProduct() {
		return this.product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getBatch() {
		return this.batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Integer getAmount() {
		return this.amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public OutputOrderDetailResultDTO(String product, String serialNumber, String batch, String expirationDate, Integer amount) {
		super();
		this.product = product;
		this.serialNumber = serialNumber;
		this.batch = batch;
		this.expirationDate = expirationDate;
		this.amount = amount;
	}

}

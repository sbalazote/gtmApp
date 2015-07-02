package com.drogueria.dto;

public class OutputOrderDetailResultDTO {
	private Integer id;
	private Integer code;
	private String product;
	private String serialNumber;
	private String batch;
	private String expirationDate;
	private Integer amount;

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

	public OutputOrderDetailResultDTO(Integer id, Integer code, String product, String serialNumber, String batch, String expirationDate, Integer amount) {
		super();
		this.id = id;
		this.code = code;
		this.product = product;
		this.serialNumber = serialNumber;
		this.batch = batch;
		this.expirationDate = expirationDate;
		this.amount = amount;
	}

}

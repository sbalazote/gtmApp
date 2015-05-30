package com.drogueria.query;

public class StockQuery {

	private String expirateDateFrom;
	private String expirateDateTo;
	private Integer productId;
	private Integer agreementId;
	private String serialNumber;
	private String batchNumber;

	public static StockQuery createFromParameters(String expirateDateFrom, String expirateDateTo, Integer productId, Integer agreementId, String serialNumber,
			String batchNumber) {
		StockQuery stockQuery = new StockQuery();
		stockQuery.expirateDateFrom = expirateDateFrom;
		stockQuery.expirateDateTo = expirateDateTo;
		stockQuery.productId = productId;
		stockQuery.agreementId = agreementId;
		stockQuery.serialNumber = serialNumber;
		stockQuery.batchNumber = batchNumber;
		return stockQuery;
	}

	public String getExpirateDateFrom() {
		return this.expirateDateFrom;
	}

	public void setExpirateDateFrom(String expirateDateFrom) {
		this.expirateDateFrom = expirateDateFrom;
	}

	public String getExpirateDateTo() {
		return this.expirateDateTo;
	}

	public void setExpirateDateTo(String expirateDateTo) {
		this.expirateDateTo = expirateDateTo;
	}

	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getAgreementId() {
		return this.agreementId;
	}

	public void setAgreementId(Integer agreementId) {
		this.agreementId = agreementId;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getBatchNumber() {
		return this.batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

}

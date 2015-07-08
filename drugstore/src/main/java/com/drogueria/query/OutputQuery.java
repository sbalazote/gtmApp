package com.drogueria.query;

public class OutputQuery {
	private Integer id;
	private String dateFrom;
	private String dateTo;
	private Integer conceptId;
	private Integer providerId;
	private Integer deliveryLocationId;
	private Integer agreementId;
	private Integer productId;
	private Boolean cancelled;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDateFrom() {
		return this.dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return this.dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
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

	public Integer getDeliveryLocationId() {
		return this.deliveryLocationId;
	}

	public void setDeliveryLocationId(Integer deliveryLocationId) {
		this.deliveryLocationId = deliveryLocationId;
	}

	public Integer getAgreementId() {
		return this.agreementId;
	}

	public void setAgreementId(Integer agreementId) {
		this.agreementId = agreementId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Boolean getCancelled() {
		return this.cancelled;
	}

	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}

	public static OutputQuery createFromParameters(Integer id, String dateFrom, String dateTo, Integer conceptId, Integer providerId,
			Integer deliveryLocationId, Integer agreementId) {
		OutputQuery outputQuery = new OutputQuery();
		outputQuery.id = id;
		outputQuery.dateFrom = dateFrom;
		outputQuery.dateTo = dateTo;
		outputQuery.conceptId = conceptId;
		outputQuery.providerId = providerId;
		outputQuery.deliveryLocationId = deliveryLocationId;
		outputQuery.agreementId = agreementId;
		return outputQuery;
	}

}

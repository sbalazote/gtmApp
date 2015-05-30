package com.drogueria.query;

public class ProvisioningQuery {

	private Integer provisioningId;
	private String dateFrom;
	private String dateTo;
	private Integer agreementId;
	private Integer clientId;
	private Integer affiliateId;
	private String comment;
	private Integer deliveryLocation;
	private Integer logisticsOperator;
	private Integer stateId;

	public Integer getProvisioningId() {
		return this.provisioningId;
	}

	public void setProvisioningId(Integer provisioningId) {
		this.provisioningId = provisioningId;
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

	public Integer getAgreementId() {
		return this.agreementId;
	}

	public void setAgreementId(Integer agreementId) {
		this.agreementId = agreementId;
	}

	public Integer getClientId() {
		return this.clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getAffiliateId() {
		return this.affiliateId;
	}

	public void setAffiliateId(Integer affiliateId) {
		this.affiliateId = affiliateId;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getDeliveryLocation() {
		return this.deliveryLocation;
	}

	public void setDeliveryLocation(Integer deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}

	public Integer getLogisticsOperator() {
		return this.logisticsOperator;
	}

	public void setLogisticsOperator(Integer logisticsOperator) {
		this.logisticsOperator = logisticsOperator;
	}

	public Integer getStateId() {
		return this.stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public static ProvisioningQuery createFromParameters(Integer id, String dateFrom, String dateTo, Integer agreementId, Integer clientId,
			Integer affiliateId, String comment, Integer deliveryLocation, Integer logisticsOperator, Integer stateId) {
		ProvisioningQuery provisioningQuery = new ProvisioningQuery();
		provisioningQuery.provisioningId = id;
		provisioningQuery.dateFrom = dateFrom;
		provisioningQuery.dateTo = dateTo;
		provisioningQuery.agreementId = agreementId;
		provisioningQuery.clientId = clientId;
		provisioningQuery.affiliateId = affiliateId;
		provisioningQuery.comment = comment;
		provisioningQuery.deliveryLocation = deliveryLocation;
		provisioningQuery.logisticsOperator = logisticsOperator;
		provisioningQuery.stateId = stateId;
		return provisioningQuery;
	}
}

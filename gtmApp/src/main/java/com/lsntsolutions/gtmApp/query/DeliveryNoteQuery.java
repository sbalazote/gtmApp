package com.lsntsolutions.gtmApp.query;

public class DeliveryNoteQuery {

	private String deliveryNoteNumber;
	private String dateFrom;
	private String dateTo;
	private Integer conceptId;
	private Integer providerId;
	private Integer clientId;
	private Integer affiliateId;
	private Integer productId;
	private Integer deliveryLocationId;
	private Integer agreementId;
	private Integer outputId;
	private Integer supplyingId;
	private Integer provisioningRequestId;
	private Boolean cancelled;

	public String getDeliveryNoteNumber() {
		return deliveryNoteNumber;
	}

	public void setDeliveryNoteNumber(String deliveryNoteNumber) {
		this.deliveryNoteNumber = deliveryNoteNumber;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public Integer getConceptId() {
		return conceptId;
	}

	public void setConceptId(Integer conceptId) {
		this.conceptId = conceptId;
	}

	public Integer getProviderId() {
		return providerId;
	}

	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getAffiliateId() {
		return affiliateId;
	}

	public void setAffiliateId(Integer affiliateId) {
		this.affiliateId = affiliateId;
	}

	public Integer getDeliveryLocationId() {
		return deliveryLocationId;
	}

	public void setDeliveryLocationId(Integer deliveryLocationId) {
		this.deliveryLocationId = deliveryLocationId;
	}

	public Integer getAgreementId() {
		return agreementId;
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

	public Integer getOutputId() {
		return outputId;
	}

	public void setOutputId(Integer outputId) {
		this.outputId = outputId;
	}

	public Integer getProvisioningRequestId() {
		return provisioningRequestId;
	}

	public void setProvisioningRequestId(Integer provisioningRequestId) {
		this.provisioningRequestId = provisioningRequestId;
	}

	public Integer getSupplyingId() {
		return supplyingId;
	}

	public void setSupplyingId(Integer supplyingId) {
		this.supplyingId = supplyingId;
	}

	public Boolean getCancelled() {
		return cancelled;
	}

	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}
}
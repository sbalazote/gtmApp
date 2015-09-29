package com.lsntsolutions.gtmApp.query;

public class InputQuery {

	private Integer id;
	private String dateFrom;
	private String dateTo;
	private Integer conceptId;
	private Integer providerId;
	private Integer deliveryLocationId;
	private Integer agreementId;
	private Integer productId;
	private String deliveryNoteNumber;
	private String purchaseOrderNumber;
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

	public String getDeliveryNoteNumber() {
		return this.deliveryNoteNumber;
	}

	public void setDeliveryNoteNumber(String deliveryNoteNumber) {
		this.deliveryNoteNumber = deliveryNoteNumber;
	}

	public String getPurchaseOrderNumber() {
		return this.purchaseOrderNumber;
	}

	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	public Integer getDeliveryLocationId() {
		return this.deliveryLocationId;
	}

	public void setDeliveryLocationId(Integer deliveryLocationId) {
		this.deliveryLocationId = deliveryLocationId;
	}

	public Boolean getCancelled() {
		return this.cancelled;
	}

	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}

	public static InputQuery createFromParameters(Integer id, String dateFrom, String dateTo, Integer conceptId, Integer providerId,
												  Integer deliveryLocationId, Integer agreementId, String deliveryNoteNumber, String purchaseOrderNumber, Boolean cancelled, Integer productId) {
		InputQuery inputQuery = new InputQuery();
		inputQuery.id = id;
		inputQuery.dateFrom = dateFrom;
		inputQuery.dateTo = dateTo;
		inputQuery.conceptId = conceptId;
		inputQuery.providerId = providerId;
		inputQuery.deliveryLocationId = deliveryLocationId;
		inputQuery.agreementId = agreementId;
		inputQuery.deliveryNoteNumber = deliveryNoteNumber;
		inputQuery.purchaseOrderNumber = purchaseOrderNumber;
		inputQuery.cancelled = cancelled;
		inputQuery.productId = productId;
		return inputQuery;
	}
}

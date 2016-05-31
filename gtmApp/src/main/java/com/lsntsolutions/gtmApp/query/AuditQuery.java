package com.lsntsolutions.gtmApp.query;

public class AuditQuery {

	private String deliveryNoteNumber;
	private Integer roleId;
	private Integer operationId;
	private String dateFrom;
	private String dateTo;
	private Integer userId;
	private Integer provisioningRequestId;

	public String getDeliveryNoteNumber() {
		return deliveryNoteNumber;
	}

	public void setDeliveryNoteNumber(String deliveryNoteNumber) {
		this.deliveryNoteNumber = deliveryNoteNumber;
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getOperationId() {
		return this.operationId;
	}

	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
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

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getProvisioningRequestId() {
		return provisioningRequestId;
	}

	public void setProvisioningRequestId(Integer provisioningRequestId) {
		this.provisioningRequestId = provisioningRequestId;
	}

	public static AuditQuery createFromParameters(String dateFrom, String dateTo, Integer roleId, Integer operationId, Integer userId, String deliveryNoteNumber, Integer provisioningRequestId) {
		AuditQuery inputQuery = new AuditQuery();
		inputQuery.dateFrom = dateFrom;
		inputQuery.dateTo = dateTo;
		inputQuery.roleId = roleId;
		inputQuery.operationId = operationId;
		inputQuery.userId = userId;
		inputQuery.deliveryNoteNumber = deliveryNoteNumber;
		inputQuery.provisioningRequestId = provisioningRequestId;
		return inputQuery;
	}

}

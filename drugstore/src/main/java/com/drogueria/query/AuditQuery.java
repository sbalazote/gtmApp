package com.drogueria.query;

public class AuditQuery {

	private Integer roleId;
	private Integer operationId;
	private Integer actionId;
	private String dateFrom;
	private String dateTo;
	private Integer userId;

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

	public Integer getActionId() {
		return this.actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
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

	public static AuditQuery createFromParameters(String dateFrom, String dateTo, Integer roleId, Integer operationId, Integer userId, Integer actionId) {
		AuditQuery inputQuery = new AuditQuery();
		inputQuery.dateFrom = dateFrom;
		inputQuery.dateTo = dateTo;
		inputQuery.roleId = roleId;
		inputQuery.operationId = operationId;
		inputQuery.userId = userId;
		inputQuery.actionId = actionId;
		return inputQuery;
	}

}

package com.drogueria.dto;

public class AuditDTO {

	private Integer id;
	private String role;
	private Integer operationId;
	private String auditAction;
	private String date;
	private String username;

	public AuditDTO(Integer id, String role, Integer operationId, String auditAction, String date, String username) {
		super();
		this.id = id;
		this.role = role;
		this.operationId = operationId;
		this.auditAction = auditAction;
		this.date = date;
		this.username = username;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getOperationId() {
		return this.operationId;
	}

	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}

	public String getAuditAction() {
		return this.auditAction;
	}

	public void setAuditAction(String auditAction) {
		this.auditAction = auditAction;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}

package com.drogueria.constant;

public enum AuditState {

	COMFIRMED("Confirmado", 1), MODIFIED("Modificado", 2), AUTHORITED("Autorizado", 3), CANCELLED("Anulado", 4);

	private Integer id;
	private String description;

	private AuditState(String description, Integer id) {
		this.id = id;
		this.description = description;
	}

	public Integer getId() {
		return this.id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static AuditState fromId(Integer id) {
		for (AuditState auditState : AuditState.values()) {
			if (auditState.getId().equals(id)) {
				return auditState;
			}
		}
		return null;
	}
}

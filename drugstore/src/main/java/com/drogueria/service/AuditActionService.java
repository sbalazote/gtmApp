package com.drogueria.service;

import java.util.List;

import com.drogueria.model.AuditAction;

public interface AuditActionService {
	void save(AuditAction auditAction);

	AuditAction get(Integer id);

	List<AuditAction> getAll();

}
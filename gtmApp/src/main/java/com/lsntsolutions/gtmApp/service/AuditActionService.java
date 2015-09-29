package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.model.AuditAction;

public interface AuditActionService {
	void save(AuditAction auditAction);

	AuditAction get(Integer id);

	List<AuditAction> getAll();

}
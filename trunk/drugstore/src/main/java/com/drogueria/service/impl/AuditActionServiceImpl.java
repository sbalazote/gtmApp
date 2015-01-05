package com.drogueria.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.model.AuditAction;
import com.drogueria.persistence.dao.GenericDAO;
import com.drogueria.service.AuditActionService;

@Service
@Transactional
public class AuditActionServiceImpl implements AuditActionService {

	@Autowired
	private GenericDAO<AuditAction> genericDAO;

	@Override
	public void save(AuditAction auditAction) {
		this.genericDAO.save(auditAction);
	}

	@Override
	public AuditAction get(Integer id) {
		return this.genericDAO.get(AuditAction.class, id);
	}

	@Override
	public List<AuditAction> getAll() {
		return this.genericDAO.getAll(AuditAction.class);
	}
}
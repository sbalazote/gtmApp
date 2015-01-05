package com.drogueria.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.constant.AuditState;
import com.drogueria.dto.AuditResultDTO;
import com.drogueria.model.Audit;
import com.drogueria.model.AuditAction;
import com.drogueria.model.Role;
import com.drogueria.persistence.dao.AuditDAO;
import com.drogueria.query.AuditQuery;
import com.drogueria.service.AuditActionService;
import com.drogueria.service.AuditService;
import com.drogueria.service.RoleService;
import com.drogueria.service.UserService;

@Service
@Transactional
public class AuditServiceImpl implements AuditService {

	private static final Logger logger = Logger.getLogger(AuditServiceImpl.class);

	@Autowired
	private AuditDAO auditDAO;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private AuditActionService auditActionService;

	@Override
	public void save(Audit audit) {
		this.auditDAO.save(audit);
	}

	@Override
	public Audit get(Integer id) {
		return this.auditDAO.get(id);
	}

	@Override
	public List<Audit> getAll() {
		return this.auditDAO.getAll();
	}

	@Override
	public void addAudit(String username, Integer roleId, AuditState action, Integer operationId) {
		Audit audit = new Audit();
		AuditAction auditAction = this.auditActionService.get(action.getId());
		audit.setAuditAction(auditAction);
		audit.setDate(new Date());
		audit.setOperationId(operationId);
		Role role = this.roleService.get(roleId);
		audit.setRole(role);
		audit.setUser(this.userService.getByName(username));

		this.save(audit);
		logger.info("Se ha registrado la accion: " + auditAction.getDescription() + ", relacionado con la Operación " + role.getDescription()
				+ " realizada por: " + username);
	}

	@Override
	public List<Audit> getAuditForSearch(AuditQuery auditQuery) {
		return this.auditDAO.getAuditForSearch(auditQuery);
	}

	@Override
	public boolean getCountAuditSearch(AuditQuery auditQuery) {
		return this.auditDAO.getCountAuditSearch(auditQuery);
	}

	@Override
	public AuditResultDTO getAudit(Integer productId, String serialNumber) {
		return this.auditDAO.getAudit(productId, serialNumber);
	}

	@Override
	public AuditResultDTO getAudit(Integer productId, String batch, String expirateDate) {
		return this.auditDAO.getAudit(productId, batch, expirateDate);
	}
}

package com.drogueria.service;

import java.text.ParseException;
import java.util.List;

import com.drogueria.constant.AuditState;
import com.drogueria.dto.AuditResultDTO;
import com.drogueria.dto.OutputOrderResultDTO;
import com.drogueria.model.Audit;
import com.drogueria.query.AuditQuery;

public interface AuditService {

	void save(Audit audit);

	Audit get(Integer id);

	List<Audit> getAll();

	void addAudit(String username, Integer roleId, AuditState action, Integer operationId);

	List<Audit> getAuditForSearch(AuditQuery auditQuery);

	boolean getCountAuditSearch(AuditQuery auditQuery);

	public AuditResultDTO getAudit(Integer productId, String serialNumber);

	public AuditResultDTO getAudit(Integer productId, String batch, String expirateDate);

	public OutputOrderResultDTO getOutputOrOrder(Integer productId, String serialNumber) throws ParseException;
}

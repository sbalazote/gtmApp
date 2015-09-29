package com.lsntsolutions.gtmApp.service;

import java.text.ParseException;
import java.util.List;

import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.dto.AuditResultDTO;
import com.lsntsolutions.gtmApp.dto.OutputOrderResultDTO;
import com.lsntsolutions.gtmApp.model.Audit;
import com.lsntsolutions.gtmApp.query.AuditQuery;

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

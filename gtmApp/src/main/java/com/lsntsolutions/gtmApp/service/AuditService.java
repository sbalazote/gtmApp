package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.dto.AuditResultDTO;
import com.lsntsolutions.gtmApp.dto.OutputOrderResultDTO;
import com.lsntsolutions.gtmApp.model.Audit;
import com.lsntsolutions.gtmApp.query.AuditQuery;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface AuditService {

	void save(Audit audit);

	Audit get(Integer id);

	List<Audit> getAll();

	void addAudit(String username, Integer roleId, Integer operationId);

	List<Audit> getAuditForSearch(AuditQuery auditQuery);

	boolean getCountAuditSearch(AuditQuery auditQuery);

	AuditResultDTO getAudit(Integer productId, String serialNumber);

	AuditResultDTO getAudit(Integer productId, String batch, String expirateDate);

	OutputOrderResultDTO getOutputOrOrder(Integer productId, String serialNumber) throws ParseException;

	Date getDate(RoleOperation roleOperation, Integer operationId);
}

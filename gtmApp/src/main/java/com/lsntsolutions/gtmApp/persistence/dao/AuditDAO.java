package com.lsntsolutions.gtmApp.persistence.dao;

import java.util.Date;
import java.util.List;

import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.dto.AuditResultDTO;
import com.lsntsolutions.gtmApp.model.Audit;
import com.lsntsolutions.gtmApp.query.AuditQuery;

public interface AuditDAO {

	void save(Audit audit);

	Audit get(Integer id);

	List<Audit> getAll();

	List<Audit> getAuditForSearch(AuditQuery auditQuery);

	boolean getCountAuditSearch(AuditQuery auditQuery);

	AuditResultDTO getAudit(Integer productId, String serialNumber);

	AuditResultDTO getAudit(Integer productId, String batch, String expirateDate);

	Date getDate(RoleOperation roleOperation, Integer operationId, AuditState auditState);
}
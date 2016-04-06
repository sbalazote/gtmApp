package com.lsntsolutions.gtmApp.persistence.dao;

import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.dto.SearchAuditResultDTO;
import com.lsntsolutions.gtmApp.dto.SearchProductResultDTO;
import com.lsntsolutions.gtmApp.model.Audit;
import com.lsntsolutions.gtmApp.query.AuditQuery;

import java.util.Date;
import java.util.List;

public interface AuditDAO {

	void save(Audit audit);

	Audit get(Integer id);

	List<Audit> getAll();

	SearchAuditResultDTO getAuditForSearch(AuditQuery auditQuery);

	boolean getCountAuditSearch(AuditQuery auditQuery);

	SearchProductResultDTO getAudit(Integer productId, String serialNumber, String batch, String expirationDate);

	Date getDate(RoleOperation roleOperation, Integer operationId);
}
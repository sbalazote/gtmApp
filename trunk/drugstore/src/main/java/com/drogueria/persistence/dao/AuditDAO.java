package com.drogueria.persistence.dao;

import java.util.List;

import com.drogueria.dto.AuditResultDTO;
import com.drogueria.model.Audit;
import com.drogueria.query.AuditQuery;

public interface AuditDAO {

	void save(Audit audit);

	Audit get(Integer id);

	List<Audit> getAll();

	List<Audit> getAuditForSearch(AuditQuery auditQuery);

	boolean getCountAuditSearch(AuditQuery auditQuery);

	public AuditResultDTO getAudit(Integer productId, String serialNumber);

	public AuditResultDTO getAudit(Integer productId, String batch, String expirateDate);
}
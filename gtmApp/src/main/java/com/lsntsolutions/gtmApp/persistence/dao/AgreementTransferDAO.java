package com.lsntsolutions.gtmApp.persistence.dao;

import java.util.List;

import com.lsntsolutions.gtmApp.model.AgreementTransfer;

public interface AgreementTransferDAO {
	void save(AgreementTransfer agreementTransfer);

	AgreementTransfer get(Integer id);

	List<AgreementTransfer> getAll();
}

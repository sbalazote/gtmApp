package com.drogueria.persistence.dao;

import java.util.List;

import com.drogueria.model.AgreementTransfer;

public interface AgreementTransferDAO {
	void save(AgreementTransfer agreementTransfer);

	AgreementTransfer get(Integer id);

	List<AgreementTransfer> getAll();
}

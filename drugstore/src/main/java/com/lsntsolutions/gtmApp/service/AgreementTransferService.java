package com.lsntsolutions.gtmApp.service;

import java.text.ParseException;
import java.util.List;

import com.lsntsolutions.gtmApp.dto.AgreementTransferDTO;
import com.lsntsolutions.gtmApp.model.AgreementTransfer;

public interface AgreementTransferService {

	void save(AgreementTransfer agreementTransfer);

	AgreementTransfer get(Integer id);

	List<AgreementTransfer> getAll();

	void updateAgreementStock(AgreementTransferDTO agreementTransferDTO, String name) throws ParseException;
}

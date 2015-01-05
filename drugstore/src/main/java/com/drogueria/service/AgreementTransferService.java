package com.drogueria.service;

import java.text.ParseException;
import java.util.List;

import com.drogueria.dto.AgreementTransferDTO;
import com.drogueria.model.AgreementTransfer;

public interface AgreementTransferService {

	void save(AgreementTransfer agreementTransfer);

	AgreementTransfer get(Integer id);

	List<AgreementTransfer> getAll();

	void updateAgreementStock(AgreementTransferDTO agreementTransferDTO, String name) throws ParseException;
}

package com.lsntsolutions.gtmApp.service.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Agreement;
import com.lsntsolutions.gtmApp.service.AgreementService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lsntsolutions.gtmApp.persistence.dao.AgreementDAO;

@Service
@Transactional
public class AgreementServiceImpl implements AgreementService {

	private static final Logger logger = Logger.getLogger(AgreementServiceImpl.class);

	@Autowired
	private AgreementDAO agreementDAO;

	@Override
	public void save(Agreement agreement) {
		this.agreementDAO.save(agreement);
		logger.info("Se han guardado los cambios exitosamente. Id de Convenio: " + agreement.getId());
	}

	@Override
	public Agreement get(Integer id) {
		return this.agreementDAO.get(id);
	}

	@Override
	public Boolean exists(Integer code) {
		return this.agreementDAO.exists(code);
	}

	@Override
	public List<Agreement> getForAutocomplete(String term, Boolean active) {
		return this.agreementDAO.getForAutocomplete(term, active);
	}

	@Override
	public List<Agreement> getAll() {
		return this.agreementDAO.getAll();
	}

	@Override
	public List<Agreement> getAllActives() {
		return this.agreementDAO.getAllActives();
	}

	@Override
	public boolean delete(Integer agreementId) {
		return this.agreementDAO.delete(agreementId);
	}

	@Override
	public List<Agreement> getPaginated(int start, int length) {
		return this.agreementDAO.getPaginated(start, length);
	}

	@Override
	public Long getTotalNumber() {
		return this.agreementDAO.getTotalNumber();
	}

	@Override
	public boolean isConceptInUse(Integer conceptId){
		return this.agreementDAO.isConceptInUse(conceptId);
	}
}

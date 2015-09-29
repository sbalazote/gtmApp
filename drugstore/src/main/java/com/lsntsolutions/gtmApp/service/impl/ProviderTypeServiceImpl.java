package com.lsntsolutions.gtmApp.service.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProviderType;
import com.lsntsolutions.gtmApp.service.ProviderTypeService;
import com.lsntsolutions.gtmApp.persistence.dao.ProviderTypeDAO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProviderTypeServiceImpl implements ProviderTypeService {

	private static final Logger logger = Logger.getLogger(ProviderTypeServiceImpl.class);

	@Autowired
	private ProviderTypeDAO providerTypeDAO;

	@Override
	public void save(ProviderType providerType) {
		this.providerTypeDAO.save(providerType);
		logger.info("Se han guardado los cambios exitosamente. Id de Tipo de Proveedor: " + providerType.getId());
	}

	@Override
	public ProviderType get(Integer id) {
		return this.providerTypeDAO.get(id);
	}

	@Override
	public Boolean exists(Integer code) {
		return this.providerTypeDAO.exists(code);
	}

	@Override
	public List<ProviderType> getForAutocomplete(String term, Boolean active) {
		return this.providerTypeDAO.getForAutocomplete(term, active);
	}

	@Override
	public List<ProviderType> getAll() {
		return this.providerTypeDAO.getAll();
	}

	@Override
	public boolean delete(Integer providerTypeId) {
		return this.providerTypeDAO.delete(providerTypeId);
	}

	@Override
	public List<ProviderType> getPaginated(int start, int length) {
		return this.providerTypeDAO.getPaginated(start, length);
	}

	@Override
	public Long getTotalNumber() {
		return this.providerTypeDAO.getTotalNumber();
	}
}

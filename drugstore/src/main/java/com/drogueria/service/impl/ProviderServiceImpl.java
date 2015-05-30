package com.drogueria.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.model.Provider;
import com.drogueria.persistence.dao.ProviderDAO;
import com.drogueria.service.ProviderService;

@Service
@Transactional
public class ProviderServiceImpl implements ProviderService {

	private static final Logger logger = Logger.getLogger(ProviderServiceImpl.class);

	@Autowired
	private ProviderDAO providerDAO;

	@Override
	public void save(Provider provider) {
		this.providerDAO.save(provider);
		logger.info("Se han guardado los cambios exitosamente. Id de Proveedor: " + provider.getId());
	}

	@Override
	public Provider get(Integer id) {
		return this.providerDAO.get(id);
	}

	@Override
	public Boolean exists(Integer code) {
		return this.providerDAO.exists(code);
	}

	@Override
	public List<Provider> getForAutocomplete(String term, Boolean active) {
		return this.providerDAO.getForAutocomplete(term, active);
	}

	@Override
	public List<Provider> getAll() {
		return this.providerDAO.getAll();
	}

	@Override
	public List<Provider> getAllActives() {
		return this.providerDAO.getAllActives();
	}

	@Override
	public boolean delete(Integer providerId) {
		return this.providerDAO.delete(providerId);
	}

	@Override
	public List<Provider> getPaginated(int start, int length) {
		return this.providerDAO.getPaginated(start, length);
	}

	@Override
	public Long getTotalNumber() {
		return this.providerDAO.getTotalNumber();
	}
}

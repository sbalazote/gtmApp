package com.drogueria.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.model.ProductMonodrug;
import com.drogueria.persistence.dao.ProductMonodrugDAO;
import com.drogueria.service.ProductMonodrugService;

@Service
@Transactional
public class ProductMonodrugServiceImpl implements ProductMonodrugService {

	private static final Logger logger = Logger.getLogger(ProductMonodrugServiceImpl.class);

	@Autowired
	private ProductMonodrugDAO productMonodrugDAO;

	@Override
	public void save(ProductMonodrug productMonodrug) {
		this.productMonodrugDAO.save(productMonodrug);
		logger.info("Se han guardado los cambios exitosamente. Id de Monodroga: " + productMonodrug.getId());
	}

	@Override
	public ProductMonodrug get(Integer id) {
		return this.productMonodrugDAO.get(id);
	}

	@Override
	public Boolean exists(Integer code) {
		return this.productMonodrugDAO.exists(code);
	}

	@Override
	public List<ProductMonodrug> getForAutocomplete(String term, Boolean active) {
		return this.productMonodrugDAO.getForAutocomplete(term, active);
	}

	@Override
	public List<ProductMonodrug> getForAutocomplete(String description) {
		return this.productMonodrugDAO.getForAutocomplete(description);
	}

	@Override
	public List<ProductMonodrug> getAll() {
		return this.productMonodrugDAO.getAll();
	}

	@Override
	public boolean delete(Integer productMonodrugId) {
		return this.productMonodrugDAO.delete(productMonodrugId);
	}

	@Override
	public List<ProductMonodrug> getPaginated(int start, int length) {
		return this.productMonodrugDAO.getPaginated(start, length);
	}

	@Override
	public Long getTotalNumber() {
		return this.productMonodrugDAO.getTotalNumber();
	}
}
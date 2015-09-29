package com.lsntsolutions.gtmApp.service.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProductGroup;
import com.lsntsolutions.gtmApp.persistence.dao.ProductGroupDAO;
import com.lsntsolutions.gtmApp.service.ProductGroupService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductGroupServiceImpl implements ProductGroupService {

	private static final Logger logger = Logger.getLogger(ProductGroupServiceImpl.class);

	@Autowired
	private ProductGroupDAO productGroupDAO;

	@Override
	public void save(ProductGroup productGroup) {
		this.productGroupDAO.save(productGroup);
		logger.info("Se han guardado los cambios exitosamente. Id de Agrupacion: " + productGroup.getId());
	}

	@Override
	public ProductGroup get(Integer id) {
		return this.productGroupDAO.get(id);
	}

	@Override
	public Boolean exists(Integer code) {
		return this.productGroupDAO.exists(code);
	}

	@Override
	public List<ProductGroup> getForAutocomplete(String term, Boolean active) {
		return this.productGroupDAO.getForAutocomplete(term, active);
	}

	@Override
	public List<ProductGroup> getAll() {
		return this.productGroupDAO.getAll();
	}

	@Override
	public boolean delete(Integer productGroupId) {
		return this.productGroupDAO.delete(productGroupId);
	}

	@Override
	public List<ProductGroup> getPaginated(int start, int length) {
		return this.productGroupDAO.getPaginated(start, length);
	}

	@Override
	public Long getTotalNumber() {
		return this.productGroupDAO.getTotalNumber();
	}
}
package com.lsntsolutions.gtmApp.service.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProductDrugCategory;
import com.lsntsolutions.gtmApp.service.ProductDrugCategoryService;
import com.lsntsolutions.gtmApp.persistence.dao.ProductDrugCategoryDAO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductDrugCategoryServiceImpl implements ProductDrugCategoryService {

	private static final Logger logger = Logger.getLogger(ProductDrugCategoryServiceImpl.class);

	@Autowired
	private ProductDrugCategoryDAO productDrugCategoryDAO;

	@Override
	public void save(ProductDrugCategory productDrugCategory) {
		this.productDrugCategoryDAO.save(productDrugCategory);
		logger.info("Se han guardado los cambios exitosamente. Id de Accionn Farmacologica: " + productDrugCategory.getId());
	}

	@Override
	public ProductDrugCategory get(Integer id) {
		return this.productDrugCategoryDAO.get(id);
	}

	@Override
	public Boolean exists(Integer code) {
		return this.productDrugCategoryDAO.exists(code);
	}

	@Override
	public List<ProductDrugCategory> getForAutocomplete(String term, Boolean active) {
		return this.productDrugCategoryDAO.getForAutocomplete(term, active);
	}

	@Override
	public List<ProductDrugCategory> getForAutocomplete(String description) {
		return this.productDrugCategoryDAO.getForAutocomplete(description);
	}

	@Override
	public List<ProductDrugCategory> getAll() {
		return this.productDrugCategoryDAO.getAll();
	}

	@Override
	public boolean delete(Integer productDrugCategoryId) {
		return this.productDrugCategoryDAO.delete(productDrugCategoryId);
	}

	@Override
	public List<ProductDrugCategory> getPaginated(int start, int length) {
		return this.productDrugCategoryDAO.getPaginated(start, length);
	}

	@Override
	public Long getTotalNumber() {
		return this.productDrugCategoryDAO.getTotalNumber();
	}
}
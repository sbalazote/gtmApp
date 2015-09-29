package com.lsntsolutions.gtmApp.service.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.persistence.dao.ProductBrandDAO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lsntsolutions.gtmApp.model.ProductBrand;
import com.lsntsolutions.gtmApp.service.ProductBrandService;

@Service
@Transactional
public class ProductBrandServiceImpl implements ProductBrandService {

	private static final Logger logger = Logger.getLogger(ProductBrandServiceImpl.class);

	@Autowired
	private ProductBrandDAO productBrandDAO;

	@Override
	public void save(ProductBrand productBrand) {
		this.productBrandDAO.save(productBrand);
		logger.info("Se han guardado los cambios exitosamente. Id de Marca: " + productBrand.getId());
	}

	@Override
	public ProductBrand get(Integer id) {
		return this.productBrandDAO.get(id);
	}

	@Override
	public Boolean exists(Integer code) {
		return this.productBrandDAO.exists(code);
	}

	@Override
	public List<ProductBrand> getForAutocomplete(String term, Boolean active) {
		return this.productBrandDAO.getForAutocomplete(term, active);
	}

	@Override
	public List<ProductBrand> getForAutocomplete(String term) {
		return this.productBrandDAO.getForAutocomplete(term);
	}

	@Override
	public List<ProductBrand> getAll() {
		return this.productBrandDAO.getAll();
	}

	@Override
	public boolean delete(Integer productBrandId) {
		return this.productBrandDAO.delete(productBrandId);
	}

	@Override
	public List<ProductBrand> getPaginated(int start, int length) {
		return this.productBrandDAO.getPaginated(start, length);
	}

	@Override
	public Long getTotalNumber() {
		return this.productBrandDAO.getTotalNumber();
	}
}

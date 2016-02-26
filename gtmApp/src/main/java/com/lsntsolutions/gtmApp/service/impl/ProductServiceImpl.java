package com.lsntsolutions.gtmApp.service.impl;

import com.lsntsolutions.gtmApp.model.Product;
import com.lsntsolutions.gtmApp.persistence.dao.ProductDAO;
import com.lsntsolutions.gtmApp.service.*;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = Logger.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductDAO productDAO;

	@Override
	public void save(Product product) {
		this.productDAO.save(product);
		logger.info("Se han guardado los cambios exitosamente. Id de Producto: " + product.getId());
	}

	@Override
	public Product get(Integer id) {
		return this.productDAO.get(id);
	}

	@Override
	public Boolean exists(Integer code) {
		return this.productDAO.exists(code);
	}

	@Override
	public List<Product> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortDescription, String sortGtin, String sortIsCold, String sortIsActive, Integer start, Integer length, Integer brandId, Integer monodrugId, Integer productGroupId, Integer drugCategoryId, Boolean productFilterColdOption) {
		return this.productDAO.getForAutocomplete(term, active, sortId, sortCode, sortDescription, sortGtin, sortIsCold, sortIsActive, start, length, brandId, monodrugId, productGroupId, drugCategoryId, productFilterColdOption);
	}

	@Override
	public Integer getTotalNumberOfRows(String searchPhrase, Boolean active, String sortId, String sortCode, String sortDescription, String sortGtin, String sortIsCold, String sortIsActive) {
		return this.productDAO.getTotalNumberOfRows(searchPhrase, active, sortId, sortCode, sortDescription, sortGtin, sortIsCold, sortIsActive);
	}

	@Override
	public Product getByGtin(String gtin) {
		return this.productDAO.getByGtin(gtin);
	}

	@Override
	public Product getByGtin(String gtin, Boolean active) {
		return this.productDAO.getByGtin(gtin,active);
	}

	@Override
	public boolean delete(Integer productId) {
		return this.productDAO.delete(productId);
	}

	@Override
	public Integer updateFromAlfabeta(String description, BigDecimal price, Integer code, String gtin, Boolean cold) {
		return this.productDAO.updateFromAlfabeta(description, price, code, gtin, cold);
	}

	@Override
	public List<Product> getAll() {
		return this.productDAO.getAll();
	}

	@Override
	public List<Product> getPaginated(int start, int length) {
		return this.productDAO.getPaginated(start, length);
	}

	@Override
	public Long getTotalNumber() {
		return this.productDAO.getTotalNumber();
	}
}

package com.drogueria.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.model.Product;
import com.drogueria.model.ProductGtin;
import com.drogueria.model.ProductPrice;
import com.drogueria.persistence.dao.ProductDAO;
import com.drogueria.service.ProductService;

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
	public List<Product> getForAutocomplete(String term, Boolean active) {
		return this.productDAO.getForAutocomplete(term, active);
	}

	@Override
	public Product getByGtin(String gtin) {
		return this.productDAO.getByGtin(gtin);
	}

	@Override
	public boolean delete(Integer productId) {
		return this.productDAO.delete(productId);
	}

	@Override
	public boolean updateFromAlfabeta(Integer code, String gtin, BigDecimal price) {
		Product product = this.productDAO.getByCode(code);
		ProductGtin productGtin = new ProductGtin();
		productGtin.setDate(new Date());
		productGtin.setNumber(gtin);
		product.getGtins().add(productGtin);
		ProductPrice productPrice = new ProductPrice();
		productPrice.setDate(new Date());
		productPrice.setPrice(price);
		product.getPrices().add(productPrice);

		return this.productDAO.updateFromAlfabeta(product);
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

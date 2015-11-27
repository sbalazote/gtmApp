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
	private SessionFactory sessionFactory;

	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private ProductGtinService productGtinService;

	@Autowired
	private ProductBrandService productBrandService;

	@Autowired
	private ProductMonodrugService productMonodrugService;

	@Autowired
	private ProductGroupService productGroupService;

	@Autowired
	private ProductDrugCategoryService productDrugCategoryService;

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
		/*Product product;
		if (this.productDAO.exists(code)) {
			product = this.productDAO.getByCode(code);
			ProductGtin productGtin = new ProductGtin();
			productGtin.setDate(new Date());
			productGtin.setNumber(gtin);
			List<ProductGtin> gtins = product.getGtins();
			if (!gtins.contains(productGtin) && (this.productGtinService.getByNumber(gtin) == null)) {
				gtins.add(productGtin);
			}
			ProductPrice productPrice = new ProductPrice();
			productPrice.setDate(new Date());
			productPrice.setPrice(price);
			List<ProductPrice> prices = product.getPrices();
			prices.add(productPrice);
			this.productDAO.save(product);
			logger.info("El producto con codigo nro. " + code + " ha sido actualizado.");

		} else {
			// si no existe el producto se da de alta pero como inactivo siempre y cuando el gtin no exista.
			if (this.productGtinService.getByNumber(gtin) == null) {
				product = new Product();
				List<ProductGtin> productGtins = new ArrayList<ProductGtin>();
				ProductGtin productGtin = new ProductGtin();
				productGtin.setDate(new Date());
				productGtin.setNumber(gtin);
				productGtins.add(productGtin);
				product.setGtins(productGtins);

				List<ProductPrice> productPrices = new ArrayList<ProductPrice>();
				ProductPrice productPrice = new ProductPrice();
				productPrice.setDate(new Date());
				productPrice.setPrice(price);
				productPrices.add(productPrice);
				product.setPrices(productPrices);

				product.setCode(code);
				product.setDescription(description);
				product.setBrand(this.productBrandService.get(1));
				product.setMonodrug(this.productMonodrugService.get(1));
				product.setGroup(this.productGroupService.get(1));
				product.setDrugCategory(this.productDrugCategoryService.get(1));
				product.setCold(cold);
				// TODO que va aca??
				product.setInformAnmat(false);
				// TODO que va aca??
				product.setType("BE");
				product.setActive(false);

				this.productDAO.save(product);
				logger.info("El producto con codigo nro. " + code + " no existe. Se ha dado de alta.");
			} else {
				logger.info("El producto con codigo nro. " + code + " no existe pero su GTIN " + gtin + " ya existe. No se puede dar de alta");
			}
		}*/
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

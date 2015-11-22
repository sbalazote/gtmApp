package com.lsntsolutions.gtmApp.service;

import java.math.BigDecimal;
import java.util.List;

import com.lsntsolutions.gtmApp.model.Product;

public interface ProductService {

	void save(Product product);

	Product get(Integer id);

	Boolean exists(Integer code);

	List<Product> getForAutocomplete(String term, Boolean active);

	Product getByGtin(String gtin);

	//TODO reemplazar el metodo getByGtin(String) por getByGtin(String, Boolean)
	Product getByGtin(String gtin, Boolean active);

	boolean delete(Integer productId);

	void updateFromAlfabeta(String description, BigDecimal price, Integer code, String gtin, Boolean cold);

	List<Product> getAll();

	List<Product> getPaginated(int start, int length);

	Long getTotalNumber();
}

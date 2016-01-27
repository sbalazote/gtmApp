package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

	void save(Product product);

	Product get(Integer id);

	Boolean exists(Integer code);

	List<Product> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortDescription, String sortGtin, String sortIsCold, String sortIsActive, Integer start, Integer length);

	Integer getTotalNumberOfRows();

	Product getByGtin(String gtin);

	//TODO reemplazar el metodo getByGtin(String) por getByGtin(String, Boolean)
	Product getByGtin(String gtin, Boolean active);

	boolean delete(Integer productId);

	Integer updateFromAlfabeta(String description, BigDecimal price, Integer code, String gtin, Boolean cold);

	List<Product> getAll();

	List<Product> getPaginated(int start, int length);

	Long getTotalNumber();
}

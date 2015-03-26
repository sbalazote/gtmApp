package com.drogueria.service;

import java.math.BigDecimal;
import java.util.List;

import com.drogueria.model.Product;

public interface ProductService {

	void save(Product product);

	Product get(Integer id);

	Boolean exists(Integer code);

	List<Product> getForAutocomplete(String term, Boolean active);

	Product getByGtin(String gtin);

	boolean delete(Integer productId);

	boolean updateFromAlfabeta(Integer code, String gtin, BigDecimal price);

	List<Product> getAll();

	List<Product> getPaginated(int start, int length);

	Long getTotalNumber();
}
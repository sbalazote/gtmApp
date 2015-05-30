package com.drogueria.persistence.dao;

import java.util.List;

import com.drogueria.model.Product;

public interface ProductDAO {

	void save(Product product);

	Product get(Integer id);

	Boolean exists(Integer code);

	List<Product> getForAutocomplete(String term, Boolean active);

	Product getByGtin(String gtin);

	boolean delete(Integer productId);

	List<Product> getAll();

	List<Product> getPaginated(int start, int length);

	Long getTotalNumber();

	Product getByCode(Integer code);

	boolean updateFromAlfabeta(Product product);

}

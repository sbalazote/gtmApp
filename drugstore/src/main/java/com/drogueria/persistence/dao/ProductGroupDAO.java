package com.drogueria.persistence.dao;

import java.util.List;

import com.drogueria.model.ProductGroup;

public interface ProductGroupDAO {

	void save(ProductGroup product);

	ProductGroup get(Integer id);

	Boolean exists(Integer code);

	List<ProductGroup> getForAutocomplete(String term, Boolean active);

	List<ProductGroup> getAll();

	boolean delete(Integer productGroupId);

	List<ProductGroup> getPaginated(int start, int length);

	Long getTotalNumber();
}

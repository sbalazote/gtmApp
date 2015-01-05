package com.drogueria.persistence.dao;

import java.util.List;

import com.drogueria.model.ProductDrugCategory;

public interface ProductDrugCategoryDAO {

	void save(ProductDrugCategory productDrugCategory);

	ProductDrugCategory get(Integer id);

	Boolean exists(Integer code);

	List<ProductDrugCategory> getForAutocomplete(String term, Boolean active);

	List<ProductDrugCategory> getForAutocomplete(String term);

	List<ProductDrugCategory> getAll();

	boolean delete(Integer productDrugCategoryId);

	List<ProductDrugCategory> getPaginated(int start, int length);

	Long getTotalNumber();
}

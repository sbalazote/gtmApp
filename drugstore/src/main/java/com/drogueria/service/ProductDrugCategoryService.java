package com.drogueria.service;

import java.util.List;

import com.drogueria.model.ProductDrugCategory;

public interface ProductDrugCategoryService {

	void save(ProductDrugCategory productDrugCategory);

	ProductDrugCategory get(Integer id);

	Boolean exists(Integer code);

	List<ProductDrugCategory> getForAutocomplete(String term, Boolean active);

	List<ProductDrugCategory> getForAutocomplete(String description);

	List<ProductDrugCategory> getAll();

	boolean delete(Integer productDrugCategoryId);

	List<ProductDrugCategory> getPaginated(int start, int length);

	Long getTotalNumber();
}

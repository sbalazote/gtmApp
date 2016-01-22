package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.ProductDrugCategory;

import java.util.List;

public interface ProductDrugCategoryService {

	void save(ProductDrugCategory productDrugCategory);

	ProductDrugCategory get(Integer id);

	Boolean exists(Integer code);

	List<ProductDrugCategory> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortDescription, String sortIsActive);

	List<ProductDrugCategory> getForAutocomplete(String description);

	List<ProductDrugCategory> getAll();

	boolean delete(Integer productDrugCategoryId);

	List<ProductDrugCategory> getPaginated(int start, int length);

	Long getTotalNumber();
}

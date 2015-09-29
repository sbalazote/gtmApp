package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProductGroup;

public interface ProductGroupService {

	void save(ProductGroup product);

	ProductGroup get(Integer id);

	Boolean exists(Integer code);

	List<ProductGroup> getForAutocomplete(String term, Boolean active);

	List<ProductGroup> getAll();

	boolean delete(Integer productGroupId);

	List<ProductGroup> getPaginated(int start, int length);

	Long getTotalNumber();
}

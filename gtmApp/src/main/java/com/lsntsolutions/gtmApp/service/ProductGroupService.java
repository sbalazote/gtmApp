package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.ProductGroup;

import java.util.List;

public interface ProductGroupService {

	void save(ProductGroup product);

	ProductGroup get(Integer id);

	Boolean exists(Integer code);

	List<ProductGroup> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortDescription, String sortIsActive);

	List<ProductGroup> getAll();

	boolean delete(Integer productGroupId);

	List<ProductGroup> getPaginated(int start, int length);

	Long getTotalNumber();
}

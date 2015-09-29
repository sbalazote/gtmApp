package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProductBrand;

public interface ProductBrandService {

	void save(ProductBrand productBrand);

	ProductBrand get(Integer id);

	Boolean exists(Integer code);

	List<ProductBrand> getForAutocomplete(String term, Boolean active);

	List<ProductBrand> getForAutocomplete(String term);

	List<ProductBrand> getAll();

	boolean delete(Integer productBrandId);

	List<ProductBrand> getPaginated(int start, int length);

	Long getTotalNumber();
}

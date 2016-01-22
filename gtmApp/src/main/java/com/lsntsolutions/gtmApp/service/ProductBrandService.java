package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.ProductBrand;

import java.util.List;

public interface ProductBrandService {

	void save(ProductBrand productBrand);

	ProductBrand get(Integer id);

	Boolean exists(Integer code);

	List<ProductBrand> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortDescription, String sortIsActive);

	List<ProductBrand> getForAutocomplete(String term);

	List<ProductBrand> getAll();

	boolean delete(Integer productBrandId);

	List<ProductBrand> getPaginated(int start, int length);

	Long getTotalNumber();
}

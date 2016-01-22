package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.ProductMonodrug;

import java.util.List;

public interface ProductMonodrugService {

	void save(ProductMonodrug productMonodrug);

	ProductMonodrug get(Integer id);

	Boolean exists(Integer code);

	List<ProductMonodrug> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortDescription, String sortIsActive);

	List<ProductMonodrug> getForAutocomplete(String description);

	List<ProductMonodrug> getAll();

	boolean delete(Integer productMonodrugId);

	List<ProductMonodrug> getPaginated(int start, int length);

	Long getTotalNumber();
}
package com.lsntsolutions.gtmApp.persistence.dao;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProductMonodrug;

public interface ProductMonodrugDAO {

	void save(ProductMonodrug productMonodrug);

	ProductMonodrug get(Integer id);

	Boolean exists(Integer code);

	List<ProductMonodrug> getForAutocomplete(String term, Boolean active);

	List<ProductMonodrug> getForAutocomplete(String term);

	List<ProductMonodrug> getAll();

	boolean delete(Integer productMonodrugId);

	List<ProductMonodrug> getPaginated(int start, int length);

	Long getTotalNumber();
}
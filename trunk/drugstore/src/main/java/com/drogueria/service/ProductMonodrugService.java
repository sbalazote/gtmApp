package com.drogueria.service;

import java.util.List;

import com.drogueria.model.ProductMonodrug;

public interface ProductMonodrugService {

	void save(ProductMonodrug productMonodrug);

	ProductMonodrug get(Integer id);

	Boolean exists(Integer code);

	List<ProductMonodrug> getForAutocomplete(String term, Boolean active);

	List<ProductMonodrug> getForAutocomplete(String description);

	List<ProductMonodrug> getAll();

	boolean delete(Integer productMonodrugId);

	List<ProductMonodrug> getPaginated(int start, int length);

	Long getTotalNumber();
}
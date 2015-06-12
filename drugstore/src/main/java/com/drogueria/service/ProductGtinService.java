package com.drogueria.service;

import java.util.List;

import com.drogueria.model.ProductGtin;

public interface ProductGtinService {
	void save(ProductGtin productGtin);

	ProductGtin get(Integer id);

	List<ProductGtin> getAll();

	ProductGtin getByNumber(String number);

	boolean isGtinUsed(String number);
}

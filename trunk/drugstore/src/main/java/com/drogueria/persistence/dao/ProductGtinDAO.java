package com.drogueria.persistence.dao;

import java.util.List;

import com.drogueria.model.ProductGtin;

public interface ProductGtinDAO {

	void save(ProductGtin productGtin);

	ProductGtin get(Integer id);

	List<ProductGtin> getAll();

	ProductGtin getByNumber(String number);
}

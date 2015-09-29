package com.lsntsolutions.gtmApp.persistence.dao;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProductGtin;

public interface ProductGtinDAO {

	void save(ProductGtin productGtin);

	ProductGtin get(Integer id);

	List<ProductGtin> getAll();

	ProductGtin getByNumber(String number);

	boolean isGtinUsed(String number);
}

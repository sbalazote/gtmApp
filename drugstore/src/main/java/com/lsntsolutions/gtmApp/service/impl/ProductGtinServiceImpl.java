package com.lsntsolutions.gtmApp.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lsntsolutions.gtmApp.model.ProductGtin;
import com.lsntsolutions.gtmApp.persistence.dao.ProductGtinDAO;
import com.lsntsolutions.gtmApp.service.ProductGtinService;

@Service
@Transactional
public class ProductGtinServiceImpl implements ProductGtinService {

	private static final Logger logger = Logger.getLogger(ProductGtinServiceImpl.class);

	@Autowired
	private ProductGtinDAO productGtinDAO;

	@Override
	public void save(ProductGtin productGtin) {
		this.productGtinDAO.save(productGtin);
	}

	@Override
	public ProductGtin get(Integer id) {
		return this.productGtinDAO.get(id);
	}

	@Override
	public List<ProductGtin> getAll() {
		return this.productGtinDAO.getAll();
	}

	@Override
	public ProductGtin getByNumber(String number) {
		return this.productGtinDAO.getByNumber(number);
	}

	@Override
	public boolean isGtinUsed(String number) {
		return this.productGtinDAO.isGtinUsed(number);
	}
}

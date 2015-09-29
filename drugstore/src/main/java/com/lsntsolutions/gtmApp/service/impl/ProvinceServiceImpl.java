package com.lsntsolutions.gtmApp.service.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Province;
import com.lsntsolutions.gtmApp.service.ProvinceService;
import com.lsntsolutions.gtmApp.persistence.dao.GenericDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProvinceServiceImpl implements ProvinceService {

	@Autowired
	private GenericDAO<Province> genericDAO;

	@Override
	public void save(Province province) {
		this.genericDAO.save(province);
	}

	@Override
	public Province get(Integer id) {
		return this.genericDAO.get(Province.class, id);
	}

	@Override
	public List<Province> getAll() {
		return this.genericDAO.getAll(Province.class);
	}

}
package com.drogueria.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.model.Province;
import com.drogueria.persistence.dao.GenericDAO;
import com.drogueria.service.ProvinceService;

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
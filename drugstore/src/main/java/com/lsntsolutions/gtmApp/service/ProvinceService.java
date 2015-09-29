package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Province;

public interface ProvinceService {

	void save(Province province);

	Province get(Integer id);

	List<Province> getAll();
}

package com.drogueria.service;

import java.util.List;

import com.drogueria.model.Province;

public interface ProvinceService {

	void save(Province province);

	Province get(Integer id);

	List<Province> getAll();
}

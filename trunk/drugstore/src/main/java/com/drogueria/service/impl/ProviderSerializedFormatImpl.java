package com.drogueria.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.model.ProviderSerializedFormat;
import com.drogueria.persistence.dao.ProviderSerializedFormatDAO;
import com.drogueria.service.ProviderSerializedFormatService;

@Service
@Transactional
public class ProviderSerializedFormatImpl implements ProviderSerializedFormatService {

	@Autowired
	private ProviderSerializedFormatDAO providerSerializedFormatDAO;

	@Override
	public void save(ProviderSerializedFormat providerSerializedFormat) {
		this.providerSerializedFormatDAO.save(providerSerializedFormat);
	}

	@Override
	public ProviderSerializedFormat get(Integer id) {
		return this.providerSerializedFormatDAO.get(id);
	}

	@Override
	public List<ProviderSerializedFormat> getAll() {
		return this.providerSerializedFormatDAO.getAll();
	}

	@Override
	public boolean delete(Integer id) {
		return this.providerSerializedFormatDAO.delete(id);
	}
}

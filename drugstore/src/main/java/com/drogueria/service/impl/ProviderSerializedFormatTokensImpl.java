package com.drogueria.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.model.ProviderSerializedFormatTokens;
import com.drogueria.persistence.dao.ProviderSerializedFormatTokensDAO;
import com.drogueria.service.ProviderSerializedFormatTokensService;

@Service
@Transactional
public class ProviderSerializedFormatTokensImpl implements ProviderSerializedFormatTokensService {

	@Autowired
	private ProviderSerializedFormatTokensDAO providerSerializedFormatTokensDAO;

	@Override
	public void save(ProviderSerializedFormatTokens providerSerializedFormatTokens) {
		this.providerSerializedFormatTokensDAO.save(providerSerializedFormatTokens);
	}

	@Override
	public ProviderSerializedFormatTokens get(Integer id) {
		return this.providerSerializedFormatTokensDAO.get(id);
	}

	@Override
	public List<ProviderSerializedFormatTokens> getAll() {
		return this.providerSerializedFormatTokensDAO.getAll();
	}
}

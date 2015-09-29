package com.lsntsolutions.gtmApp.service.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProviderSerializedFormatTokens;
import com.lsntsolutions.gtmApp.persistence.dao.ProviderSerializedFormatTokensDAO;
import com.lsntsolutions.gtmApp.service.ProviderSerializedFormatTokensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

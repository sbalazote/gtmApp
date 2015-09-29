package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProviderSerializedFormatTokens;

public interface ProviderSerializedFormatTokensService {

	void save(ProviderSerializedFormatTokens providerSerializedFormatTokens);

	ProviderSerializedFormatTokens get(Integer id);

	List<ProviderSerializedFormatTokens> getAll();

}

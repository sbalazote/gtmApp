package com.drogueria.service;

import java.util.List;

import com.drogueria.model.ProviderSerializedFormatTokens;

public interface ProviderSerializedFormatTokensService {

	void save(ProviderSerializedFormatTokens providerSerializedFormatTokens);

	ProviderSerializedFormatTokens get(Integer id);

	List<ProviderSerializedFormatTokens> getAll();

}

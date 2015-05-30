package com.drogueria.persistence.dao;

import java.util.List;

import com.drogueria.model.ProviderSerializedFormatTokens;

public interface ProviderSerializedFormatTokensDAO {

	void save(ProviderSerializedFormatTokens providerSerializedFormatTokens);

	ProviderSerializedFormatTokens get(Integer id);

	List<ProviderSerializedFormatTokens> getAll();

}

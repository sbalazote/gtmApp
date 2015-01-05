package com.drogueria.service;

import java.util.List;

import com.drogueria.model.ProviderSerializedFormat;

public interface ProviderSerializedFormatService {

	void save(ProviderSerializedFormat providerSerializedFormat);

	ProviderSerializedFormat get(Integer id);

	List<ProviderSerializedFormat> getAll();

	boolean delete(Integer id);
}

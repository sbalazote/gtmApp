package com.drogueria.persistence.dao;

import java.util.List;

import com.drogueria.model.ProviderSerializedFormat;

public interface ProviderSerializedFormatDAO {

	void save(ProviderSerializedFormat providerSerializedFormat);

	ProviderSerializedFormat get(Integer id);

	List<ProviderSerializedFormat> getAll();

	boolean delete(Integer id);
}

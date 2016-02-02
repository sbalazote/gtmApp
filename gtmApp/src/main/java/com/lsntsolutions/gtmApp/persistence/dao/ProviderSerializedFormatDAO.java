package com.lsntsolutions.gtmApp.persistence.dao;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProviderSerializedFormat;

public interface ProviderSerializedFormatDAO {

	void save(ProviderSerializedFormat providerSerializedFormat);

	ProviderSerializedFormat get(Integer id);

	List<ProviderSerializedFormat> getAll();

	boolean delete(Integer id);

	boolean exists(ProviderSerializedFormat providerSerializedFormat);
}

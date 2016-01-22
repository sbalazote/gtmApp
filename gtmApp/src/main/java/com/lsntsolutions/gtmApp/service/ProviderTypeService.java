package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.ProviderType;

import java.util.List;

public interface ProviderTypeService {

	void save(ProviderType providerType);

	ProviderType get(Integer id);

	Boolean exists(Integer code);

	List<ProviderType> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortDescription, String sortIsActive);

	List<ProviderType> getAll();

	boolean delete(Integer providerTypeId);

	List<ProviderType> getPaginated(int start, int length);

	Long getTotalNumber();
}

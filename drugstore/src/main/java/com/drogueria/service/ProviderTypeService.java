package com.drogueria.service;

import java.util.List;

import com.drogueria.model.ProviderType;

public interface ProviderTypeService {

	void save(ProviderType providerType);

	ProviderType get(Integer id);

	Boolean exists(Integer code);

	List<ProviderType> getForAutocomplete(String term, Boolean active);

	List<ProviderType> getAll();

	boolean delete(Integer providerTypeId);

	List<ProviderType> getPaginated(int start, int length);

	Long getTotalNumber();
}
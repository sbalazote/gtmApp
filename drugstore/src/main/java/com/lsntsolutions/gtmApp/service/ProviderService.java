package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Provider;

public interface ProviderService {

	void save(Provider provider);

	Provider get(Integer id);

	Boolean exists(Integer code);

	List<Provider> getForAutocomplete(String term, Boolean active);

	List<Provider> getAll();

	List<Provider> getAllActives();

	boolean delete(Integer providerId);

	List<Provider> getPaginated(int start, int length);

	Long getTotalNumber();
}

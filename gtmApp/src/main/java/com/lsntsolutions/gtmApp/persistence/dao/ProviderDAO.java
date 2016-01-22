package com.lsntsolutions.gtmApp.persistence.dao;

import com.lsntsolutions.gtmApp.model.Provider;

import java.util.List;

public interface ProviderDAO {

	void save(Provider provider);

	Provider get(Integer id);

	Boolean exists(Integer code);

	List<Provider> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortName, String sortCorporateName, String sortTaxId, String sortIsActive);

	List<Provider> getAll();

	List<Provider> getAllActives();

	boolean delete(Integer providerId);

	List<Provider> getPaginated(int start, int length);

	Long getTotalNumber();
}

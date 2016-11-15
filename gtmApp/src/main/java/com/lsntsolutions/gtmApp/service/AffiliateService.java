package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Affiliate;

public interface AffiliateService {

	void save(Affiliate affiliate);

	Affiliate get(Integer id);

	Boolean exists(String code);

	Affiliate getByCode(String code);

	List<Affiliate> getForAutocomplete(String term, Boolean active, Integer cllientId, String sortId, String sortCode, String sortName, String sortSurname, String sortDocumentType, String sortDocument, String sortActive);

	List<Affiliate> getForAutocomplete(String term, Integer clientId, Boolean active, Integer pageNumber, Integer pageSize);

	List<Affiliate> getAll();

	boolean delete(Integer affiliateId);

	List<Affiliate> getAllAffiliatesByClient(Integer clientId, Boolean active);

	List<Affiliate> getPaginated(int start, int length);

	Long getTotalNumber();
}
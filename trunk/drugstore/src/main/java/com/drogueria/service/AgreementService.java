package com.drogueria.service;

import java.util.List;

import com.drogueria.model.Agreement;

public interface AgreementService {

	void save(Agreement agreement);

	Agreement get(Integer id);

	Boolean exists(Integer code);

	List<Agreement> getForAutocomplete(String term, Boolean active);

	List<Agreement> getAll();

	List<Agreement> getAllActives();

	boolean delete(Integer agreementId);

	List<Agreement> getPaginated(int start, int length);

	Long getTotalNumber();
}

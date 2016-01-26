package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.LogisticsOperator;

import java.util.List;

public interface LogisticsOperatorService {

	void save(LogisticsOperator logisticsOperator);

	LogisticsOperator get(Integer id);

	Boolean exists(Integer code);

	List<LogisticsOperator> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortName, String sortCorporateName, String sortTaxId, String sortIsActive, String sortIsInput);

	List<LogisticsOperator> getAll();

	List<LogisticsOperator> getAllActives();

	List<LogisticsOperator> getAllActives(boolean input);

	boolean delete(Integer logisticsOperatorId);

	List<LogisticsOperator> getPaginated(int start, int length);

	Long getTotalNumber();
}

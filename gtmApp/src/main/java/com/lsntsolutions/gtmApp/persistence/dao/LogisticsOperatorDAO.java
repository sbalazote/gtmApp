package com.lsntsolutions.gtmApp.persistence.dao;

import com.lsntsolutions.gtmApp.model.LogisticsOperator;

import java.util.List;

public interface LogisticsOperatorDAO {

	void save(LogisticsOperator logisticsOperator);

	LogisticsOperator get(Integer id);

	Boolean exists(Integer code);

	List<LogisticsOperator> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortName, String sortCorporateName, String sortTaxId, String sortIsActive, String sortIsInput);

	List<LogisticsOperator> getAll();

	List<LogisticsOperator> getAllActives();

	boolean delete(Integer logisticsOperatorId);

	List<LogisticsOperator> getPaginated(int start, int length);

	Long getTotalNumber();

	List<LogisticsOperator> getAllActives(boolean input);
}
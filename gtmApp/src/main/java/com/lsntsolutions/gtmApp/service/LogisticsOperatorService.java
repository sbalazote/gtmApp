package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.model.LogisticsOperator;

public interface LogisticsOperatorService {

	void save(LogisticsOperator logisticsOperator);

	LogisticsOperator get(Integer id);

	Boolean exists(Integer code);

	List<LogisticsOperator> getForAutocomplete(String term, Boolean active);

	List<LogisticsOperator> getAll();

	List<LogisticsOperator> getAllActives();

	List<LogisticsOperator> getAllActives(boolean input);

	boolean delete(Integer logisticsOperatorId);

	List<LogisticsOperator> getPaginated(int start, int length);

	Long getTotalNumber();
}

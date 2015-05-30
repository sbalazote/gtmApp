package com.drogueria.persistence.dao;

import java.util.List;

import com.drogueria.model.LogisticsOperator;

public interface LogisticsOperatorDAO {

	void save(LogisticsOperator logisticsOperator);

	LogisticsOperator get(Integer id);

	Boolean exists(Integer code);

	List<LogisticsOperator> getForAutocomplete(String term, Boolean active);

	List<LogisticsOperator> getAll();

	List<LogisticsOperator> getAllActives();

	boolean delete(Integer logisticsOperatorId);

	List<LogisticsOperator> getPaginated(int start, int length);

	Long getTotalNumber();
}
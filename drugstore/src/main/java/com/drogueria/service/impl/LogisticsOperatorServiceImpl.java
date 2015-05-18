package com.drogueria.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.model.LogisticsOperator;
import com.drogueria.persistence.dao.LogisticsOperatorDAO;
import com.drogueria.service.LogisticsOperatorService;

@Service
@Transactional
public class LogisticsOperatorServiceImpl implements LogisticsOperatorService {

	private static final Logger logger = Logger.getLogger(LogisticsOperatorServiceImpl.class);

	@Autowired
	private LogisticsOperatorDAO logisticsOperatorDAO;

	@Override
	public void save(LogisticsOperator logisticsOperator) {
		this.logisticsOperatorDAO.save(logisticsOperator);
		logger.info("Se han guardado los cambios exitosamente. Id de Operador Logistico: " + logisticsOperator.getId());
	}

	@Override
	public LogisticsOperator get(Integer id) {
		return this.logisticsOperatorDAO.get(id);
	}

	@Override
	public Boolean exists(Integer code) {
		return this.logisticsOperatorDAO.exists(code);
	}

	@Override
	public List<LogisticsOperator> getForAutocomplete(String term, Boolean active) {
		return this.logisticsOperatorDAO.getForAutocomplete(term, active);
	}

	@Override
	public List<LogisticsOperator> getAll() {
		return this.logisticsOperatorDAO.getAll();
	}

	@Override
	public List<LogisticsOperator> getAllActives() {
		return this.logisticsOperatorDAO.getAllActives();
	}

	@Override
	public boolean delete(Integer logisticsOperatorId) {
		return this.logisticsOperatorDAO.delete(logisticsOperatorId);
	}

	@Override
	public List<LogisticsOperator> getPaginated(int start, int length) {
		return this.logisticsOperatorDAO.getPaginated(start, length);
	}

	@Override
	public Long getTotalNumber() {
		return this.logisticsOperatorDAO.getTotalNumber();
	}
}
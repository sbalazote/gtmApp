package com.drogueria.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.model.Agent;
import com.drogueria.persistence.dao.AgentDAO;
import com.drogueria.service.AgentService;

@Service
@Transactional
public class AgentServiceImpl implements AgentService {

	private static final Logger logger = Logger.getLogger(AgentServiceImpl.class);

	@Autowired
	private AgentDAO agentDAO;

	@Override
	public void save(Agent agent) {
		this.agentDAO.save(agent);
		logger.info("Se han guardado los cambios exitosamente. Id de Agente: " + agent.getId());
	}

	@Override
	public Agent get(Integer id) {
		return this.agentDAO.get(id);
	}

	@Override
	public Boolean exists(Integer code) {
		return this.agentDAO.exists(code);
	}

	@Override
	public List<Agent> getForAutocomplete(String term, Boolean active) {
		return this.agentDAO.getForAutocomplete(term, active);
	}

	@Override
	public List<Agent> getAll() {
		return this.agentDAO.getAll();
	}

	@Override
	public boolean delete(Integer agentId) {
		return this.agentDAO.delete(agentId);
	}

	@Override
	public List<Agent> getPaginated(int start, int length) {
		return this.agentDAO.getPaginated(start, length);
	}

	@Override
	public Long getTotalNumber() {
		return this.agentDAO.getTotalNumber();
	}
}

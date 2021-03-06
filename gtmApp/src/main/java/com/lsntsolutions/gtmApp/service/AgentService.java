package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.Agent;

import java.util.List;

public interface AgentService {

	void save(Agent agent);

	Agent get(Integer id);

	Boolean exists(Integer code);

	List<Agent> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortDescription, String sortIsActive);

	List<Agent> getAll();

	boolean delete(Integer agentId);

	List<Agent> getPaginated(int start, int length);

	Long getTotalNumber();
}
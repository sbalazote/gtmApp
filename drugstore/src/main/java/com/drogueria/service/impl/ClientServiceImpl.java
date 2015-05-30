package com.drogueria.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.model.Client;
import com.drogueria.persistence.dao.ClientDAO;
import com.drogueria.service.ClientService;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

	private static final Logger logger = Logger.getLogger(ClientServiceImpl.class);

	@Autowired
	private ClientDAO clientDAO;

	@Override
	public void save(Client client) {
		this.clientDAO.save(client);
		logger.info("Se han guardado los cambios exitosamente. Id de Cliente: " + client.getId());
	}

	@Override
	public Client get(Integer id) {
		return this.clientDAO.get(id);
	}

	@Override
	public Boolean exists(Integer code) {
		return this.clientDAO.exists(code);
	}

	@Override
	public List<Client> getForAutocomplete(String term, Boolean active) {
		return this.clientDAO.getForAutocomplete(term, active);
	}

	@Override
	public List<Client> getAll() {
		return this.clientDAO.getAll();
	}

	@Override
	public List<Client> getAllActives() {
		return this.clientDAO.getAllActives();
	}

	@Override
	public boolean delete(Integer clientId) {
		return this.clientDAO.delete(clientId);
	}

	@Override
	public List<Client> getPaginated(int start, int length) {
		return this.clientDAO.getPaginated(start, length);
	}

	@Override
	public Long getTotalNumber() {
		return this.clientDAO.getTotalNumber();
	}
}

package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Client;

public interface ClientService {

	void save(Client client);

	Client get(Integer id);

	Boolean exists(Integer code);

	List<Client> getForAutocomplete(String term, Boolean active);

	List<Client> getAll();

	List<Client> getAllActives();

	boolean delete(Integer clientId);

	List<Client> getPaginated(int start, int length);

	Long getTotalNumber();
}
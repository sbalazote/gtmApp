package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.Client;
import com.lsntsolutions.gtmApp.model.DeliveryLocation;

import java.util.List;

public interface ClientService {

	void save(Client client);

	Client get(Integer id);

	Boolean exists(Integer code);

	List<Client> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortName, String sortTaxId, String sortProvince, String sortIsActive);

	List<Client> getAll();

	List<Client> getAllActives();

	boolean delete(Integer clientId);

	List<Client> getPaginated(int start, int length);

	Long getTotalNumber();

	List<DeliveryLocation> getDeliveriesLocations(Integer clientId);
}
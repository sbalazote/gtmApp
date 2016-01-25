package com.lsntsolutions.gtmApp.persistence.dao;

import com.lsntsolutions.gtmApp.model.Client;
import com.lsntsolutions.gtmApp.model.DeliveryLocation;

import java.util.List;

public interface ClientDAO {

	void save(Client client);

	Client get(Integer id);

	Boolean exists(Integer code);

	List<Client> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortName, String sortTaxId, String sortProvince, String sortIsActive);

	List<Client> getAll();

	List<Client> getAllActives();

	boolean delete(Integer clientId);

	List<Client> getPaginated(int start, int length);

	Long getTotalNumber();

	public List<DeliveryLocation> getDeliveriesLocations(Integer clientId);
}

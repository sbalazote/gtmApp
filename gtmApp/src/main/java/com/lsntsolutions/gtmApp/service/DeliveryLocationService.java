package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.DeliveryLocation;

import java.util.List;

public interface DeliveryLocationService {

	void save(DeliveryLocation deliveryLocation);

	DeliveryLocation get(Integer id);

	Boolean exists(Integer code);

	List<DeliveryLocation> getForAutocomplete(String searchPhrase, Boolean active, String sortId, String sortCode, String sortName, String sortLocality, String sortAddress, String sortIsActive);

	List<DeliveryLocation> getAll();

	List<DeliveryLocation> getAllActives();

	boolean delete(Integer deliveryLocationId);

	List<DeliveryLocation> getPaginated(int start, int length);

	Long getTotalNumber();
}
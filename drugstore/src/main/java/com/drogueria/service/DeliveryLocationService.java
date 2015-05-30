package com.drogueria.service;

import java.util.List;

import com.drogueria.model.DeliveryLocation;

public interface DeliveryLocationService {

	void save(DeliveryLocation deliveryLocation);

	DeliveryLocation get(Integer id);

	Boolean exists(Integer code);

	List<DeliveryLocation> getForAutocomplete(String term, Boolean active);

	List<DeliveryLocation> getAll();

	List<DeliveryLocation> getAllActives();

	boolean delete(Integer deliveryLocationId);

	List<DeliveryLocation> getPaginated(int start, int length);

	Long getTotalNumber();
}
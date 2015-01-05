package com.drogueria.persistence.dao;

import java.util.List;

import com.drogueria.model.DeliveryLocation;

public interface DeliveryLocationDAO {

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

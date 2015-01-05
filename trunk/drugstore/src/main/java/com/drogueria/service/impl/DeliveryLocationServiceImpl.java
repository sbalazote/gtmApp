package com.drogueria.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.model.DeliveryLocation;
import com.drogueria.persistence.dao.DeliveryLocationDAO;
import com.drogueria.service.DeliveryLocationService;

@Service
@Transactional
public class DeliveryLocationServiceImpl implements DeliveryLocationService {

	private static final Logger logger = Logger.getLogger(DeliveryLocationServiceImpl.class);

	@Autowired
	private DeliveryLocationDAO deliveryLocationDAO;

	@Override
	public void save(DeliveryLocation deliveryLocation) {
		this.deliveryLocationDAO.save(deliveryLocation);
		logger.info("Se han guardado los cambios exitosamente. Id de Lugar de Entrega: " + deliveryLocation.getId());
	}

	@Override
	public DeliveryLocation get(Integer id) {
		return this.deliveryLocationDAO.get(id);
	}

	@Override
	public Boolean exists(Integer code) {
		return this.deliveryLocationDAO.exists(code);
	}

	@Override
	public List<DeliveryLocation> getForAutocomplete(String term, Boolean active) {
		return this.deliveryLocationDAO.getForAutocomplete(term, active);
	}

	@Override
	public List<DeliveryLocation> getAll() {
		return this.deliveryLocationDAO.getAll();
	}

	@Override
	public List<DeliveryLocation> getAllActives() {
		return this.deliveryLocationDAO.getAllActives();
	}

	@Override
	public boolean delete(Integer deliveryLocationId) {
		return this.deliveryLocationDAO.delete(deliveryLocationId);
	}

	@Override
	public List<DeliveryLocation> getPaginated(int start, int length) {
		return this.deliveryLocationDAO.getPaginated(start, length);
	}

	@Override
	public Long getTotalNumber() {
		return this.deliveryLocationDAO.getTotalNumber();
	}
}
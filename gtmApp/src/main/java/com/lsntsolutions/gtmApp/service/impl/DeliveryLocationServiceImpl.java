package com.lsntsolutions.gtmApp.service.impl;

import com.lsntsolutions.gtmApp.model.DeliveryLocation;
import com.lsntsolutions.gtmApp.persistence.dao.DeliveryLocationDAO;
import com.lsntsolutions.gtmApp.service.DeliveryLocationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
	public List<DeliveryLocation> getForAutocomplete(String searchPhrase, Boolean active, String sortId, String sortCode, String sortName, String sortLocality, String sortAddress, String sortIsActive) {
		return this.deliveryLocationDAO.getForAutocomplete(searchPhrase, active, sortId, sortCode, sortName, sortLocality, sortAddress, sortIsActive);
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
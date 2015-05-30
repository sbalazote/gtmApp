package com.drogueria.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.model.Property;
import com.drogueria.persistence.dao.PropertyDAO;
import com.drogueria.service.PropertyService;

@Service
@Transactional
public class DrugstoreProperyServiceImpl implements PropertyService {

	@Autowired
	private PropertyDAO PropertyDAO;

	@Override
	public void save(Property Property) {
		this.PropertyDAO.save(Property);

	}

	@Override
	public Property get() {
		return this.PropertyDAO.get();
	}

	@Override
	public Property getAndUpdateSelfSerializedTag(Integer amount) {
		Property Property = this.PropertyDAO.getForUpdate();
		Integer lastAmount = Property.getLastTag();
		Integer newLastAmount = lastAmount + amount;
		Property.setLastTag(newLastAmount);
		this.PropertyDAO.save(Property);
		return Property;
	}

}

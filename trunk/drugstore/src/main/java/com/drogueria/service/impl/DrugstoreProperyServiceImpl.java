package com.drogueria.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.model.DrugstoreProperty;
import com.drogueria.persistence.dao.DrugstorePropertyDAO;
import com.drogueria.service.DrugstorePropertyService;

@Service
@Transactional
public class DrugstoreProperyServiceImpl implements DrugstorePropertyService {

	@Autowired
	private DrugstorePropertyDAO drugstorePropertyDAO;

	@Override
	public void save(DrugstoreProperty drugstoreProperty) {
		this.drugstorePropertyDAO.save(drugstoreProperty);

	}

	@Override
	public DrugstoreProperty get() {
		return this.drugstorePropertyDAO.get();
	}

	@Override
	public DrugstoreProperty getAndUpdateSelfSerializedTag(Integer amount) {
		DrugstoreProperty drugstoreProperty = this.drugstorePropertyDAO.getForUpdate();
		Integer lastAmount = drugstoreProperty.getLastTag();
		Integer newLastAmount = lastAmount + amount;
		drugstoreProperty.setLastTag(newLastAmount);
		this.drugstorePropertyDAO.save(drugstoreProperty);
		return drugstoreProperty;
	}

}

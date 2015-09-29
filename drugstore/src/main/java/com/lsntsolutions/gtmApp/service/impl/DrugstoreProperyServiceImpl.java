package com.lsntsolutions.gtmApp.service.impl;

import com.lsntsolutions.gtmApp.service.PropertyService;
import com.lsntsolutions.gtmApp.model.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lsntsolutions.gtmApp.persistence.dao.PropertyDAO;

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

	public boolean isConceptInUse(Integer conceptId){
		Property property = this.get();
		boolean isUseAsStartTraceConcept = false;
		boolean isUseAsSupplyingConcept= false;
		if(property.getStartTraceConcept() != null){
			isUseAsStartTraceConcept = (conceptId == property.getStartTraceConcept().getId());
		}
		if(property.getSupplyingConcept() != null){
			isUseAsSupplyingConcept = (conceptId == property.getSupplyingConcept().getId());
		}
		return (isUseAsStartTraceConcept || isUseAsSupplyingConcept);
	}

}

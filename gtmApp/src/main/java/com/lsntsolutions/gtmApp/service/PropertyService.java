package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.Property;

public interface PropertyService {

	void save(Property Property);

	Property get();

	Property getAndUpdateSelfSerializedTag(Integer amount);

	boolean isConceptInUse(Integer conceptId);
}

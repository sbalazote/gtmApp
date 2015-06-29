package com.drogueria.service;

import com.drogueria.model.Property;

public interface PropertyService {

	void save(Property Property);

	Property get();

	Property getAndUpdateSelfSerializedTag(Integer amount);

	boolean isConceptInUse(Integer conceptId);
}

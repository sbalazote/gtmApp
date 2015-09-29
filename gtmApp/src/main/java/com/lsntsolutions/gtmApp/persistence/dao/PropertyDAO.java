package com.lsntsolutions.gtmApp.persistence.dao;

import com.lsntsolutions.gtmApp.model.Property;

public interface PropertyDAO {

	void save(Property Property);

	Property get();

	Property getForUpdate();

}

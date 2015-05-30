package com.drogueria.persistence.dao;

import com.drogueria.model.Property;

public interface PropertyDAO {

	void save(Property Property);

	Property get();

	Property getForUpdate();

}

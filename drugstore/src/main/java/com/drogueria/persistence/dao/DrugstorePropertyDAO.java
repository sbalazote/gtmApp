package com.drogueria.persistence.dao;

import com.drogueria.model.DrugstoreProperty;

public interface DrugstorePropertyDAO {

	void save(DrugstoreProperty drugstoreProperty);

	DrugstoreProperty get();

	DrugstoreProperty getForUpdate();

}

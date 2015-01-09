package com.drogueria.service;

import com.drogueria.model.DrugstoreProperty;

public interface DrugstorePropertyService {

	void save(DrugstoreProperty drugstoreProperty);

	DrugstoreProperty get();

	DrugstoreProperty getAndUpdateSelfSerializedTag(Integer amount);

}

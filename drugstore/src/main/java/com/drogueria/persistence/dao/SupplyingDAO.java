package com.drogueria.persistence.dao;

import java.util.List;

import com.drogueria.model.Output;
import com.drogueria.model.Supplying;
import com.drogueria.query.OutputQuery;
import com.drogueria.query.SupplyingQuery;

public interface SupplyingDAO {

	void save(Supplying supplying);

	Supplying get(Integer id);

	List<Supplying> getAll();

	List<Supplying> getCancelleables();

	// List<Output> getPendings();

    List<Supplying> getSupplyingForSearch(SupplyingQuery supplyingQuery);

    boolean getCountSupplyingSearch(SupplyingQuery supplyingQuery);
}

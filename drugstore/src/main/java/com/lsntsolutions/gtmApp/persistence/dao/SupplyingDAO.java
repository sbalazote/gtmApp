package com.lsntsolutions.gtmApp.persistence.dao;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Supplying;
import com.lsntsolutions.gtmApp.query.SupplyingQuery;

public interface SupplyingDAO {

	void save(Supplying supplying);

	Supplying get(Integer id);

	List<Supplying> getAll();

	List<Supplying> getCancelleables();

	// List<Output> getPendings();

    List<Supplying> getSupplyingForSearch(SupplyingQuery supplyingQuery);

    boolean getCountSupplyingSearch(SupplyingQuery supplyingQuery);
}

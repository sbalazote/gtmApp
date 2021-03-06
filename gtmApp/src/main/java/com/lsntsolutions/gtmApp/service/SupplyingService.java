package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.dto.SupplyingDTO;
import com.lsntsolutions.gtmApp.model.Supplying;
import com.lsntsolutions.gtmApp.query.SupplyingQuery;

public interface SupplyingService {

	Supplying save(SupplyingDTO supplyingDTO);

	Supplying get(Integer id);

	List<Supplying> getAll();

	void save(Supplying supplying);

	void addSupplyingToStock(Supplying supplying);

	void cancel(Supplying supplying);

    List<Supplying> getSupplyingForSearch(SupplyingQuery supplyingQuery);

    boolean getCountSupplyingSearch(SupplyingQuery supplyingQuery);
}
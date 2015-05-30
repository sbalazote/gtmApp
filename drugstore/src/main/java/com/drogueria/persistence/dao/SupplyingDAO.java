package com.drogueria.persistence.dao;

import java.util.List;

import com.drogueria.model.Supplying;

public interface SupplyingDAO {

	void save(Supplying supplying);

	Supplying get(Integer id);

	List<Supplying> getAll();

	List<Supplying> getCancelleables();

	// List<Output> getPendings();
}

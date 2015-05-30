package com.drogueria.service;

import java.util.List;

import com.drogueria.dto.SupplyingDTO;
import com.drogueria.model.Supplying;

public interface SupplyingService {

	Supplying save(SupplyingDTO supplyingDTO);

	Supplying get(Integer id);

	List<Supplying> getAll();

	List<Supplying> getCancelleables();

	void save(Supplying supplying);

	void addSupplyingToStock(Supplying supplying);

	void cancel(Supplying supplying);
}
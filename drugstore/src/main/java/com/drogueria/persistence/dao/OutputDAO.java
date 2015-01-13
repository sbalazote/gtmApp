package com.drogueria.persistence.dao;

import java.util.List;

import com.drogueria.model.Output;
import com.drogueria.query.OutputQuery;

public interface OutputDAO {

	void save(Output output);

	Output get(Integer id);

	List<Output> getAll();

	List<Output> getOutputForSearch(OutputQuery outputQuery);

	boolean getCountOutputSearch(OutputQuery outputQuery);

	boolean existSerial(Integer productId, String serial);

	List<Output> getCancelleables();

	public List<Integer> getAllHasToPrint();

	// List<Output> getPendings();
}

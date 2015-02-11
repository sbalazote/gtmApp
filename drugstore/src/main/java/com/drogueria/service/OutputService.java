package com.drogueria.service;

import java.util.List;

import com.drogueria.dto.OutputDTO;
import com.drogueria.model.Output;
import com.drogueria.query.OutputQuery;

public interface OutputService {

	Output save(OutputDTO outputDTO);

	Output get(Integer id);

	List<Output> getAll();

	List<Output> getOutputForSearch(OutputQuery outputQuery);

	boolean getCountOutputSearch(OutputQuery outputQuery);

	boolean existSerial(Integer productId, String serial);

	List<Output> getCancelleables();

	public List<Integer> getAllHasToPrint();

	void addOutputToStock(Output output);

	void save(Output output);

	void cancel(Output output);
}

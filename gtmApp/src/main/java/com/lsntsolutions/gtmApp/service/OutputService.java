package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.dto.OutputDTO;
import com.lsntsolutions.gtmApp.model.Output;
import com.lsntsolutions.gtmApp.query.OutputQuery;

public interface OutputService {

	Output save(OutputDTO outputDTO);

	Output get(Integer id);

	List<Output> getAll();

	List<Output> getOutputForSearch(OutputQuery outputQuery);

	boolean getCountOutputSearch(OutputQuery outputQuery);

	void addOutputToStock(Output output);

	void save(Output output);

	void cancel(Output output);

	boolean isConceptInUse(Integer conceptId);
}

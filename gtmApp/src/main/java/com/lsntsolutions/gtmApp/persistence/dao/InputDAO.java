package com.lsntsolutions.gtmApp.persistence.dao;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Input;
import com.lsntsolutions.gtmApp.model.InputDetail;
import com.lsntsolutions.gtmApp.model.Stock;
import com.lsntsolutions.gtmApp.query.InputQuery;

public interface InputDAO {

	void save(Input input);

	Input get(Integer id);

	List<Input> getAll();

	Boolean existsSerial(String serialNumber, Integer productId, String gtin);

	List<Input> getInputForSearch(InputQuery inputQuery);

	boolean getCountInputSearch(InputQuery inputQuery);

	List<Input> getInputToAuthorize();

    List<Input> getForcedInputs();

	boolean exitsMovements(Input input);

	List<Input> getInputs(boolean cancelled);

	List<Input> getCancelables(InputQuery inputQuery);

	boolean isConceptInUse(Integer conceptId);

	InputDetail getSelfSerializedProductBySerial(String serialNumber);
}
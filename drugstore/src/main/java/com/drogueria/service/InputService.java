package com.drogueria.service;

import java.util.List;

import com.drogueria.dto.InputDTO;
import com.drogueria.model.Input;
import com.drogueria.query.InputQuery;
import com.drogueria.util.OperationResult;

public interface InputService {

	Input save(InputDTO inputDTO, Boolean isSerializedReturn, String username) throws Exception;

	Input get(Integer id);

	List<Input> getAll();

	Boolean existsSerial(String serialNumber, Integer productId, String gtin);

	List<Input> getInputForSearch(InputQuery inputQuery);

	boolean getCountInputSearch(InputQuery inputQuery);

	List<Input> getInputToAuthorize();

    List<Input> getForcedInputs();

	void saveAndUpdateStock(Input input);

	void save(Input input);

	Input update(InputDTO inputDTO);

	void saveAndRemoveFromStock(Input input);

	OperationResult updateInput(InputDTO inputDTO, String username) throws Exception;

    OperationResult updateForcedInput(Input input, String username) throws Exception;

	boolean cancelInput(Integer inputId);

	List<Input> getCancelables(InputQuery inputQuery);

	public void sendAsyncTransaction(Input input) throws Exception;

	Input authorizeWithoutInform(InputDTO inputDTO, String name);
}

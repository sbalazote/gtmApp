package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.dto.InputDTO;
import com.lsntsolutions.gtmApp.model.Input;
import com.lsntsolutions.gtmApp.model.InputDetail;
import com.lsntsolutions.gtmApp.query.InputQuery;
import com.lsntsolutions.gtmApp.util.OperationResult;

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

    OperationResult sendTransaction(Input input, boolean addStock) throws Exception;

	boolean cancelInput(Integer inputId);

	List<Input> getCancelables(InputQuery inputQuery);

	void sendAsyncTransaction(Input input) throws Exception;

	Input authorizeWithoutInform(InputDTO inputDTO, String name);

	boolean isConceptInUse(Integer conceptId);

	Input importStock(List<InputDetail> inputDetails, Integer agreementId, Integer conceptId, Integer providerId, String userName);
}

package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.dto.ProvisioningRequestDTO;
import com.lsntsolutions.gtmApp.model.ProvisioningRequest;
import com.lsntsolutions.gtmApp.query.ProvisioningQuery;
import com.lsntsolutions.gtmApp.util.OperationResult;

public interface ProvisioningRequestService {

	void save(ProvisioningRequest provisioningRequest);

	OperationResult save(ProvisioningRequestDTO provisioningRequestDTO);

	ProvisioningRequest get(Integer id);

	List<ProvisioningRequest> getAll();

	List<ProvisioningRequest> getAllByState(Integer stateId);

	List<ProvisioningRequest> getProvisioningForSearch(ProvisioningQuery provisioningQuery);

	boolean getCountOfProvisioningSearch(ProvisioningQuery provisioningQuery);

	void authorizeProvisioningRequests(List<Integer> provisioningIds);

	boolean cancelProvisioningRequest(Integer provisioningRequestId);

	List<ProvisioningRequest> getFilterProvisionings(Integer agreementId, Integer clientId, Integer stateId);

	void reassignOperators(ProvisioningRequest provisioningRequest, Integer operatorLogisticId);

}

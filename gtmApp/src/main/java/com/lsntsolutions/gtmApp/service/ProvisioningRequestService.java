package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.dto.ProvisioningRequestDTO;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.query.ProvisioningQuery;
import com.lsntsolutions.gtmApp.util.OperationResult;

import java.util.List;

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

	List<ProvisioningRequest> getFilterProvisionings(Integer provisioningRequestId, Integer agreementId, Integer logisticsOperatorId, Integer clientId, Integer deliveryLocationId, Integer stateId);

	void reassignOperators(List<Integer> provisioningsIdsToReassign, Integer operatorLogisticId);

	List<Agreement> getProvisioningsAgreement(boolean provisioningRequireAuthorization);

	List<DeliveryLocation> getProvisioningsDeliveryLocations(boolean provisioningRequireAuthorization);

	List<LogisticsOperator> getProvisioningsLogisticsOperators(boolean provisioningRequireAuthorization);

	List<Client> getProvisioningsClient(boolean provisioningRequireAuthorization);
}

package com.lsntsolutions.gtmApp.persistence.dao;

import com.lsntsolutions.gtmApp.model.ProvisioningRequest;
import com.lsntsolutions.gtmApp.query.ProvisioningQuery;

import java.util.List;

public interface ProvisioningRequestDAO {

	void save(ProvisioningRequest provisioningRequest);

	ProvisioningRequest get(Integer id);

	List<ProvisioningRequest> getAll();

	List<ProvisioningRequest> getAllByState(Integer stateId);

	List<ProvisioningRequest> getProvisioningForSearch(ProvisioningQuery provisioningQuery);

	boolean getCountOfProvisioningSearch(ProvisioningQuery provisioningQuery);

	List<ProvisioningRequest> getFilterProvisionings(Integer provisioningRequestId, Integer agreementId, Integer logisticsOperatorId, Integer clientId, Integer deliveryLocationId, Integer stateId);
}
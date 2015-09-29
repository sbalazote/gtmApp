package com.lsntsolutions.gtmApp.persistence.dao;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProvisioningRequest;
import com.lsntsolutions.gtmApp.query.ProvisioningQuery;

public interface ProvisioningRequestDAO {

	void save(ProvisioningRequest provisioningRequest);

	ProvisioningRequest get(Integer id);

	List<ProvisioningRequest> getAll();

	List<ProvisioningRequest> getAllByState(Integer stateId);

	List<ProvisioningRequest> getProvisioningForSearch(ProvisioningQuery provisioningQuery);

	boolean getCountOfProvisioningSearch(ProvisioningQuery provisioningQuery);

	List<ProvisioningRequest> getFilterProvisionings(Integer agreementId, Integer clientId, Integer stateId);
}

package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProvisioningRequestState;

public interface ProvisioningRequestStateService {

	void save(ProvisioningRequestState provisioningRequestState);

	ProvisioningRequestState get(Integer id);

	List<ProvisioningRequestState> getAll();
}

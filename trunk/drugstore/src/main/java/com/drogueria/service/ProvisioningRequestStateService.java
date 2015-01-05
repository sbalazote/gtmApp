package com.drogueria.service;

import java.util.List;

import com.drogueria.model.ProvisioningRequestState;

public interface ProvisioningRequestStateService {

	void save(ProvisioningRequestState provisioningRequestState);

	ProvisioningRequestState get(Integer id);

	List<ProvisioningRequestState> getAll();
}

package com.drogueria.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.model.ProvisioningRequestState;
import com.drogueria.persistence.dao.GenericDAO;
import com.drogueria.service.ProvisioningRequestStateService;

@Service
@Transactional
public class ProvisioningRequestStateServiceImpl implements ProvisioningRequestStateService {

	@Autowired
	private GenericDAO<ProvisioningRequestState> genericDAO;

	@Override
	public void save(ProvisioningRequestState provisioningRequestState) {
		this.genericDAO.save(provisioningRequestState);
	}

	@Override
	public ProvisioningRequestState get(Integer id) {
		return this.genericDAO.get(ProvisioningRequestState.class, id);
	}

	@Override
	public List<ProvisioningRequestState> getAll() {
		return this.genericDAO.getAll(ProvisioningRequestState.class);
	}

}
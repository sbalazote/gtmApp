package com.lsntsolutions.gtmApp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lsntsolutions.gtmApp.model.VATLiability;
import com.lsntsolutions.gtmApp.persistence.dao.GenericDAO;
import com.lsntsolutions.gtmApp.service.VATLiabilityService;

@Service
@Transactional
public class VATLiabilityServiceImpl implements VATLiabilityService {

	@Autowired
	private GenericDAO<VATLiability> genericDAO;

	@Override
	public void save(VATLiability VATLiability) {
		this.genericDAO.save(VATLiability);
	}

	@Override
	public VATLiability get(Integer id) {
		return this.genericDAO.get(VATLiability.class, id);
	}

	@Override
	public List<VATLiability> getAll() {
		return this.genericDAO.getAll(VATLiability.class);
	}

}

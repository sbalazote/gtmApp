package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.model.VATLiability;

public interface VATLiabilityService {

	void save(VATLiability VATLiability);

	VATLiability get(Integer id);

	List<VATLiability> getAll();
}
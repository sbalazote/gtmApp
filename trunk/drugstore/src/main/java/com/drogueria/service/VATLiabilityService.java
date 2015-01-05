package com.drogueria.service;

import java.util.List;

import com.drogueria.model.VATLiability;

public interface VATLiabilityService {

	void save(VATLiability VATLiability);

	VATLiability get(Integer id);

	List<VATLiability> getAll();
}
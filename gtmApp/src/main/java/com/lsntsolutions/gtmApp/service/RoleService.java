package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Role;

public interface RoleService {

	void save(Role role);

	Role get(Integer id);

	List<Role> getAll();
}

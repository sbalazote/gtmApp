package com.lsntsolutions.gtmApp.persistence.dao;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Role;

public interface RoleDAO {

	void save(Role role);

	Role get(Integer id);

	List<Role> getAll();
}

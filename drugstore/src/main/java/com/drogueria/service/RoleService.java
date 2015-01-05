package com.drogueria.service;

import java.util.List;

import com.drogueria.model.Role;

public interface RoleService {

	void save(Role role);

	Role get(Integer id);

	List<Role> getAll();
}

package com.drogueria.persistence.dao;

import java.util.List;

import com.drogueria.model.Role;

public interface RoleDAO {

	void save(Role role);

	Role get(Integer id);

	List<Role> getAll();
}

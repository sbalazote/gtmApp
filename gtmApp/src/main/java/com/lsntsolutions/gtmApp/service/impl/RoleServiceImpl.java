package com.lsntsolutions.gtmApp.service.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Role;
import com.lsntsolutions.gtmApp.persistence.dao.RoleDAO;
import com.lsntsolutions.gtmApp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDAO roleDAO;

	@Override
	public void save(Role role) {
		this.roleDAO.save(role);
	}

	@Override
	public Role get(Integer id) {
		return this.roleDAO.get(id);
	}

	@Override
	public List<Role> getAll() {
		return this.roleDAO.getAll();
	}

}

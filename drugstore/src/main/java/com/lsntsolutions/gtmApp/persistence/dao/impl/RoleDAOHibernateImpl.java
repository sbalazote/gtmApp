package com.lsntsolutions.gtmApp.persistence.dao.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Role;
import com.lsntsolutions.gtmApp.persistence.dao.RoleDAO;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDAOHibernateImpl implements RoleDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Role role) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(role);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(Role.class).list();
	}

	@Override
	public Role get(Integer id) {
		return (Role) this.sessionFactory.getCurrentSession().get(Role.class, id);
	}
}

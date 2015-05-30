package com.drogueria.persistence.dao.impl;

import org.hibernate.LockOptions;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.Property;
import com.drogueria.persistence.dao.PropertyDAO;

@Repository
public class PropertyDAOHibernateImpl implements PropertyDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Property Property) {
		this.sessionFactory.getCurrentSession().merge(Property);
	}

	@Override
	public Property get() {
		return (Property) this.sessionFactory.getCurrentSession().get(Property.class, 1);
	}

	@Override
	public Property getForUpdate() {
		return (Property) this.sessionFactory.getCurrentSession().get(Property.class, 1, LockOptions.UPGRADE);
	}

}

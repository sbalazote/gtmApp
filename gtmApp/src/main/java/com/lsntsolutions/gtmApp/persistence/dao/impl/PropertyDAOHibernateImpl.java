package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.Property;
import com.lsntsolutions.gtmApp.persistence.dao.PropertyDAO;
import org.hibernate.LockOptions;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

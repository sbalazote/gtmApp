package com.drogueria.persistence.dao.impl;

import org.hibernate.LockOptions;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.DrugstoreProperty;
import com.drogueria.persistence.dao.DrugstorePropertyDAO;

@Repository
public class DrugstorePropertyDAOHibernateImpl implements DrugstorePropertyDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(DrugstoreProperty drugstoreProperty) {
		this.sessionFactory.getCurrentSession().merge(drugstoreProperty);
	}

	@Override
	public DrugstoreProperty get() {
		return (DrugstoreProperty) this.sessionFactory.getCurrentSession().get(DrugstoreProperty.class, 1);
	}

	@Override
	public DrugstoreProperty getForUpdate() {
		return (DrugstoreProperty) this.sessionFactory.getCurrentSession().get(DrugstoreProperty.class, 1, LockOptions.UPGRADE);
	}

}

package com.drogueria.persistence.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.persistence.dao.GenericDAO;

@Repository
@Transactional
public class GenericDAOHibernateImpl<T extends Serializable> implements GenericDAO<T> {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(T entity) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(Class<T> type, Integer id) {
		return (T) this.sessionFactory.getCurrentSession().get(type, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAll(Class<T> type) {
		return this.sessionFactory.getCurrentSession().createCriteria(type).list();
	}

}

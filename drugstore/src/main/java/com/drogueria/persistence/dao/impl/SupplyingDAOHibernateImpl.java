package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.Supplying;
import com.drogueria.persistence.dao.SupplyingDAO;

@Repository
public class SupplyingDAOHibernateImpl implements SupplyingDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Supplying supplying) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(supplying);
	}

	@Override
	public Supplying get(Integer id) {
		return (Supplying) this.sessionFactory.getCurrentSession().get(Supplying.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Supplying> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(Supplying.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Supplying> getCancelleables() {
		Query query;

		query = this.sessionFactory.getCurrentSession()
				.createSQLQuery("select * from supplying as o where not exists (select * from supplying_detail as od and o.cancelled = 0")
						.addEntity(Supplying.class);
		return query.list();
	}
}

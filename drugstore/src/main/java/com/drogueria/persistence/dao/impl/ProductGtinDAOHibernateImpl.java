package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.ProductGtin;
import com.drogueria.persistence.dao.ProductGtinDAO;

@Repository
public class ProductGtinDAOHibernateImpl implements ProductGtinDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(ProductGtin productGtin) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(productGtin);
	}

	@Override
	public ProductGtin get(Integer id) {
		return (ProductGtin) this.sessionFactory.getCurrentSession().get(ProductGtin.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductGtin> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(ProductGtin.class).list();
	}

	@Override
	public ProductGtin getByNumber(String number) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from ProductGtin where number = :number");
		query.setParameter("number", number);
		if (query.list().isEmpty()) {
			return null;
		} else {
			return (ProductGtin) query.list().get(0);
		}
	}

	@Override
	public boolean isGtinUsed(String number) {
		// TODO
		return false;
	}
}

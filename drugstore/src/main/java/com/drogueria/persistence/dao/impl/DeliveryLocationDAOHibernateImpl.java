package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.DeliveryLocation;
import com.drogueria.persistence.dao.DeliveryLocationDAO;
import com.drogueria.util.StringUtility;

@Repository
public class DeliveryLocationDAOHibernateImpl implements DeliveryLocationDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(DeliveryLocation deliveryLocation) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(deliveryLocation);
	}

	@Override
	public DeliveryLocation get(Integer id) {
		return (DeliveryLocation) this.sessionFactory.getCurrentSession().get(DeliveryLocation.class, id);
	}

	@Override
	public Boolean exists(Integer code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from DeliveryLocation where code = :code");
		query.setParameter("code", code);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryLocation> getForAutocomplete(String term, Boolean active) {
		String sentence = "from DeliveryLocation where (taxId like :taxId or corporateName like :corporateName or locality like :locality";
		if (StringUtility.isInteger(term)) {
			sentence += " or convert(code, CHAR) like :code";
		}
		sentence += ")";
		if (active != null && Boolean.TRUE.equals(active)) {
			sentence += " and active = true";
		}

		Query query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("taxId", "%" + term + "%");
		query.setParameter("corporateName", "%" + term + "%");
		query.setParameter("locality", "%" + term + "%");

		if (StringUtility.isInteger(term)) {
			query.setParameter("code", "%" + term + "%");
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryLocation> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(DeliveryLocation.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryLocation> getAllActives() {
		return this.sessionFactory.getCurrentSession().createQuery("from DeliveryLocation where active = true").list();
	}

	@Override
	public boolean delete(Integer deliveryLocationId) {
		DeliveryLocation deliveryLocation = this.get(deliveryLocationId);
		if (deliveryLocation != null) {
			this.sessionFactory.getCurrentSession().delete(deliveryLocation);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryLocation> getPaginated(int start, int length) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(DeliveryLocation.class);
		criteria.setMaxResults(length);
		criteria.setFirstResult(start);
		return criteria.list();
	}

	@Override
	public Long getTotalNumber() {
		Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from DeliveryLocation").uniqueResult();
		return count;
	}
}

package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.DeliveryLocation;
import com.lsntsolutions.gtmApp.persistence.dao.DeliveryLocationDAO;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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
	public List<DeliveryLocation> getForAutocomplete(String searchPhrase, Boolean active, String sortId, String sortCode, String sortName, String sortLocality, String sortAddress, String sortIsActive) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(DeliveryLocation.class);

		if (StringUtility.isInteger(searchPhrase)) {
			criteria.add(Restrictions.or(Restrictions.eq("id", Integer.parseInt(searchPhrase)), Restrictions.eq("code", Integer.parseInt(searchPhrase))));
		} else {
			criteria.add(Restrictions.or(Restrictions.ilike("name", searchPhrase, MatchMode.ANYWHERE), Restrictions.ilike("locality", searchPhrase, MatchMode.ANYWHERE)));
		}

		if (active != null && Boolean.TRUE.equals(active)) {
			criteria.add(Restrictions.eq("active", "true"));
		}

		if (sortId != null) {
			if (sortId.equals("asc")) {
				criteria.addOrder(Order.asc("id"));
			} else {
				criteria.addOrder(Order.desc("id"));
			}
		} else if (sortCode != null) {
			if (sortCode.equals("asc")) {
				criteria.addOrder(Order.asc("code"));
			} else {
				criteria.addOrder(Order.desc("code"));
			}
		} else if (sortName != null) {
			if (sortName.equals("asc")) {
				criteria.addOrder(Order.asc("name"));
			} else {
				criteria.addOrder(Order.desc("name"));
			}
		} else if (sortLocality != null) {
			if (sortLocality.equals("asc")) {
				criteria.addOrder(Order.asc("locality"));
			} else {
				criteria.addOrder(Order.desc("locality"));
			}
		} else if (sortAddress != null) {
			if (sortAddress.equals("asc")) {
				criteria.addOrder(Order.asc("address"));
			} else {
				criteria.addOrder(Order.desc("address"));
			}
		} else if (sortIsActive != null) {
			if (sortIsActive.equals("asc")) {
				criteria.addOrder(Order.asc("active"));
			} else {
				criteria.addOrder(Order.desc("active"));
			}
		}else {
			criteria.addOrder(Order.asc("id"));
		}

		return (List<DeliveryLocation>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryLocation> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(DeliveryLocation.class).addOrder(Order.asc("name")).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryLocation> getAllActives() {
		return this.sessionFactory.getCurrentSession().createCriteria(DeliveryLocation.class).add(Restrictions.eq("active", true)).addOrder(Order.asc("name")).list();
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

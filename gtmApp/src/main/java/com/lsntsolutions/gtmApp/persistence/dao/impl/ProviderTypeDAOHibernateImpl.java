package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.ProviderType;
import com.lsntsolutions.gtmApp.persistence.dao.ProviderTypeDAO;
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
public class ProviderTypeDAOHibernateImpl implements ProviderTypeDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(ProviderType providerType) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(providerType);
	}

	@Override
	public ProviderType get(Integer id) {
		return (ProviderType) this.sessionFactory.getCurrentSession().get(ProviderType.class, id);
	}

	@Override
	public Boolean exists(Integer code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from ProviderType where code = :code");
		query.setParameter("code", code);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProviderType> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortDescription, String sortIsActive) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(ProviderType.class);

		if (StringUtility.isInteger(term)) {
			criteria.add(Restrictions.or(Restrictions.eq("id", Integer.parseInt(term)), Restrictions.eq("code",  Integer.parseInt(term))));
		} else {
			criteria.add(Restrictions.ilike("description", term, MatchMode.ANYWHERE));
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
		} else if (sortDescription != null) {
			if (sortDescription.equals("asc")) {
				criteria.addOrder(Order.asc("description"));
			} else {
				criteria.addOrder(Order.desc("description"));
			}
		} else if (sortIsActive != null) {
			if (sortIsActive.equals("asc")) {
				criteria.addOrder(Order.asc("active"));
			} else {
				criteria.addOrder(Order.desc("active"));
			}
		} else {
			criteria.addOrder(Order.asc("id"));
		}

		return (List<ProviderType>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProviderType> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(ProviderType.class).list();
	}

	@Override
	public boolean delete(Integer providerTypeId) {
		ProviderType providerType = this.get(providerTypeId);
		if (providerType != null) {
			this.sessionFactory.getCurrentSession().delete(providerType);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProviderType> getPaginated(int start, int length) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(ProviderType.class);
		criteria.setMaxResults(length);
		criteria.setFirstResult(start);
		return criteria.list();
	}

	@Override
	public Long getTotalNumber() {
		Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from ProviderType").uniqueResult();
		return count;
	}
}

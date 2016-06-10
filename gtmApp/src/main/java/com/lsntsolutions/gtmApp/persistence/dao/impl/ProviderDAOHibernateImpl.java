package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.Provider;
import com.lsntsolutions.gtmApp.persistence.dao.ProviderDAO;
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
public class ProviderDAOHibernateImpl implements ProviderDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Provider provider) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(provider);
	}

	@Override
	public Provider get(Integer id) {
		return (Provider) this.sessionFactory.getCurrentSession().get(Provider.class, id);
	}

	@Override
	public Boolean exists(Integer code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Provider where code = :code");
		query.setParameter("code", code);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Provider> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortName, String sortCorporateName, String sortTaxId, String sortIsActive) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Provider.class);

		if (StringUtility.isInteger(term)) {
			criteria.add(Restrictions.or(Restrictions.eq("id", Integer.parseInt(term)), Restrictions.eq("code",  Integer.parseInt(term))));
		} else {
			criteria.add(Restrictions.or(Restrictions.ilike("name", term, MatchMode.ANYWHERE), Restrictions.ilike("corporateName", term, MatchMode.ANYWHERE), Restrictions.ilike("taxId", term, MatchMode.ANYWHERE)));
		}

		if (active != null && Boolean.TRUE.equals(active)) {
			criteria.add(Restrictions.eq("active", true));
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
		} else if (sortCorporateName != null) {
			if (sortCorporateName.equals("asc")) {
				criteria.addOrder(Order.asc("corporateName"));
			} else {
				criteria.addOrder(Order.desc("corporateName"));
			}
		} else if (sortTaxId != null) {
			if (sortTaxId.equals("asc")) {
				criteria.addOrder(Order.asc("taxId"));
			} else {
				criteria.addOrder(Order.desc("taxId"));
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

		return (List<Provider>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Provider> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(Provider.class).addOrder(Order.asc("name")).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Provider> getAllActives() {
		return this.sessionFactory.getCurrentSession().createQuery("from Provider where active = true order by name").list();
	}

	@Override
	public boolean delete(Integer providerId) {
		Provider provider = this.get(providerId);
		if (provider != null) {
			this.sessionFactory.getCurrentSession().delete(provider);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Provider> getPaginated(int start, int length) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Provider.class);
		criteria.setMaxResults(length);
		criteria.setFirstResult(start);
		return criteria.list();
	}

	@Override
	public Long getTotalNumber() {
		Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from Provider").uniqueResult();
		return count;
	}
}
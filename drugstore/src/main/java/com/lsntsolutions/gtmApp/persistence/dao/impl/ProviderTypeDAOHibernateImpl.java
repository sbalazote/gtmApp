package com.lsntsolutions.gtmApp.persistence.dao.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProviderType;
import com.lsntsolutions.gtmApp.persistence.dao.ProviderTypeDAO;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
	public List<ProviderType> getForAutocomplete(String term, Boolean active) {
		String sentence = "from ProviderType where (description like :description";
		if (StringUtility.isInteger(term)) {
			sentence += " or convert(code, CHAR) like :code";
		}
		sentence += ")";
		if (active != null && Boolean.TRUE.equals(active)) {
			sentence += " and active = true";
		}

		Query query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("description", "%" + term + "%");

		if (StringUtility.isInteger(term)) {
			query.setParameter("code", "%" + term + "%");
		}
		return query.list();
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

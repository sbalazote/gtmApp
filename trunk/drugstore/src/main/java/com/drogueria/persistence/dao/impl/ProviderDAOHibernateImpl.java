package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.Provider;
import com.drogueria.persistence.dao.ProviderDAO;
import com.drogueria.util.StringUtils;

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
	public List<Provider> getForAutocomplete(String term, Boolean active) {
		String sentence = "from Provider where (taxId like :taxId or corporateName like :corporateName or locality like :locality";
		if (StringUtils.isInteger(term)) {
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

		if (StringUtils.isInteger(term)) {
			query.setParameter("code", "%" + term + "%");
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Provider> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(Provider.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Provider> getAllActives() {
		return this.sessionFactory.getCurrentSession().createQuery("from Provider where active = true").list();
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
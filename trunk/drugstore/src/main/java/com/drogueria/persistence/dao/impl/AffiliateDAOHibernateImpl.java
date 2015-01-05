package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.Affiliate;
import com.drogueria.persistence.dao.AffiliateDAO;
import com.drogueria.util.StringUtils;

@Repository
public class AffiliateDAOHibernateImpl implements AffiliateDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Affiliate affiliate) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(affiliate);
	}

	@Override
	public Affiliate get(Integer id) {
		return (Affiliate) this.sessionFactory.getCurrentSession().get(Affiliate.class, id);
	}

	@Override
	public Boolean exists(String code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Affiliate where code = :code");
		query.setParameter("code", code);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Affiliate> getForAutocomplete(String term, Boolean active) {
		String sentence = "from Affiliate where (name like :name or surname = :surname";
		if (StringUtils.isInteger(term)) {
			sentence += " or convert(code, CHAR) like :code";
		}
		sentence += ")";
		if (active != null && Boolean.TRUE.equals(active)) {
			sentence += " and active = true";
		}

		Query query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("name", "%" + term + "%");
		query.setParameter("surname", "%" + term + "%");

		if (StringUtils.isInteger(term)) {
			query.setParameter("code", "%" + term + "%");
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Affiliate> getForAutocomplete(String term, Integer clientId, Boolean active, Integer pageNumber, Integer pageSize) {
		Query query = null;

		String sentence = "from Affiliate where (code = :code or CONCAT_WS(' ', name, surname) like :name or CONCAT_WS(' ', surname, name) like :name)";
		if (clientId != null) {
			sentence += " and client.id = :clientId";
		}
		if (active != null && Boolean.TRUE.equals(active)) {
			sentence += " and active = true";
		}

		query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("code", term);
		query.setParameter("name", "%" + term + "%");
		if (clientId != null) {
			query.setParameter("clientId", clientId);
		}
		if ((pageNumber != null) && (pageSize != null)) {
			Integer offset = (pageNumber - 1) * pageSize;
			query.setFirstResult(offset);
			query.setMaxResults(pageSize);
		}

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Affiliate> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(Affiliate.class).list();
	}

	@Override
	public boolean delete(Integer affiliateId) {
		Affiliate affiliate = this.get(affiliateId);
		if (affiliate != null) {
			this.sessionFactory.getCurrentSession().delete(affiliate);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Affiliate> getAllAffiliatesByClient(Integer clientId, Boolean active) {
		Query query = null;

		String sentence = "from Affiliate where client.id = :clientId and active = true";

		query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("clientId", clientId);

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Affiliate> getPaginated(int start, int length) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Affiliate.class);
		criteria.setMaxResults(length);
		criteria.setFirstResult(start);
		return criteria.list();
	}

	@Override
	public Long getTotalNumber() {
		Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from Affiliate").uniqueResult();
		return count;
	}
}

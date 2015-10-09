package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.Affiliate;
import com.lsntsolutions.gtmApp.persistence.dao.AffiliateDAO;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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
	public Affiliate get(String code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Affiliate where code = :code");
		query.setParameter("code", code);
		if(query.list() != null){
			return (Affiliate) query.list().get(0);
		}else{
			return null;
		}
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
		String sentence = "from Affiliate where (name like :name or surname like :surname";
		if (StringUtility.isInteger(term)) {
			sentence += " or convert(code, CHAR) like :code";
		}
		sentence += ")";
		if (active != null && Boolean.TRUE.equals(active)) {
			sentence += " and active = true";
		}

		Query query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("name", "%" + term + "%");
		query.setParameter("surname", "%" + term + "%");

		if (StringUtility.isInteger(term)) {
			query.setParameter("code", "%" + term + "%");
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Affiliate> getForAutocomplete(String term, Integer clientId, Boolean active, Integer pageNumber, Integer pageSize) {
		Query query = null;

		String sentence = "select a from Affiliate as a";

		if (clientId != null) {
			sentence += " inner join a.clients as c";
		}

		sentence += " where (a.code = :code or CONCAT_WS(' ', a.name, a.surname) like :name or CONCAT_WS(' ', a.surname, a.name) like :name)";

		if (clientId != null) {
			sentence += " and c.id = :clientId";
		}

		if (active != null && Boolean.TRUE.equals(active)) {
			sentence += " and a.active = true";
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

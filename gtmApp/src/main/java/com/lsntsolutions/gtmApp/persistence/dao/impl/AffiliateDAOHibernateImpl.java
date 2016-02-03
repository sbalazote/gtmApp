package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.Affiliate;
import com.lsntsolutions.gtmApp.persistence.dao.AffiliateDAO;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.lsntsolutions.gtmApp.util.StringUtility;

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
	public List<Affiliate> getForAutocomplete(String term, Boolean active, Integer clientId, String sortId, String sortCode, String sortName, String sortSurname, String sortDocumentType, String sortDocument, String sortActive) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Affiliate.class);

		if (StringUtility.isInteger(term)) {
			criteria.add(Restrictions.eq("id", term));
		} else {
			if(term != "") {
				criteria.add(Restrictions.or(Restrictions.sqlRestriction("CONCAT_WS(' ', name, surname) like (?)", "%"+term+"%" , StandardBasicTypes.STRING),
						Restrictions.sqlRestriction("CONCAT_WS(' ', surname, name) like (?)", "%"+term+"%" , StandardBasicTypes.STRING),
						Restrictions.ilike("code", term, MatchMode.ANYWHERE), Restrictions.ilike("document", term, MatchMode.ANYWHERE)));
			}
		}

		if (active != null && Boolean.TRUE.equals(active)) {
			criteria.add(Restrictions.eq("active", true));
		}

		if (clientId != null) {
			criteria.createCriteria("clients" , "client");
			criteria.add(Restrictions.eq("client.id", clientId));
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
		} else if (sortSurname != null) {
			if (sortSurname.equals("asc")) {
				criteria.addOrder(Order.asc("surname"));
			} else {
				criteria.addOrder(Order.desc("surname"));
			}
		} else if (sortDocumentType != null) {
			if (sortDocumentType.equals("asc")) {
				criteria.addOrder(Order.asc("documentType"));
			} else {
				criteria.addOrder(Order.desc("documentType"));
			}
		} else if (sortDocument != null) {
			if (sortDocument.equals("asc")) {
				criteria.addOrder(Order.asc("document"));
			} else {
				criteria.addOrder(Order.desc("document"));
			}
		}else if (sortActive != null) {
			if (sortDocument.equals("asc")) {
				criteria.addOrder(Order.asc("active"));
			} else {
				criteria.addOrder(Order.desc("active"));
			}
		}else {
			criteria.addOrder(Order.asc("id"));
		}

		return criteria.list();
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

		String sentence = "select distinct a from Affiliate as a inner join a.clients as c where c.id = :clientId and a.active = true";

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

package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.Concept;
import com.lsntsolutions.gtmApp.persistence.dao.ConceptDAO;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConceptDAOHibernateImpl implements ConceptDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Concept concept) {
		this.sessionFactory.getCurrentSession().merge(concept);
	}

	@Override
	public Concept get(Integer id) {
		return (Concept) this.sessionFactory.getCurrentSession().get(Concept.class, id);
	}

	@Override
	public Boolean exists(Integer code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Concept where code = :code");
		query.setParameter("code", code);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Concept> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortDescription, String sortDeliveryNotePOS, String sortIsInformAnmat, String sortIsActive) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Concept.class);

		criteria.createAlias("deliveryNoteEnumerator", "dne");

		if (StringUtility.isInteger(term)) {
			criteria.add(Restrictions.or(Restrictions.eq("id", Integer.parseInt(term)), Restrictions.eq("code",  Integer.parseInt(term)), Restrictions.eq("dne.deliveryNotePOS",  Integer.parseInt(term))));
		} else {
			criteria.add(Restrictions.ilike("description", term, MatchMode.ANYWHERE));
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
		} else if (sortDescription != null) {
			if (sortDescription.equals("asc")) {
				criteria.addOrder(Order.asc("description"));
			} else {
				criteria.addOrder(Order.desc("description"));
			}
		} else if (sortDeliveryNotePOS != null) {
			if (sortDeliveryNotePOS.equals("asc")) {
				criteria.addOrder(Order.asc("dne.deliveryNotePOS"));
			} else {
				criteria.addOrder(Order.desc("dne.deliveryNotePOS"));
			}
		} else if (sortIsInformAnmat != null) {
			if (sortIsInformAnmat.equals("asc")) {
				criteria.addOrder(Order.asc("informAnmat"));
			} else {
				criteria.addOrder(Order.desc("informAnmat"));
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

		return (List<Concept>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Concept> getForAutocomplete(String term) {
		Query query;
		try {
			Integer code = Integer.valueOf(term);
			query = this.sessionFactory.getCurrentSession().createQuery("from Concept where code = :code");
			query.setParameter("code", code);

		} catch (NumberFormatException e) {
			query = this.sessionFactory.getCurrentSession().createQuery("from Concept where description like :description");
			query.setParameter("description", term + "%");
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Concept> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(Concept.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Concept> getAllActives(Boolean input) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Concept where active = true and input = :input");
		if (input != null) {
			query.setParameter("input", input.booleanValue());
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Concept> getAllReturnFromClientConcepts() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Concept where refund = true and client = true");
		return query.list();
	}

	@Override
	public boolean delete(Integer conceptId) {
		Concept concept = this.get(conceptId);
		if (concept != null) {
			concept.getEvents().clear();
			this.sessionFactory.getCurrentSession().delete(concept);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Concept> getPaginated(int start, int length) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Concept.class);
		criteria.setMaxResults(length);
		criteria.setFirstResult(start);
		return criteria.list();
	}

	@Override
	public Long getTotalNumber() {
		Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from Concept").uniqueResult();
		return count;
	}

	@Override
	public Concept getForUpdate(Integer id) {
		return (Concept) this.sessionFactory.getCurrentSession().get(Concept.class, id, LockOptions.UPGRADE);
	}

	@Override
	public List<Concept> getConceptForInput() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Concept where input = true and destruction = false and refund = false and active = true order by description");
		return query.list();
	}

	@Override
	public List<Concept> getDestructionConcept() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Concept where destruction = true and active = true");
		return query.list();
	}
}

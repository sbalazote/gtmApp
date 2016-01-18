package com.lsntsolutions.gtmApp.persistence.dao.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.model.Concept;
import com.lsntsolutions.gtmApp.persistence.dao.ConceptDAO;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
	public List<Concept> getForAutocomplete(String term, Boolean active) {
		String sentence = "from Concept where (description like :description";
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
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Concept where input = true and destruction = false and refund = false and active = true");
		return query.list();
	}

	@Override
	public List<Concept> getDestructionConcept() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Concept where destruction = true and active = true");
		return query.list();
	}
}

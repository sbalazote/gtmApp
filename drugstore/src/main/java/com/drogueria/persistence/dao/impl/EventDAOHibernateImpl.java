package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.Property;
import com.drogueria.model.Event;
import com.drogueria.persistence.dao.EventDAO;
import com.drogueria.util.StringUtility;

@Repository
public class EventDAOHibernateImpl implements EventDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Event event) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(event);
	}

	@Override
	public Event get(Integer id) {
		return (Event) this.sessionFactory.getCurrentSession().get(Event.class, id);
	}

	@Override
	public Boolean exists(Integer code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Event where code = :code");
		query.setParameter("code", code);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Event> getForAutocomplete(String term, Boolean active) {
		String sentence = "from Event where (description like :description";
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
	public List<Event> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(Event.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Event> getInputOutput(boolean input) {
		Property Property = (Property) this.sessionFactory.getCurrentSession().createCriteria(Property.class).list().get(0);
		Integer agentId = Property.getAgent().getId();
		Query query = null;
		if (!input) {
			query = this.sessionFactory.getCurrentSession().createQuery("from Event where originAgent.id = :agentId");
			query.setParameter("agentId", agentId);
		} else {
			query = this.sessionFactory.getCurrentSession().createQuery("from Event where destinationAgent.id = :agentId");
			query.setParameter("agentId", agentId);
		}
		return query.list();
	}

	@Override
	public boolean delete(Integer eventId) {
		Event event = this.get(eventId);
		if (event != null) {
			this.sessionFactory.getCurrentSession().delete(event);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Event> getPaginated(int start, int length) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Event.class);
		criteria.setMaxResults(length);
		criteria.setFirstResult(start);
		return criteria.list();
	}

	@Override
	public Long getTotalNumber() {
		Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from Event").uniqueResult();
		return count;
	}
}
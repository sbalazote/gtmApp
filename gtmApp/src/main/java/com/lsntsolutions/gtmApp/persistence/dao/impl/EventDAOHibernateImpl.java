package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.Event;
import com.lsntsolutions.gtmApp.model.Property;
import com.lsntsolutions.gtmApp.persistence.dao.EventDAO;
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
	public List<Event> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortDescription, String sortOriginAgent, String sortDestinationAgent, String sortIsActive) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Event.class);

		criteria.createAlias("originAgent", "oa");
		criteria.createAlias("destinationAgent", "da");

		if (StringUtility.isInteger(term)) {
			criteria.add(Restrictions.or(Restrictions.eq("id", Integer.parseInt(term)), Restrictions.eq("code",  Integer.parseInt(term))));
		} else {
			criteria.add(Restrictions.or(Restrictions.ilike("description", term, MatchMode.ANYWHERE), Restrictions.ilike("oa.description", term, MatchMode.ANYWHERE), Restrictions.ilike("da.description", term, MatchMode.ANYWHERE)));
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
		} else if (sortOriginAgent != null) {
			if (sortOriginAgent.equals("asc")) {
				criteria.addOrder(Order.asc("oa.description"));
			} else {
				criteria.addOrder(Order.desc("oa.description"));
			}
		} else if (sortDestinationAgent != null) {
			if (sortDestinationAgent.equals("asc")) {
				criteria.addOrder(Order.asc("da.description"));
			} else {
				criteria.addOrder(Order.desc("da.description"));
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

		return (List<Event>) criteria.list();
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
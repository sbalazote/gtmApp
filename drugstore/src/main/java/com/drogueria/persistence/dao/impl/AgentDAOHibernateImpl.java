package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.Agent;
import com.drogueria.persistence.dao.AgentDAO;
import com.drogueria.util.StringUtility;

@Repository
public class AgentDAOHibernateImpl implements AgentDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Agent agent) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(agent);
	}

	@Override
	public Agent get(Integer id) {
		return (Agent) this.sessionFactory.getCurrentSession().get(Agent.class, id);
	}

	@Override
	public Boolean exists(Integer code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Agent where code = :code");
		query.setParameter("code", code);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Agent> getForAutocomplete(String term, Boolean active) {
		String sentence = "from Agent where (description like :description";
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
	public List<Agent> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(Agent.class).list();
	}

	@Override
	public boolean delete(Integer agentId) {
		Agent agent = this.get(agentId);
		if (agent != null) {
			this.sessionFactory.getCurrentSession().delete(agent);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Agent> getPaginated(int start, int length) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Agent.class);
		criteria.setMaxResults(length);
		criteria.setFirstResult(start);
		return criteria.list();
	}

	@Override
	public Long getTotalNumber() {
		Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from Agent").uniqueResult();
		return count;
	}
}

package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.LogisticsOperator;
import com.drogueria.persistence.dao.LogisticsOperatorDAO;
import com.drogueria.util.StringUtility;

@Repository
public class LogisticsOperatorDAOHibernateImpl implements LogisticsOperatorDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(LogisticsOperator logisticsOperator) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(logisticsOperator);
	}

	@Override
	public LogisticsOperator get(Integer id) {
		return (LogisticsOperator) this.sessionFactory.getCurrentSession().get(LogisticsOperator.class, id);
	}

	@Override
	public Boolean exists(Integer code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from LogisticsOperator where code = :code");
		query.setParameter("code", code);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LogisticsOperator> getForAutocomplete(String term, Boolean active) {
		String sentence = "from LogisticsOperator where (taxId like :taxId or corporateName like :corporateName or locality like :locality";
		if (StringUtility.isInteger(term)) {
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

		if (StringUtility.isInteger(term)) {
			query.setParameter("code", "%" + term + "%");
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LogisticsOperator> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(LogisticsOperator.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LogisticsOperator> getAllActives() {
		return this.sessionFactory.getCurrentSession().createQuery("from LogisticsOperator where active = true").list();
	}

	@Override
	public boolean delete(Integer logisticsOperatorId) {
		LogisticsOperator logisticsOperator = this.get(logisticsOperatorId);
		if (logisticsOperator != null) {
			this.sessionFactory.getCurrentSession().delete(logisticsOperator);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LogisticsOperator> getPaginated(int start, int length) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(LogisticsOperator.class);
		criteria.setMaxResults(length);
		criteria.setFirstResult(start);
		return criteria.list();
	}

	@Override
	public Long getTotalNumber() {
		Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from LogisticsOperator").uniqueResult();
		return count;
	}
}

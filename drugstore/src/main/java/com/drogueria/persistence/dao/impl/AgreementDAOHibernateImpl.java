package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.Agreement;
import com.drogueria.persistence.dao.AgreementDAO;
import com.drogueria.util.StringUtility;

@Repository
public class AgreementDAOHibernateImpl implements AgreementDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Agreement agreement) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(agreement);
	}

	@Override
	public Agreement get(Integer id) {
		return (Agreement) this.sessionFactory.getCurrentSession().get(Agreement.class, id);
	}

	@Override
	public Boolean exists(Integer code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Agreement where code = :code");
		query.setParameter("code", code);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Agreement> getForAutocomplete(String term, Boolean active) {
		String sentence = "from Agreement where (description like :description";
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
	public List<Agreement> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(Agreement.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Agreement> getAllActives() {
		return this.sessionFactory.getCurrentSession().createQuery("from Agreement where active = true").list();
	}

	@Override
	public boolean delete(Integer agreementId) {
		Agreement agreement = this.get(agreementId);
		if (agreement != null) {
			this.sessionFactory.getCurrentSession().delete(agreement);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Agreement> getPaginated(int start, int length) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Agreement.class);
		criteria.setMaxResults(length);
		criteria.setFirstResult(start);
		return criteria.list();
	}

	@Override
	public Long getTotalNumber() {
		Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from Agreement").uniqueResult();
		return count;
	}
	@Override
	public boolean isConceptInUse(Integer conceptId){
        boolean isUseAsDeliveryNoteConcept = false;
        boolean isUseAsDestructionConcept = false;
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Agreement where deliveryNoteConcept.id = :conceptId");
		query.setParameter("conceptId", conceptId);
        isUseAsDestructionConcept = !query.list().isEmpty();
        query = this.sessionFactory.getCurrentSession().createQuery("from Agreement where destructionConcept.id = :conceptId");
        query.setParameter("conceptId", conceptId);
        return (isUseAsDeliveryNoteConcept || isUseAsDestructionConcept);
	}
}

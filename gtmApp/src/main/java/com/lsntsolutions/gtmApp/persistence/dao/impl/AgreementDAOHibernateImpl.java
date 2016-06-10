package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.Agreement;
import com.lsntsolutions.gtmApp.persistence.dao.AgreementDAO;
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
	public List<Agreement> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortDescription, String sortNumberOfDeliveryNoteDetailsPerPage, String sortIsPickingList, String sortIsActive, String sortIsDeliveryNoteConcept, String sortDestructionConcept) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Agreement.class);

		criteria.createAlias("deliveryNoteConcept", "dnc");
		criteria.createAlias("destructionConcept", "dc");

		if (StringUtility.isInteger(term)) {
			criteria.add(Restrictions.or(Restrictions.eq("id", Integer.parseInt(term)), Restrictions.eq("code",  Integer.parseInt(term)), Restrictions.eq("numberOfDeliveryNoteDetailsPerPage",  Integer.parseInt(term))));
		} else {
			criteria.add(Restrictions.or(Restrictions.ilike("description", term, MatchMode.ANYWHERE), Restrictions.ilike("dnc.description", term, MatchMode.ANYWHERE), Restrictions.ilike("dc.description", term, MatchMode.ANYWHERE)));
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
		} else if (sortNumberOfDeliveryNoteDetailsPerPage != null) {
			if (sortNumberOfDeliveryNoteDetailsPerPage.equals("asc")) {
				criteria.addOrder(Order.asc("numberOfDeliveryNoteDetailsPerPage"));
			} else {
				criteria.addOrder(Order.desc("numberOfDeliveryNoteDetailsPerPage"));
			}
		} else if (sortIsPickingList != null) {
			if (sortIsPickingList.equals("asc")) {
				criteria.addOrder(Order.asc("pickingList"));
			} else {
				criteria.addOrder(Order.desc("pickingList"));
			}
		} else if (sortIsActive != null) {
			if (sortIsActive.equals("asc")) {
				criteria.addOrder(Order.asc("active"));
			} else {
				criteria.addOrder(Order.desc("active"));
			}
		} else if (sortIsDeliveryNoteConcept != null) {
			if (sortIsDeliveryNoteConcept.equals("asc")) {
				criteria.addOrder(Order.asc("dnc.description"));
			} else {
				criteria.addOrder(Order.desc("dnc.description"));
			}
		} else if (sortDestructionConcept != null) {
			if (sortDestructionConcept.equals("asc")) {
				criteria.addOrder(Order.asc("dc.description"));
			} else {
				criteria.addOrder(Order.desc("dc.description"));
			}
		} else {
			criteria.addOrder(Order.asc("id"));
		}

		return (List<Agreement>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Agreement> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(Agreement.class).addOrder(Order.asc("description")).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Agreement> getAllActives() {
		return this.sessionFactory.getCurrentSession().createQuery("from Agreement where active = true order by description").list();
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

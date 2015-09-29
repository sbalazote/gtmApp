package com.lsntsolutions.gtmApp.persistence.dao.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.persistence.dao.DeliveryNoteEnumeratorDAO;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lsntsolutions.gtmApp.model.DeliveryNoteEnumerator;

@Repository
public class DeliveryNoteEnumeratorDAOImpl implements DeliveryNoteEnumeratorDAO {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(DeliveryNoteEnumerator DeliveryNoteEnumerator) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(DeliveryNoteEnumerator);
	}

	@Override
	public DeliveryNoteEnumerator get(Integer id) {
		return (DeliveryNoteEnumerator) this.sessionFactory.getCurrentSession().get(DeliveryNoteEnumerator.class, id);
	}

	@Override
	public Boolean exists(Integer deliveryNotePOS, Boolean fake) {
		Query query = this.sessionFactory.getCurrentSession().createQuery(
				"from DeliveryNoteEnumerator where deliveryNotePOS = :deliveryNotePOS and fake = :fake");
		query.setParameter("deliveryNotePOS", deliveryNotePOS);
		query.setParameter("fake", fake);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryNoteEnumerator> getForAutocomplete(String term, Boolean active, Boolean fake) {
		String sentence = "from DeliveryNoteEnumerator where (";
		if (StringUtility.isInteger(term)) {
			sentence += " convert(deliveryNotePOS, CHAR) like :deliveryNotePOS";
		}
		sentence += ")";
		if (active != null && Boolean.TRUE.equals(active)) {
			sentence += " and active = true";
		}

		if (fake != null && Boolean.TRUE.equals(fake)) {
			sentence += " and fake = true";
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
	public List<DeliveryNoteEnumerator> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(DeliveryNoteEnumerator.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryNoteEnumerator> getReals(boolean fake) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from DeliveryNoteEnumerator where fake = :fake");
		query.setParameter("fake", fake);
		return query.list();
	}

	@Override
	public boolean delete(Integer deliveryNoteEnumeratorId) {
		DeliveryNoteEnumerator DeliveryNoteEnumerator = this.get(deliveryNoteEnumeratorId);
		if (DeliveryNoteEnumerator != null) {
			this.sessionFactory.getCurrentSession().delete(DeliveryNoteEnumerator);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryNoteEnumerator> getPaginated(int start, int length) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(DeliveryNoteEnumerator.class);
		criteria.setMaxResults(length);
		criteria.setFirstResult(start);
		return criteria.list();
	}

	@Override
	public Long getTotalNumber() {
		Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from DeliveryNoteEnumerator").uniqueResult();
		return count;
	}
}

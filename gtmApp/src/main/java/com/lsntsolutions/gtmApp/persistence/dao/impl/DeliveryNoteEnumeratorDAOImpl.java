package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.DeliveryNoteEnumerator;
import com.lsntsolutions.gtmApp.persistence.dao.DeliveryNoteEnumeratorDAO;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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
	public List<DeliveryNoteEnumerator> getForAutocomplete(String term, Boolean active, Boolean fake, String sortId, String sortDeliveryNotePOS, String sortLastDeliveryNoteNumber, String sortIsActive) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(DeliveryNoteEnumerator.class);

		if (StringUtility.isInteger(term)) {
			criteria.add(Restrictions.or(Restrictions.eq("id", Integer.parseInt(term)), Restrictions.eq("deliveryNotePOS",  Integer.parseInt(term)), Restrictions.eq("lastDeliveryNoteNumber",  Integer.parseInt(term))));
		}

		if (active != null && Boolean.TRUE.equals(active)) {
			criteria.add(Restrictions.eq("active", true));
		}

		if (fake != null && Boolean.TRUE.equals(fake)) {
			criteria.add(Restrictions.eq("fake", true));
		}

		if (sortId != null) {
			if (sortId.equals("asc")) {
				criteria.addOrder(Order.asc("id"));
			} else {
				criteria.addOrder(Order.desc("id"));
			}
		} else if (sortDeliveryNotePOS != null) {
			if (sortDeliveryNotePOS.equals("asc")) {
				criteria.addOrder(Order.asc("deliveryNotePOS"));
			} else {
				criteria.addOrder(Order.desc("deliveryNotePOS"));
			}
		} else if (sortLastDeliveryNoteNumber != null) {
			if (sortLastDeliveryNoteNumber.equals("asc")) {
				criteria.addOrder(Order.asc("lastDeliveryNoteNumber"));
			} else {
				criteria.addOrder(Order.desc("lastDeliveryNoteNumber"));
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

		return (List<DeliveryNoteEnumerator>) criteria.list();
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
	public Boolean checkNewDeliveryNoteNumber(Integer deliveryNotePOS, Integer lastDeliveryNoteNumberInput) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from DeliveryNoteEnumerator where deliveryNotePOS = :deliveryNotePOS and lastDeliveryNoteNumber <= :lastDeliveryNoteNumberInput");
		query.setParameter("deliveryNotePOS", deliveryNotePOS);
		query.setParameter("lastDeliveryNoteNumberInput", lastDeliveryNoteNumberInput);
		return !query.list().isEmpty();
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

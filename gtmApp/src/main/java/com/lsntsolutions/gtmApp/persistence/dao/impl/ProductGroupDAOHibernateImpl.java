package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.ProductGroup;
import com.lsntsolutions.gtmApp.persistence.dao.ProductGroupDAO;
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
public class ProductGroupDAOHibernateImpl implements ProductGroupDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(ProductGroup productGroup) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(productGroup);
	}

	@Override
	public ProductGroup get(Integer id) {
		return (ProductGroup) this.sessionFactory.getCurrentSession().get(ProductGroup.class, id);
	}

	@Override
	public Boolean exists(Integer code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from ProductGroup where code = :code");
		query.setParameter("code", code);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductGroup> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortDescription, String sortIsActive) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(ProductGroup.class);

		if (StringUtility.isInteger(term)) {
			criteria.add(Restrictions.or(Restrictions.eq("id", Integer.parseInt(term)), Restrictions.eq("code",  Integer.parseInt(term))));
		} else {
			criteria.add(Restrictions.ilike("description", term, MatchMode.ANYWHERE));
		}

		if (active != null && Boolean.TRUE.equals(active)) {
			criteria.add(Restrictions.eq("active", "true"));
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
		} else if (sortIsActive != null) {
			if (sortIsActive.equals("asc")) {
				criteria.addOrder(Order.asc("active"));
			} else {
				criteria.addOrder(Order.desc("active"));
			}
		} else {
			criteria.addOrder(Order.asc("id"));
		}

		return (List<ProductGroup>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductGroup> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(ProductGroup.class).list();
	}

	@Override
	public boolean delete(Integer productGroupId) {
		ProductGroup productGroup = this.get(productGroupId);
		if (productGroup != null) {
			this.sessionFactory.getCurrentSession().delete(productGroup);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductGroup> getPaginated(int start, int length) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(ProductGroup.class);
		criteria.setMaxResults(length);
		criteria.setFirstResult(start);
		return criteria.list();
	}

	@Override
	public Long getTotalNumber() {
		Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from ProductGroup").uniqueResult();
		return count;
	}
}

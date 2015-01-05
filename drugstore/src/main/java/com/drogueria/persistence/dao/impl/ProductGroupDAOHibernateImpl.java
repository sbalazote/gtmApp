package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.ProductGroup;
import com.drogueria.persistence.dao.ProductGroupDAO;
import com.drogueria.util.StringUtils;

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
	public List<ProductGroup> getForAutocomplete(String term, Boolean active) {
		String sentence = "from ProductGroup where (description like :description";
		if (StringUtils.isInteger(term)) {
			sentence += " or convert(code, CHAR) like :code";
		}
		sentence += ")";
		if (active != null && Boolean.TRUE.equals(active)) {
			sentence += " and active = true";
		}

		Query query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("description", "%" + term + "%");

		if (StringUtils.isInteger(term)) {
			query.setParameter("code", "%" + term + "%");
		}
		return query.list();
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

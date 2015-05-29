package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.ProductDrugCategory;
import com.drogueria.persistence.dao.ProductDrugCategoryDAO;
import com.drogueria.util.StringUtility;

@Repository
public class ProductDrugCategoryDAOHibernateImpl implements ProductDrugCategoryDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(ProductDrugCategory productGroup) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(productGroup);
	}

	@Override
	public ProductDrugCategory get(Integer id) {
		return (ProductDrugCategory) this.sessionFactory.getCurrentSession().get(ProductDrugCategory.class, id);
	}

	@Override
	public Boolean exists(Integer code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from ProductDrugCategory where code = :code");
		query.setParameter("code", code);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductDrugCategory> getForAutocomplete(String term, Boolean active) {
		String sentence = "from ProductDrugCategory where (description like :description";
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
	public List<ProductDrugCategory> getForAutocomplete(String term) {
		String sentence = "from ProductDrugCategory where description like :description";
		if (StringUtility.isInteger(term)) {
			sentence += " or convert(code, CHAR) like :code";
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
	public List<ProductDrugCategory> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(ProductDrugCategory.class).list();
	}

	@Override
	public boolean delete(Integer productDrugCategoryId) {
		ProductDrugCategory productDrugCategory = this.get(productDrugCategoryId);
		if (productDrugCategory != null) {
			this.sessionFactory.getCurrentSession().delete(productDrugCategory);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductDrugCategory> getPaginated(int start, int length) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(ProductDrugCategory.class);
		criteria.setMaxResults(length);
		criteria.setFirstResult(start);
		return criteria.list();
	}

	@Override
	public Long getTotalNumber() {
		Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from ProductDrugCategory").uniqueResult();
		return count;
	}
}
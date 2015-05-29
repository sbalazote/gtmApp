package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.ProductBrand;
import com.drogueria.persistence.dao.ProductBrandDAO;
import com.drogueria.util.StringUtility;

@Repository
public class ProductBrandDAOHibernateImpl implements ProductBrandDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(ProductBrand productGroup) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(productGroup);
	}

	@Override
	public ProductBrand get(Integer id) {
		return (ProductBrand) this.sessionFactory.getCurrentSession().get(ProductBrand.class, id);
	}

	@Override
	public Boolean exists(Integer code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from ProductBrand where code = :code");
		query.setParameter("code", code);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductBrand> getForAutocomplete(String term, Boolean active) {
		String sentence = "from ProductBrand where (description like :description";
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
	public List<ProductBrand> getForAutocomplete(String term) {
		String sentence = "from ProductBrand where description like :description";
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
	public List<ProductBrand> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(ProductBrand.class).list();
	}

	@Override
	public boolean delete(Integer productBrandId) {
		ProductBrand productBrand = this.get(productBrandId);
		if (productBrand != null) {
			this.sessionFactory.getCurrentSession().delete(productBrand);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductBrand> getPaginated(int start, int length) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(ProductBrand.class);
		criteria.setMaxResults(length);
		criteria.setFirstResult(start);
		return criteria.list();
	}

	@Override
	public Long getTotalNumber() {
		Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from ProductBrand").uniqueResult();
		return count;
	}
}
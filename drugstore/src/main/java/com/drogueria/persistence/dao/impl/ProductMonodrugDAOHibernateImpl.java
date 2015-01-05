package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.ProductMonodrug;
import com.drogueria.persistence.dao.ProductMonodrugDAO;
import com.drogueria.util.StringUtils;

@Repository
public class ProductMonodrugDAOHibernateImpl implements ProductMonodrugDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(ProductMonodrug productMonodrug) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(productMonodrug);
	}

	@Override
	public ProductMonodrug get(Integer id) {
		return (ProductMonodrug) this.sessionFactory.getCurrentSession().get(ProductMonodrug.class, id);
	}

	@Override
	public Boolean exists(Integer code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from ProductMonodrug where code = :code");
		query.setParameter("code", code);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductMonodrug> getForAutocomplete(String term, Boolean active) {
		String sentence = "from ProductMonodrug where (description like :description";
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
	public List<ProductMonodrug> getForAutocomplete(String term) {
		String sentence = "from ProductMonodrug where description like :description";
		if (StringUtils.isInteger(term)) {
			sentence += " or convert(code, CHAR) like :code";
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
	public List<ProductMonodrug> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(ProductMonodrug.class).list();
	}

	@Override
	public boolean delete(Integer productMonodrugId) {
		ProductMonodrug productMonodrug = this.get(productMonodrugId);
		if (productMonodrug != null) {
			this.sessionFactory.getCurrentSession().delete(productMonodrug);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductMonodrug> getPaginated(int start, int length) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(ProductMonodrug.class);
		criteria.setMaxResults(length);
		criteria.setFirstResult(start);
		return criteria.list();
	}

	@Override
	public Long getTotalNumber() {
		Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from ProductMonodrug").uniqueResult();
		return count;
	}
}

package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.Product;
import com.drogueria.persistence.dao.ProductDAO;
import com.drogueria.util.StringUtility;

@Repository
public class ProductDAOHibernateImpl implements ProductDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Product product) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(product);
	}

	@Override
	public Product get(Integer id) {
		return (Product) this.sessionFactory.getCurrentSession().get(Product.class, id);
	}

	@Override
	public Boolean exists(Integer code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Product where code = :code");
		query.setParameter("code", code);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getForAutocomplete(String term, Boolean active) {
		String gtinSentence = "select p from Product as p inner join p.gtins as pg where pg.number = :gtin";
		String literalSentence = "select p from Product as p where description like :description or brand.description like :brand or monodrug.description like :monodrug";

		if (StringUtility.isInteger(term)) {
			literalSentence += " or convert(code, CHAR) like :code";
		}
		if (active != null && Boolean.TRUE.equals(active)) {
			literalSentence += " and active = true";
		}

		Query gtinQuery = this.sessionFactory.getCurrentSession().createQuery(gtinSentence);
		gtinQuery.setParameter("gtin", StringUtility.removeLeadingZero(term));
		List<Product> gtinSentenceQuery = gtinQuery.list();

		Query literalQuery = this.sessionFactory.getCurrentSession().createQuery(literalSentence);
		literalQuery.setParameter("description", "%" + term + "%");
		literalQuery.setParameter("brand", "%" + term + "%");
		literalQuery.setParameter("monodrug", "%" + term + "%");

		if (StringUtility.isInteger(term)) {
			literalQuery.setParameter("code", "%" + term + "%");
		}
		List<Product> literalSentenceQuery = literalQuery.list();

		gtinSentenceQuery.addAll(literalSentenceQuery);

		return gtinSentenceQuery;
	}

	@Override
	public Product getByGtin(String gtin) {
		try {
			Query query = this.sessionFactory.getCurrentSession().createQuery(
					"select p from Product as p inner join p.gtins as g where g.number = :gtin and p.active = true");
			query.setParameter("gtin", StringUtility.removeLeadingZero(gtin));
			return (Product) query.list().get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public boolean delete(Integer productId) {
		Product product = this.get(productId);
		if (product != null) {
			this.sessionFactory.getCurrentSession().delete(product);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateFromAlfabeta(Product product) {
		try {
			this.sessionFactory.getCurrentSession().saveOrUpdate(product);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Product getByCode(Integer code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Product where code = :code");
		query.setParameter("code", code);
		return (Product) query.list().get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(Product.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getPaginated(int start, int length) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Product.class);
		criteria.setMaxResults(length);
		criteria.setFirstResult(start);
		return criteria.list();
	}

	@Override
	public Long getTotalNumber() {
		Long count = (Long) this.sessionFactory.getCurrentSession().createQuery("select count(*) from Product").uniqueResult();
		return count;
	}
}

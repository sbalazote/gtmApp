package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.Product;
import com.lsntsolutions.gtmApp.persistence.dao.ProductDAO;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDAOHibernateImpl implements ProductDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Cacheable(value="saveProductCache", key="#name")
	public void save(Product product) {
		this.sessionFactory.getCurrentSession().merge(product);
	}

	@Override
	public Product get(Integer id) {
		return (Product) this.sessionFactory.getCurrentSession().get(Product.class, id);
	}

	@Override
	@Cacheable(value="existsProductCache", key="#code")
	public Boolean exists(Integer code) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Product where code = :code");
		query.setParameter("code", code);
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getForAutocomplete(String term, Boolean active) {

		// Si active es null significa que busco x activos y inactivos.
		List<Product> gtinSentenceQuery = new ArrayList<Product>();
		if (active == null) {
			String gtinSentence = "select p from Product as p inner join p.gtins as pg where pg.number = :gtin";
			Query gtinQuery = this.sessionFactory.getCurrentSession().createQuery(gtinSentence);
			gtinQuery.setParameter("gtin", StringUtility.removeLeadingZero(term));
			gtinSentenceQuery = gtinQuery.list();
		}

		String literalSentence = "select p from Product as p where (description like :description or brand.description like :brand or monodrug.description like :monodrug";

		if (StringUtility.isInteger(term)) {
			literalSentence += " or convert(code, CHAR) like :code";
		}
		literalSentence += ")";
		if (active != null && Boolean.TRUE.equals(active)) {
			literalSentence += " and active = true";
		}

		Query literalQuery = this.sessionFactory.getCurrentSession().createQuery(literalSentence);
		literalQuery.setParameter("description", "%" + term + "%");
		literalQuery.setParameter("brand", "%" + term + "%");
		literalQuery.setParameter("monodrug", "%" + term + "%");

		if (StringUtility.isInteger(term)) {
			literalQuery.setParameter("code", "%" + term + "%");
		}
		List<Product> literalSentenceQuery = literalQuery.list();

		if (active == null) {
			literalSentenceQuery.addAll(gtinSentenceQuery);
		}

		return literalSentenceQuery;
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

	//TODO reemplazar el metodo getByGtin(String) por getByGtin(String, boolean)
	@Override
	public Product getByGtin(String gtin, Boolean active) {
		try {
			String sentence = "select p from Product as p inner join p.gtins as g where g.number = :gtin";
			if(active != null){
				sentence += "p.active =" + active;
			}
			Query query = this.sessionFactory.getCurrentSession().createQuery(sentence);
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
	public Integer updateFromAlfabeta(String description, BigDecimal price, Integer code, String gtin, Boolean cold) {
		Query query = this.sessionFactory.getCurrentSession()
				.createSQLQuery("CALL update_from_alfabeta_proc(:description, :price, :code, :gtin, :cold)")
				.setParameter("description", description)
				.setParameter("price", price)
				.setParameter("code", code)
				.setParameter("gtin", gtin.trim().isEmpty() ? null : gtin)
				.setParameter("cold", cold);
		return query.executeUpdate();
	}

	@Override
	@Cacheable(value="getByCodeProductCache", key="#code")
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

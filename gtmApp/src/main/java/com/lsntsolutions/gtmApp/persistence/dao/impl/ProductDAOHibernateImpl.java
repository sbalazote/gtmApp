package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.Product;
import com.lsntsolutions.gtmApp.persistence.dao.ProductDAO;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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

	private Criteria buildCriteria(String searchPhrase, Boolean active, String sortId, String sortCode, String sortDescription, String sortGtin, String sortIsCold, String sortIsActive) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Product.class, "product");

		criteria.createAlias("gtins", "g");
		criteria.setFetchMode("gtins", FetchMode.SELECT);

		if (StringUtility.isInteger(searchPhrase)) {
			criteria.add(Restrictions.or(Restrictions.eq("id", Integer.parseInt(searchPhrase)), Restrictions.eq("code",  Integer.parseInt(searchPhrase))));
		} else {
			criteria.add(Restrictions.or(Restrictions.ilike("description", searchPhrase, MatchMode.ANYWHERE), Restrictions.ilike("g.number", searchPhrase, MatchMode.ANYWHERE), Restrictions.ilike("g.number", StringUtility.removeLeadingZero(searchPhrase), MatchMode.ANYWHERE)));
		}

		if (active != null && Boolean.TRUE.equals(active)) {
			criteria.add(Restrictions.eq("active", true));
		}

		return criteria;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getForAutocomplete(String searchPhrase, Boolean active, String sortId, String sortCode, String sortDescription, String sortGtin, String sortIsCold, String sortIsActive, Integer start, Integer length) {
		Criteria criteria = this.buildCriteria(searchPhrase, active, sortId, sortCode, sortDescription, sortGtin, sortIsCold, sortIsActive);

		criteria.setProjection(Projections.distinct(Projections.property("id")));
		List ids = criteria.list();

		if (ids.size() > 0) {
			criteria = this.sessionFactory.getCurrentSession().createCriteria(Product.class);
			criteria.add(Restrictions.in("id", ids));
            criteria.createAlias("gtins", "g");
			criteria.setFetchMode("gtins", FetchMode.JOIN);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

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
			} else if (sortGtin != null) {
				if (sortGtin.equals("asc")) {
					criteria.addOrder(Order.asc("g.number"));
				} else {
					criteria.addOrder(Order.desc("g.number"));
				}
			} else if (sortIsCold != null) {
				if (sortIsCold.equals("asc")) {
					criteria.addOrder(Order.asc("cold"));
				} else {
					criteria.addOrder(Order.desc("cold"));
				}
			} else if (sortIsActive != null) {
				if (sortIsActive.equals("asc")) {
					criteria.addOrder(Order.asc("active"));
				} else {
					criteria.addOrder(Order.desc("active"));
				}
			}else {
				criteria.addOrder(Order.asc("id"));
			}

            if (start != null) {
                criteria.setFirstResult(start);
            }
            if (length != null) {
                criteria.setMaxResults(length);
            }

			return (List<Product>) criteria.list();
		} else {
			return new ArrayList<>();
		}
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

	public Integer getTotalNumberOfRows(String searchPhrase, Boolean active, String sortId, String sortCode, String sortDescription, String sortGtin, String sortIsCold, String sortIsActive) {
		Criteria criteria = this.buildCriteria(searchPhrase, active, sortId, sortCode, sortDescription, sortGtin, sortIsCold, sortIsActive);
		criteria.setProjection(Projections.countDistinct("id"));
		Long count = (Long) criteria.uniqueResult();
		return count.intValue();
	}
}
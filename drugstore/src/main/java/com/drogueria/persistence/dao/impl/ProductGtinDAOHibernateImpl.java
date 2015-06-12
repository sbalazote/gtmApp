package com.drogueria.persistence.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.ProductGtin;
import com.drogueria.persistence.dao.ProductGtinDAO;

@Repository
public class ProductGtinDAOHibernateImpl implements ProductGtinDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(ProductGtin productGtin) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(productGtin);
	}

	@Override
	public ProductGtin get(Integer id) {
		return (ProductGtin) this.sessionFactory.getCurrentSession().get(ProductGtin.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductGtin> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(ProductGtin.class).list();
	}

	@Override
	public ProductGtin getByNumber(String number) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from ProductGtin where number = :number");
		query.setParameter("number", number);
		if (query.list().isEmpty()) {
			return null;
		} else {
			return (ProductGtin) query.list().get(0);
		}
	}

	@Override
	public boolean isGtinUsed(String number) {
		String gtinInOrderDetailsSentence = "select count(*) from product_gtin as pg, order_detail as od where od.gtin_id = pg.id and pg.number = " + number;
		Query gtinInOrderDetailsQuery = this.sessionFactory.getCurrentSession().createSQLQuery(gtinInOrderDetailsSentence);
		int gtinInOrderDetailsQty = ((Number) gtinInOrderDetailsQuery.uniqueResult()).intValue();
		if (gtinInOrderDetailsQty > 0) {
			return true;
		}

		String gtinInOutputDetailsSentence = "select count(*) from product_gtin as pg, output_detail as od where od.gtin_id = pg.id and pg.number = " + number;
		Query gtinInOutputDetailsQuery = this.sessionFactory.getCurrentSession().createSQLQuery(gtinInOutputDetailsSentence);
		int gtinInOutputDetailsQty = ((Number) gtinInOutputDetailsQuery.uniqueResult()).intValue();
		if (gtinInOutputDetailsQty > 0) {
			return true;
		}

		String gtinInSupplyingDetailsSentence = "select count(*) from product_gtin as pg, supplying_detail as sd where sd.gtin_id = pg.id and pg.number = "
				+ number;
		Query gtinInSupplyingDetailsQuery = this.sessionFactory.getCurrentSession().createSQLQuery(gtinInSupplyingDetailsSentence);
		int gtinInSupplyingDetailsQty = ((Number) gtinInSupplyingDetailsQuery.uniqueResult()).intValue();
		if (gtinInSupplyingDetailsQty > 0) {
			return true;
		}

		String gtinInInputDetailsSentence = "select count(*) from product_gtin as pg, input_detail as id where id.gtin_id = pg.id and pg.number = " + number;
		Query gtinInInputDetailsQuery = this.sessionFactory.getCurrentSession().createSQLQuery(gtinInInputDetailsSentence);
		int gtinInInputDetailsQty = ((Number) gtinInInputDetailsQuery.uniqueResult()).intValue();
		if (gtinInInputDetailsQty > 0) {
			return true;
		}

		return false;
	}
}

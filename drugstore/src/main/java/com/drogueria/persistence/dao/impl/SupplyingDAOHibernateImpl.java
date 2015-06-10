package com.drogueria.persistence.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.drogueria.constant.Constants;
import com.drogueria.model.Supplying;
import com.drogueria.persistence.dao.SupplyingDAO;
import com.drogueria.query.SupplyingQuery;

@Repository
public class SupplyingDAOHibernateImpl implements SupplyingDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Supplying supplying) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(supplying);
	}

	@Override
	public Supplying get(Integer id) {
		return (Supplying) this.sessionFactory.getCurrentSession().get(Supplying.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Supplying> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(Supplying.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Supplying> getCancelleables() {
		Query query;

		query = this.sessionFactory
				.getCurrentSession()
				.createSQLQuery(
						"select * from supplying as s where not exists (select * from supplying_detail as sd, delivery_note_detail as dnd, delivery_note dn where sd.id = dnd.supplying_detail_id and dn.id = dnd.delivery_note_id and s.id = sd.supplying_id and dn.cancelled = 0) and s.cancelled = 0");

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Supplying> getSupplyingForSearch(SupplyingQuery supplyingQuery) {

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Supplying.class);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Date dateFromFormated = null;
		Date dateToFormated = null;

		if (supplyingQuery.getId() != null) {
			criteria.add(Restrictions.eq("id", supplyingQuery.getId()));
		}

		if (!StringUtils.isEmpty(supplyingQuery.getDateFrom())) {
			try {
				dateFromFormated = dateFormatter.parse(supplyingQuery.getDateFrom());
				criteria.add(Restrictions.ge("date", dateFromFormated));
			} catch (ParseException e) {
				throw new RuntimeException("El formato de la fecha ingresada no es valido.", e);
			}
		}
		if (!StringUtils.isEmpty(supplyingQuery.getDateTo())) {
			try {
				dateToFormated = dateFormatter.parse(supplyingQuery.getDateTo());
				criteria.add(Restrictions.le("date", dateToFormated));
			} catch (ParseException e) {
				throw new RuntimeException("El formato de la fecha ingresada no es valido.", e);
			}
		}

		if (supplyingQuery.getAgreementId() != null) {
			criteria.add(Restrictions.eq("agreement.id", supplyingQuery.getAgreementId()));
		}

        if (supplyingQuery.getAffiliateId() != null) {
            criteria.add(Restrictions.eq("affiliate.id", supplyingQuery.getAffiliateId()));
        }

        if(supplyingQuery.getCancelled() == true) {
            criteria.add(Restrictions.eq("cancelled",supplyingQuery.getCancelled()));
        }

        criteria.addOrder(Order.desc("id"));

		List<Supplying> results = criteria.list();

		return results;
	}

	@Override
	public boolean getCountSupplyingSearch(SupplyingQuery supplyingQuery) {
		if (this.getSupplyingForSearch(supplyingQuery).size() < Constants.QUERY_MAX_RESULTS) {
			return true;
		} else {
			return false;
		}
	}
}

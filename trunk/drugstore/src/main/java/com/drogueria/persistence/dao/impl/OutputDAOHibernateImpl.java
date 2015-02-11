package com.drogueria.persistence.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.drogueria.constant.Constants;
import com.drogueria.model.Output;
import com.drogueria.persistence.dao.OutputDAO;
import com.drogueria.query.OutputQuery;

@Repository
public class OutputDAOHibernateImpl implements OutputDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Output output) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(output);
	}

	@Override
	public Output get(Integer id) {
		return (Output) this.sessionFactory.getCurrentSession().get(Output.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Output> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(Output.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Output> getOutputForSearch(OutputQuery outputQuery) {

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Output.class);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Date dateFromFormated = null;
		Date dateToFormated = null;

		if (outputQuery.getId() != null) {
			criteria.add(Restrictions.eq("id", outputQuery.getId()));
		}

		if (!StringUtils.isEmpty(outputQuery.getDateFrom())) {
			try {
				dateFromFormated = dateFormatter.parse(outputQuery.getDateFrom());
				criteria.add(Restrictions.ge("date", dateFromFormated));
			} catch (ParseException e) {
				throw new RuntimeException("El formato de la fecha ingresada no es valido.", e);
			}
		}
		if (!StringUtils.isEmpty(outputQuery.getDateTo())) {
			try {
				dateToFormated = dateFormatter.parse(outputQuery.getDateTo());
				criteria.add(Restrictions.le("date", dateToFormated));
			} catch (ParseException e) {
				throw new RuntimeException("El formato de la fecha ingresada no es valido.", e);
			}
		}

		if (outputQuery.getConceptId() != null) {
			criteria.add(Restrictions.eq("concept.id", outputQuery.getConceptId()));
		}

		if (outputQuery.getProviderId() != null) {
			criteria.add(Restrictions.eq("provider.id", outputQuery.getProviderId()));
		}

		if (outputQuery.getDeliveryLocationId() != null) {
			criteria.add(Restrictions.eq("deliveryLocation.id", outputQuery.getDeliveryLocationId()));
		}

		if (outputQuery.getAgreementId() != null) {
			criteria.add(Restrictions.eq("agreement.id", outputQuery.getAgreementId()));
		}

		List<Output> results = criteria.list();

		return results;
	}

	@Override
	public boolean getCountOutputSearch(OutputQuery outputQuery) {
		if (this.getOutputForSearch(outputQuery).size() < Constants.QUERY_MAX_RESULTS) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean existSerial(Integer productId, String serial) {
		try {
			Query query = this.sessionFactory
					.getCurrentSession()
					.createQuery(
							"from Output as o inner join o.outputDetails as od where o.cancelled = false and od.serialNumber = :serialNumber and od.product.id = :productId ");
			query.setParameter("serialNumber", serial);
			query.setParameter("productId", productId);
			return !query.list().isEmpty();
		} catch (IndexOutOfBoundsException e) {
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Output> getCancelleables() {
		Query query;

		query = this.sessionFactory
				.getCurrentSession()
				.createSQLQuery(
						"select * from output as o where not exists (select * from output_detail as od, delivery_note_detail as dnd, delivery_note dn where od.id = dnd.output_detail_id and dn.id = dnd.delivery_note_id and o.id = od.output_id and dn.cancelled = 0) and o.cancelled = 0")
				.addEntity(Output.class);
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> getAllHasToPrint() {
		Query query;

		query = this.sessionFactory
				.getCurrentSession()
				.createSQLQuery(
						"select o.id from output as o, concept as c where not exists (select * from output_detail as od, delivery_note_detail as dnd, delivery_note dn where od.id = dnd.output_detail_id and dn.id = dnd.delivery_note_id and o.id = od.output_id and dn.cancelled = 0) and o.cancelled = 0 and o.concept_id = c.id and c.print_delivery_note = false");
		return query.list();
	}
}

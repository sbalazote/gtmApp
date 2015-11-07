package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.constant.Constants;
import com.lsntsolutions.gtmApp.model.Output;
import com.lsntsolutions.gtmApp.persistence.dao.OutputDAO;
import com.lsntsolutions.gtmApp.query.OutputQuery;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Output.class, "output");
		criteria.createAlias("output.outputDetails", "outputDetail");
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
		if (outputQuery.getProductId() != null) {
			criteria.add(Restrictions.eq("outputDetail.product.id", outputQuery.getProductId()));
		}
		if (outputQuery.getCancelled() != null) {
			criteria.add(Restrictions.eq("cancelled", outputQuery.getCancelled()));
		}
        criteria.addOrder(Order.desc("id")).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List<Output> results = criteria.list();

		return results;
	}

	@Override
	public boolean getCountOutputSearch(OutputQuery outputQuery) {
		return this.getOutputForSearch(outputQuery).size() < Constants.QUERY_MAX_RESULTS;
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
	public boolean isConceptInUse(Integer conceptId){
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Output where concept.id = :conceptId");
		query.setParameter("conceptId", conceptId);
		return !query.list().isEmpty();
	}
}

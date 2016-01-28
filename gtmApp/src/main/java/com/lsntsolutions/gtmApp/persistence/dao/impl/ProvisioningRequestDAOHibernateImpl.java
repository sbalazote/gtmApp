package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.constant.Constants;
import com.lsntsolutions.gtmApp.constant.State;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.persistence.dao.ProvisioningRequestDAO;
import com.lsntsolutions.gtmApp.query.ProvisioningQuery;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
public class ProvisioningRequestDAOHibernateImpl implements ProvisioningRequestDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(ProvisioningRequest provisioningRequest) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(provisioningRequest);
		this.sessionFactory.getCurrentSession().flush();
	}

	@Override
	public ProvisioningRequest get(Integer id) {
		return (ProvisioningRequest) this.sessionFactory.getCurrentSession().get(ProvisioningRequest.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProvisioningRequest> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(ProvisioningRequest.class).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProvisioningRequest> getAllByState(Integer stateId) {
		Query query;
		query = this.sessionFactory.getCurrentSession().createQuery("from ProvisioningRequest where (state.id = :stateId)");
		query.setParameter("stateId", stateId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProvisioningRequest> getProvisioningForSearch(ProvisioningQuery provisioningQuery) {

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(ProvisioningRequest.class, "provisioning");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		criteria.createAlias("provisioning.provisioningRequestDetails", "detail");
		criteria.createAlias("detail.product", "product");
		Date dateFromFormated;
		Date dateToFormated;

		if (provisioningQuery.getProvisioningId() != null) {
			criteria.add(Restrictions.eq("id", provisioningQuery.getProvisioningId()));
		}

		if (!StringUtils.isEmpty(provisioningQuery.getDateFrom())) {
			try {
				dateFromFormated = dateFormatter.parse(provisioningQuery.getDateFrom());
				criteria.add(Restrictions.ge("deliveryDate", dateFromFormated));
			} catch (ParseException e) {
				throw new RuntimeException("El formato de la fecha ingresada no es valido.", e);
			}
		}
		if (!StringUtils.isEmpty(provisioningQuery.getDateTo())) {
			try {
				dateToFormated = dateFormatter.parse(provisioningQuery.getDateTo());
				criteria.add(Restrictions.le("deliveryDate", dateToFormated));
			} catch (ParseException e) {
				throw new RuntimeException("El formato de la fecha ingresada no es valido.", e);
			}
		}

		if (provisioningQuery.getAgreementId() != null) {
			criteria.add(Restrictions.eq("agreement.id", provisioningQuery.getAgreementId()));
		}

		if (provisioningQuery.getAffiliateId() != null) {
			criteria.add(Restrictions.eq("affiliate.id", provisioningQuery.getAffiliateId()));
		}

		if (provisioningQuery.getClientId() != null) {
			criteria.add(Restrictions.eq("client.id", provisioningQuery.getClientId()));
		}

		if (provisioningQuery.getLogisticsOperator() != null) {
			criteria.add(Restrictions.eq("logisticsOperator.id", provisioningQuery.getLogisticsOperator()));
		}

		if (provisioningQuery.getDeliveryLocation() != null) {
			criteria.add(Restrictions.eq("deliveryLocation.id", provisioningQuery.getDeliveryLocation()));
		}

		if (provisioningQuery.getStateId() != null) {
			criteria.add(Restrictions.eq("state.id", provisioningQuery.getStateId()));
		}
		if (provisioningQuery.getProductId() != null) {
			criteria.add(Restrictions.eq("product.id", provisioningQuery.getProductId()));
		}
		if (provisioningQuery.getProductMonodrugId() != null) {
			criteria.add(Restrictions.eq("product.monodrug.id", provisioningQuery.getProductMonodrugId()));
		}
		//criteria.setProjection(Projections.distinct(Projections.property("provisioning.id")));
//criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<ProvisioningRequest> results = criteria.list();

		return results;
	}

	@Override
	public boolean getCountOfProvisioningSearch(ProvisioningQuery provisioningQuery) {
		return this.getProvisioningForSearch(provisioningQuery).size() < Constants.QUERY_MAX_RESULTS;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProvisioningRequest> getFilterProvisionings(Integer provisioningRequestId, Integer agreementId, Integer logisticsOperatorId, Integer clientId, Integer deliveryLocationId, Integer stateId) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(ProvisioningRequest.class);
		if (provisioningRequestId != null) {
			criteria.add(Restrictions.eq("id", provisioningRequestId));
		}
		if (agreementId != null) {
			criteria.add(Restrictions.eq("agreement.id", agreementId));
		}
		if (logisticsOperatorId != null) {
			criteria.add(Restrictions.eq("logisticsOperator.id", logisticsOperatorId));
		}
		if (clientId != null) {
			criteria.add(Restrictions.eq("client.id", clientId));
		}
		if (deliveryLocationId != null) {
			criteria.add(Restrictions.eq("deliveryLocation.id", deliveryLocationId));
		}
		criteria.add(Restrictions.eq("state.id", stateId));

		return criteria.list();
	}

	@Override
	public List<Agreement> getProvisioningsAgreement(boolean provisioningRequireAuthorization) {
		String sentence = "select distinct p.agreement from ProvisioningRequest as p where p.state.id = :stateId";
		if(!provisioningRequireAuthorization){
			sentence = " or p.state.id = " + State.ENTERED.getId();
		}
		Query query;
		query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("stateId", State.AUTHORIZED.getId());
		return query.list();
	}

	@Override
	public List<DeliveryLocation> getProvisioningsDeliveryLocations(boolean provisioningRequireAuthorization) {
		String sentence = "select distinct p.deliveryLocation from ProvisioningRequest as p where p.state.id = :stateId";
		if(!provisioningRequireAuthorization){
			sentence = " or p.state.id = " + State.ENTERED.getId();
		}
		Query query;
		query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("stateId", State.AUTHORIZED.getId());
		return query.list();
	}

	@Override
	public List<LogisticsOperator> getProvisioningsLogisticsOperators(boolean provisioningRequireAuthorization) {
		String sentence = "select distinct p.logisticsOperator from ProvisioningRequest as p where p.logisticsOperator IS NOT NULL and p.state.id = :stateId";
		if(!provisioningRequireAuthorization){
			sentence = " or p.state.id = " + State.ENTERED.getId();
		}
		Query query;
		query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("stateId", State.AUTHORIZED.getId());
		return query.list();
	}

	@Override
	public List<Client> getProvisioningsClient(boolean provisioningRequireAuthorization) {
		String sentence = "select distinct p.client from ProvisioningRequest as p where p.state.id = :stateId";
		if(!provisioningRequireAuthorization){
			sentence = " or p.state.id = " + State.ENTERED.getId();
		}
		Query query;
		query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("stateId", State.AUTHORIZED.getId());
		return query.list();
	}
}
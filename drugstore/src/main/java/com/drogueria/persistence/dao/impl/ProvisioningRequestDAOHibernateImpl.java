package com.drogueria.persistence.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.drogueria.constant.Constants;
import com.drogueria.model.ProvisioningRequest;
import com.drogueria.persistence.dao.ProvisioningRequestDAO;
import com.drogueria.query.ProvisioningQuery;

@Repository
public class ProvisioningRequestDAOHibernateImpl implements ProvisioningRequestDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(ProvisioningRequest provisioningRequest) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(provisioningRequest);
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

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(ProvisioningRequest.class);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Date dateFromFormated = null;
		Date dateToFormated = null;

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

		if (!StringUtils.isEmpty(provisioningQuery.getComment())) {
			criteria.add(Restrictions.like("comment", provisioningQuery.getComment(), MatchMode.ANYWHERE));
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

		List<ProvisioningRequest> results = criteria.list();

		return results;
	}

	@Override
	public boolean getCountOfProvisioningSearch(ProvisioningQuery provisioningQuery) {
		if (this.getProvisioningForSearch(provisioningQuery).size() < Constants.QUERY_MAX_RESULTS) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProvisioningRequest> getFilterProvisionings(Integer agreementId, Integer clientId, Integer stateId) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(ProvisioningRequest.class);
		if (agreementId != null) {
			criteria.add(Restrictions.eq("agreement.id", agreementId));
		}
		if (clientId != null) {
			criteria.add(Restrictions.eq("client.id", clientId));
		}
		criteria.add(Restrictions.eq("state.id", stateId));

		return criteria.list();
	}
}
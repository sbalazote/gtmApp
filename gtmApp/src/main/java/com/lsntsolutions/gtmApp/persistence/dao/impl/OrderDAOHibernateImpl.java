package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.constant.Constants;
import com.lsntsolutions.gtmApp.constant.State;
import com.lsntsolutions.gtmApp.model.Agreement;
import com.lsntsolutions.gtmApp.model.Client;
import com.lsntsolutions.gtmApp.model.DeliveryLocation;
import com.lsntsolutions.gtmApp.model.Order;
import com.lsntsolutions.gtmApp.persistence.dao.OrderDAO;
import com.lsntsolutions.gtmApp.query.DeliveryNoteQuery;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class OrderDAOHibernateImpl implements OrderDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Order order) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(order);
	}

	@Override
	public Order get(Integer id) {
		return (Order) this.sessionFactory.getCurrentSession().get(Order.class, id);
	}

	@Override
	public Order getOrderByProvisioningRequestId(Integer provisioningRequestId) {
		try {
			Query query = this.sessionFactory.getCurrentSession().createQuery("from Order where provisioning_request_id = :provisioningRequestId");
			query.setParameter("provisioningRequestId", provisioningRequestId);
			return (Order) query.list().get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order> getDeliveryNoteSearch(DeliveryNoteQuery deliveryNoteQuery) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Order.class);

		if (deliveryNoteQuery.getAffiliateId() != null) {
			criteria.add(Restrictions.eq("provisioningRequest.affiliate.id", deliveryNoteQuery.getAffiliateId()));
		}

		if (!StringUtils.isEmpty(deliveryNoteQuery.getDeliveryNoteNumber())) {
			criteria.add(Restrictions.eq("deliveryNoteNumber", deliveryNoteQuery.getDeliveryNoteNumber()));
		}

		if (deliveryNoteQuery.getProvisioningRequestId() != null) {
			criteria.add(Restrictions.eq("provisioningRequest.id", deliveryNoteQuery.getProvisioningRequestId()));
		}

		List<Order> results = criteria.list();

		return results;
	}

	@Override
	public boolean getCountDeliveryNoteSearch(DeliveryNoteQuery deliveryNoteQuery) {
		return this.getDeliveryNoteSearch(deliveryNoteQuery).size() < Constants.QUERY_MAX_RESULTS;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Order> getAllByState(Integer stateId) {
		Query query;
		query = this.sessionFactory.getCurrentSession().createQuery("from Order where (provisioningRequest.state.id = :stateId and cancelled = false)");
		query.setParameter("stateId", stateId);
		return query.list();
	}

	@Override
	public boolean existSerial(Integer productId, String serial) {
		try {
			Query query = this.sessionFactory
					.getCurrentSession()
					.createQuery(
							"from Order as o inner join o.orderDetails as od where o.cancelled = false and od.serialNumber = :serialNumber and od.product.id = :productId ");
			query.setParameter("serialNumber", serial);
			query.setParameter("productId", productId);
			return !query.list().isEmpty();
		} catch (IndexOutOfBoundsException e) {
			return true;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Order> getAllFilter(Integer provisioningRequestId, Integer agreementId, Integer logisticsOperatorId, Integer clientId, Integer deliveryLocationId, Integer stateId) {
		String sentence = "from Order where (provisioningRequest.state.id = :stateId and ";
		if (provisioningRequestId != null) {
			sentence += "provisioningRequest.id =:provisioningRequestId and ";
		}
		if (agreementId != null) {
			sentence += "provisioningRequest.agreement.id =:agreementId and ";
		}
		if (logisticsOperatorId != null) {
			sentence += "provisioningRequest.logisticsOperator.id =:logisticsOperatorId and ";
		}
		if (clientId != null) {
			sentence += "provisioningRequest.client.id =:clientId and ";
		}
		if (deliveryLocationId != null) {
			sentence += "provisioningRequest.deliveryLocation.id =:deliveryLocationId and ";
		}
		sentence += "cancelled = false)";
		Query query;
		query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("stateId", stateId);

		if (provisioningRequestId != null) {
			query.setParameter("provisioningRequestId", provisioningRequestId);
		}
		if (agreementId != null) {
			query.setParameter("agreementId", agreementId);
		}
		if (logisticsOperatorId != null) {
			query.setParameter("logisticsOperatorId", logisticsOperatorId);
		}
		if (clientId != null) {
			query.setParameter("clientId", clientId);
		}
		if (deliveryLocationId != null) {
			query.setParameter("deliveryLocationId", deliveryLocationId);
		}

		return query.list();
	}

	@Override
	public List<Agreement> getAgreementForOrderToPrint() {
		Query query;
		query = this.sessionFactory.getCurrentSession().createQuery("select distinct o.provisioningRequest.agreement from Order as o where o.provisioningRequest.state.id = :stateId");
		query.setParameter("stateId", State.ASSEMBLED.getId());
		return query.list();
	}

	@Override
	public List<Client> getClientForOrderToPrint() {
		Query query;
		query = this.sessionFactory.getCurrentSession().createQuery("select distinct o.provisioningRequest.client from Order as o where o.provisioningRequest.state.id = :stateId");
		query.setParameter("stateId", State.ASSEMBLED.getId());
		return query.list();
	}

	@Override
	public List<DeliveryLocation> getDeliveryLocationsForOrderToPrint() {
		Query query;
		query = this.sessionFactory.getCurrentSession().createQuery("select distinct o.provisioningRequest.deliveryLocation from Order as o where o.provisioningRequest.state.id = :stateId");
		query.setParameter("stateId", State.ASSEMBLED.getId());
		return query.list();
	}
}

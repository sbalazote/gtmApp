package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.constant.Constants;
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
	public List<Order> getAllFilter(Integer agreementId, Integer clientId, Integer stateId) {
		String sentence = "from Order where (provisioningRequest.state.id = :stateId and ";
		if (agreementId != null) {
			sentence += "provisioningRequest.agreement.id =:agreementId and ";
		}
		if (clientId != null) {
			sentence += "provisioningRequest.client.id =:clientId and ";
		}
		sentence += "cancelled = false)";
		Query query;
		query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("stateId", stateId);

		if (agreementId != null) {
			query.setParameter("agreementId", agreementId);
		}
		if (clientId != null) {
			query.setParameter("clientId", clientId);
		}

		return query.list();
	}
}

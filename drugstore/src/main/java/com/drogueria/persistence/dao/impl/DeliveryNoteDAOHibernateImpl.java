package com.drogueria.persistence.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drogueria.model.DeliveryNote;
import com.drogueria.model.Order;
import com.drogueria.model.Output;
import com.drogueria.persistence.dao.DeliveryNoteDAO;
import com.drogueria.query.DeliveryNoteQuery;
import com.drogueria.service.OrderService;
import com.drogueria.service.OutputService;

@Repository
public class DeliveryNoteDAOHibernateImpl implements DeliveryNoteDAO {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private OutputService outputService;
	@Autowired
	private OrderService orderService;

	@Override
	public DeliveryNote get(Integer id) {
		return (DeliveryNote) this.sessionFactory.getCurrentSession().get(DeliveryNote.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryNote> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(DeliveryNote.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, List<String>> getAssociatedOrders(boolean informAnmat) {
		Map<Integer, List<String>> associatedOrders = new HashMap<Integer, List<String>>();
		Query query;
		String sentence = "select distinct od.order_id, dn.number from order_detail as od, delivery_note_detail as dnd, delivery_note dn where od.id = dnd.order_detail_id and dn.id = dnd.delivery_note_id and dn.cancelled = 0";
		if (informAnmat) {
			sentence += " and dn.inform_anmat = 1 and dn.informed = 0";
		}
		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		Iterator<Object[]> it = query.list().iterator();
		while (it.hasNext()) {
			Object[] orderDeliveryNotePair = it.next();
			Integer orderId = (Integer) orderDeliveryNotePair[0];
			String deliveryNoteNumber = (String) orderDeliveryNotePair[1];
			List<String> deliveryNoteNumbers = null;
			if (!associatedOrders.containsKey(orderId)) {
				deliveryNoteNumbers = new ArrayList<String>();
				deliveryNoteNumbers.add(deliveryNoteNumber);
				associatedOrders.put(orderId, deliveryNoteNumbers);
			} else {
				deliveryNoteNumbers = associatedOrders.get(orderId);
				deliveryNoteNumbers.add(deliveryNoteNumber);
				associatedOrders.put(orderId, deliveryNoteNumbers);
			}
		}
		return associatedOrders;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, List<String>> getAssociatedOutputs(boolean informAnmat) {
		Map<Integer, List<String>> associatedOutputs = new HashMap<Integer, List<String>>();
		String sentence = "select distinct od.output_id, dn.number from output_detail as od, delivery_note_detail as dnd, delivery_note dn where od.id = dnd.output_detail_id and dn.id = dnd.delivery_note_id and dn.cancelled = 0";
		if (informAnmat) {
			sentence += " and dn.inform_anmat = 1 and dn.informed = 0";
		}
		Query query;
		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);

		Iterator<Object[]> it = query.list().iterator();
		while (it.hasNext()) {
			Object[] outputDeliveryNotePair = it.next();
			Integer outputId = (Integer) outputDeliveryNotePair[0];
			String deliveryNoteNumber = (String) outputDeliveryNotePair[1];
			List<String> deliveryNotes = null;
			if (!associatedOutputs.containsKey(outputId)) {
				deliveryNotes = new ArrayList<String>();
				deliveryNotes.add(deliveryNoteNumber);
				associatedOutputs.put(outputId, deliveryNotes);
			} else {
				deliveryNotes = associatedOutputs.get(outputId);
				deliveryNotes.add(deliveryNoteNumber);
				associatedOutputs.put(outputId, deliveryNotes);
			}
		}
		return associatedOutputs;
	}

	@Override
	public void save(DeliveryNote deliveryNote) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(deliveryNote);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryNote> getDeliveryNoteFromOrderForSearch(DeliveryNoteQuery deliveryNoteQuery) {
		try {
			String sentence = "select dn.*, dnd.* from order_detail as od, delivery_note_detail as dnd, delivery_note as dn,`order` as o, provisioning_request as p where od.id = dnd.order_detail_id and dn.id = dnd.delivery_note_id and od.order_id = o.id and o.provisioning_request_id = p.id";

			if (deliveryNoteQuery.getAffiliateId() != null) {
				sentence += " and p.affiliate_id = " + deliveryNoteQuery.getAffiliateId();
			}
			if (deliveryNoteQuery.getProvisioningRequestId() != null) {
				sentence += " and p.id = " + deliveryNoteQuery.getProvisioningRequestId();
			}
			if (!deliveryNoteQuery.getDeliveryNoteNumber().equals("")) {
				sentence += " and dn.number = " + deliveryNoteQuery.getDeliveryNoteNumber();
			}

			Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence).addEntity("dn", DeliveryNote.class)
					.addJoin("dnd", "dn.deliveryNoteDetails").addEntity("dn", DeliveryNote.class)
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			return query.list();
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryNote> getDeliveryNoteFromOutputForSearch(DeliveryNoteQuery deliveryNoteQuery) {
		try {
			String sentence = "select dn.*, dnd.* from output_detail as od, delivery_note_detail as dnd, delivery_note as dn,output as o where od.id = dnd.output_detail_id and dn.id = dnd.delivery_note_id and od.output_id = o.id";

			if (deliveryNoteQuery.getDeliveryLocationId() != null) {
				sentence += " and o.delivery_location_id = " + deliveryNoteQuery.getDeliveryLocationId();
			}
			if (deliveryNoteQuery.getAgreementId() != null) {
				sentence += " and o.agreement_id = " + deliveryNoteQuery.getAgreementId();
			}
			if (deliveryNoteQuery.getProviderId() != null) {
				sentence += " and o.provider_id = " + deliveryNoteQuery.getProviderId();
			}
			if (!deliveryNoteQuery.getDeliveryNoteNumber().equals("")) {
				sentence += " and dn.number = " + deliveryNoteQuery.getDeliveryNoteNumber();
			}

			Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence).addEntity("dn", DeliveryNote.class)
					.addJoin("dnd", "dn.deliveryNoteDetails").addEntity("dn", DeliveryNote.class)
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			return query.list();
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public Order gerOrder(DeliveryNote deliveryNote) {
		try {
			String sentence = "select o.id from order_detail as od, delivery_note_detail as dnd, delivery_note as dn,`order` as o where od.id = dnd.order_detail_id and dn.id = dnd.delivery_note_id and od.order_id = o.id and dnd.delivery_note_id = "
					+ deliveryNote.getId();
			Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
			return this.orderService.get((Integer) (query.list().get(0)));
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public Output gerOutput(DeliveryNote deliveryNote) {
		try {
			String sentence = "select o.id from output_detail as od, delivery_note_detail as dnd, delivery_note as dn, output as o where od.id = dnd.output_detail_id and dn.id = dnd.delivery_note_id and od.output_id = o.id and dnd.delivery_note_id = "
					+ deliveryNote.getId();

			Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
			return this.outputService.get((Integer) (query.list().get(0)));
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public DeliveryNote getDeliveryNoteFromNumber(String deliveryNoteNumber) {
		try {
			Query query = this.sessionFactory.getCurrentSession().createQuery("from DeliveryNote where number = :deliveryNoteNumber");
			query.setParameter("deliveryNoteNumber", deliveryNoteNumber);
			return (DeliveryNote) query.list().get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

}
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
import com.drogueria.model.Supplying;
import com.drogueria.persistence.dao.DeliveryNoteDAO;
import com.drogueria.query.DeliveryNoteQuery;
import com.drogueria.service.OrderService;
import com.drogueria.service.OutputService;
import com.drogueria.service.SupplyingService;

@Repository
public class DeliveryNoteDAOHibernateImpl implements DeliveryNoteDAO {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private OutputService outputService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private SupplyingService supplyingService;

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

	@Override
	public Map<Integer, List<DeliveryNote>> getAssociatedOrders() {
		return null;
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

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, List<DeliveryNote>> getAssociatedOutputs() {
		Map<Integer, List<DeliveryNote>> associatedOutputs = new HashMap<Integer, List<DeliveryNote>>();
		String sentence = "select distinct od.output_id, dn from output_detail as od, delivery_note_detail as dnd, delivery_note dn where od.id = dnd.output_detail_id and dn.id = dnd.delivery_note_id and dn.cancelled = 0";

		Query query;
		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);

		Iterator<Object[]> it = query.list().iterator();
		while (it.hasNext()) {
			Object[] outputDeliveryNotePair = it.next();
			Integer outputId = (Integer) outputDeliveryNotePair[0];
			DeliveryNote deliveryNote = (DeliveryNote) outputDeliveryNotePair[1];
			List<DeliveryNote> deliveryNotes = null;
			if (!associatedOutputs.containsKey(outputId)) {
				deliveryNotes = new ArrayList<DeliveryNote>();
				deliveryNotes.add(deliveryNote);
				associatedOutputs.put(outputId, deliveryNotes);
			} else {
				deliveryNotes = associatedOutputs.get(outputId);
				deliveryNotes.add(deliveryNote);
				associatedOutputs.put(outputId, deliveryNotes);
			}
		}
		return associatedOutputs;
	}

	@Override
	public Map<Integer, List<String>> getAssociatedSupplyings(boolean informAnmat) {
		Map<Integer, List<String>> associatedSupplyings = new HashMap<Integer, List<String>>();
		String sentence = "select distinct sd.supplying_id, dn.number from supplying_detail as sd, delivery_note_detail as dnd, delivery_note dn where sd.id = dnd.supplying_detail_id and dn.id = dnd.delivery_note_id and dn.cancelled = 0";
		if (informAnmat) {
			sentence += " and dn.inform_anmat = 1 and dn.informed = 0";
		}
		Query query;
		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);

		Iterator<Object[]> it = query.list().iterator();
		while (it.hasNext()) {
			Object[] supplyingDeliveryNotePair = it.next();
			Integer supplyingId = (Integer) supplyingDeliveryNotePair[0];
			String deliveryNoteNumber = (String) supplyingDeliveryNotePair[1];
			List<String> deliveryNotes = null;
			if (!associatedSupplyings.containsKey(supplyingId)) {
				deliveryNotes = new ArrayList<String>();
				deliveryNotes.add(deliveryNoteNumber);
				associatedSupplyings.put(supplyingId, deliveryNotes);
			} else {
				deliveryNotes = associatedSupplyings.get(supplyingId);
				deliveryNotes.add(deliveryNoteNumber);
				associatedSupplyings.put(supplyingId, deliveryNotes);
			}
		}
		return associatedSupplyings;
	}

	@Override
	public Map<Integer, List<DeliveryNote>> getAssociatedSupplyings() {
		Map<Integer, List<DeliveryNote>> associatedSupplyings = new HashMap<Integer, List<DeliveryNote>>();
		String sentence = "select distinct sd.supplying_id, dn from supplying_detail as sd, delivery_note_detail as dnd, delivery_note dn where sd.id = dnd.supplying_detail_id and dn.id = dnd.delivery_note_id and dn.cancelled = 0";

		Query query;
		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);

		Iterator<Object[]> it = query.list().iterator();
		while (it.hasNext()) {
			Object[] supplyingDeliveryNotePair = it.next();
			Integer supplyingId = (Integer) supplyingDeliveryNotePair[0];
			DeliveryNote deliveryNote = (DeliveryNote) supplyingDeliveryNotePair[1];
			List<DeliveryNote> deliveryNotes = null;
			if (!associatedSupplyings.containsKey(supplyingId)) {
				deliveryNotes = new ArrayList<DeliveryNote>();
				deliveryNotes.add(deliveryNote);
				associatedSupplyings.put(supplyingId, deliveryNotes);
			} else {
				deliveryNotes = associatedSupplyings.get(supplyingId);
				deliveryNotes.add(deliveryNote);
				associatedSupplyings.put(supplyingId, deliveryNotes);
			}
		}
		return associatedSupplyings;
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
            sentence += " order by dn.id desc";
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
            sentence += " order by dn.id desc";
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
    public List<DeliveryNote> getDeliveryNoteFromSupplyingForSearch(DeliveryNoteQuery deliveryNoteQuery) {
        try {
            String sentence = "select dn.*, dnd.* from supplying_detail as sd, delivery_note_detail as dnd, delivery_note as dn,supplying as s where sd.id = dnd.supplying_detail_id and dn.id = dnd.delivery_note_id and sd.supplying_id = s.id";

            if (deliveryNoteQuery.getClientId() != null) {
                sentence += " and s.client_id = " + deliveryNoteQuery.getClientId();
            }
            if (!deliveryNoteQuery.getDeliveryNoteNumber().equals("")) {
                sentence += " and dn.number = " + deliveryNoteQuery.getDeliveryNoteNumber();
            }
            if (deliveryNoteQuery.getAgreementId() != null) {
                sentence += " and s.agreement_id = " + deliveryNoteQuery.getAgreementId();
            }
            sentence += " order by dn.id desc";
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
            if(query.list().get(0) != null) {
                return this.orderService.get((Integer) (query.list().get(0)));
            }else{
                return null;
            }
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
            if(query.list().get(0) != null) {
                return this.outputService.get((Integer) (query.list().get(0)));
            }else{
                return null;
            }
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

	@Override
	public Supplying getSupplying(DeliveryNote deliveryNote) {
		try {
			String sentence = "select distinct s.id from supplying_detail as sd, delivery_note_detail as dnd, delivery_note as dn, supplying as s where sd.id = dnd.supplying_detail_id and dn.id = dnd.delivery_note_id and sd.supplying_id = s.id and dnd.delivery_note_id = "
					+ deliveryNote.getId();

			Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
			return this.supplyingService.get((Integer) (query.list().get(0)));
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public List<String> getSupplyingsDeliveriesNoteNumbers(Integer supplyingId){
		try {
		String sentence = "select distinct dn.number from supplying_detail as sd, delivery_note_detail as dnd, delivery_note as dn, supplying as s where sd.id = dnd.supplying_detail_id and dn.id = dnd.delivery_note_id and sd.supplying_id = s.id and s.id ="
				+ supplyingId;
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		return query.list();
		} catch (IndexOutOfBoundsException e) {
            return null;
        }
	}


    @Override
    public List<String> getOutputsDeliveriesNoteNumbers(Integer outputId){
        try {
            String sentence = "select dn.number from output_detail as od, delivery_note_detail as dnd, delivery_note as dn, output as o where od.id = dnd.output_detail_id and dn.id = dnd.delivery_note_id and od.output_id = o.id and o.id ="
                    + outputId;
            Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
            return query.list();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}

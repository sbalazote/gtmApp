package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.model.Order;
import com.lsntsolutions.gtmApp.model.Output;
import com.lsntsolutions.gtmApp.model.Supplying;
import com.lsntsolutions.gtmApp.persistence.dao.DeliveryNoteDAO;
import com.lsntsolutions.gtmApp.query.DeliveryNoteQuery;
import com.lsntsolutions.gtmApp.service.OrderService;
import com.lsntsolutions.gtmApp.service.OutputService;
import com.lsntsolutions.gtmApp.service.SupplyingService;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.*;

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
	public Map<String, List<String>> getAssociatedOrders(boolean informAnmat, String deliveryNoteNumb) {
		Map<String, List<String>> associatedOrders = new HashMap<String, List<String>>();
		Query query;

		String sentence = "create temporary table if not exists associated_orders_temp1 as " +
				"(select distinct od.order_id, dn.number " +
				"from order_detail as od, delivery_note_detail as dnd, delivery_note dn where od.id = dnd.order_detail_id and dn.id = dnd.delivery_note_id and dn.cancelled = 0";

		if (informAnmat) {
			sentence += " and dn.inform_anmat = 1 and dn.informed = 0);";
		} else {
			sentence += ");";
		}

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		query.executeUpdate();

		sentence = "create temporary table if not exists associated_orders_temp2 as " +
				"(select distinct od.order_id, dn.number " +
				"from order_detail as od, delivery_note_detail as dnd, delivery_note dn where od.id = dnd.order_detail_id and dn.id = dnd.delivery_note_id and dn.cancelled = 0";

		if (informAnmat) {
			sentence += " and dn.inform_anmat = 1 and dn.informed = 0);";
		} else {
			sentence += ");";
		}

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		query.executeUpdate();

		sentence =	"select * from associated_orders_temp1 where order_id in (select order_id from associated_orders_temp2";

		if (!deliveryNoteNumb.isEmpty()) {
			sentence += " where number = '" + deliveryNoteNumb + "');";
		} else {
			sentence += ");";
		}

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		Iterator<Object[]> it = query.list().iterator();
		while (it.hasNext()) {
			Object[] orderDeliveryNotePair = it.next();
			Integer orderId = (Integer) orderDeliveryNotePair[0];
			String orderKey = "A".concat(orderId.toString());
			String deliveryNoteNumber = (String) orderDeliveryNotePair[1];
			List<String> deliveryNoteNumbers = null;
			if (!associatedOrders.containsKey(orderKey)) {
				deliveryNoteNumbers = new ArrayList<String>();
				deliveryNoteNumbers.add(deliveryNoteNumber);
				associatedOrders.put(orderKey, deliveryNoteNumbers);
			} else {
				deliveryNoteNumbers = associatedOrders.get(orderKey);
				deliveryNoteNumbers.add(deliveryNoteNumber);
				associatedOrders.put(orderKey, deliveryNoteNumbers);
			}
		}

		sentence = "drop temporary table if exists associated_orders_temp1;";
		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		query.executeUpdate();

		sentence = "drop temporary table if exists associated_orders_temp2;";
		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		query.executeUpdate();

		return associatedOrders;
	}

	@Override
	public Map<Integer, List<DeliveryNote>> getAssociatedOrders() {
		Map<Integer, List<DeliveryNote>> associatedOrders = new HashMap<Integer, List<DeliveryNote>>();
		Query query;
		String sentence = "select distinct od.order_id, dn.* from order_detail as od, delivery_note_detail as dnd, delivery_note dn where od.id = dnd.order_detail_id and dn.id = dnd.delivery_note_id";

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		Iterator<Object[]> it = query.list().iterator();
		while (it.hasNext()) {
			Object[] orderDeliveryNotePair = it.next();
			Integer orderId = (Integer) orderDeliveryNotePair[0];
			DeliveryNote deliveryNote = new DeliveryNote();
			deliveryNote.setId((Integer) orderDeliveryNotePair[1]);
			deliveryNote.setNumber((String) orderDeliveryNotePair[2]);
			deliveryNote.setDate((Date) orderDeliveryNotePair[3]);
			deliveryNote.setTransactionCodeANMAT((String) orderDeliveryNotePair[4]);
			deliveryNote.setCancelled((Boolean) orderDeliveryNotePair[5]);
			deliveryNote.setInformAnmat((Boolean) orderDeliveryNotePair[6]);
			deliveryNote.setInformed((Boolean) orderDeliveryNotePair[7]);
			deliveryNote.setFake((Boolean) orderDeliveryNotePair[8]);
			List<DeliveryNote> deliveryNoteNumbers = null;
			if (!associatedOrders.containsKey(orderId)) {
				deliveryNoteNumbers = new ArrayList<DeliveryNote>();
				deliveryNoteNumbers.add(deliveryNote);
				associatedOrders.put(orderId, deliveryNoteNumbers);
			} else {
				deliveryNoteNumbers = associatedOrders.get(orderId);
				deliveryNoteNumbers.add(deliveryNote);
				associatedOrders.put(orderId, deliveryNoteNumbers);
			}
		}
		return associatedOrders;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<String>> getAssociatedOutputs(boolean informAnmat, String deliveryNoteNumb) {
		Map<String, List<String>> associatedOutputs = new HashMap<String, List<String>>();
		Query query;

		String sentence = "create temporary table if not exists associated_outputs_temp1 as " +
				"(select distinct od.output_id, dn.number " +
				"from output_detail as od, delivery_note_detail as dnd, delivery_note dn where od.id = dnd.output_detail_id and dn.id = dnd.delivery_note_id and dn.cancelled = 0";

		if (informAnmat) {
			sentence += " and dn.inform_anmat = 1 and dn.informed = 0);";
		} else {
			sentence += ");";
		}

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		query.executeUpdate();

		sentence = "create temporary table if not exists associated_outputs_temp2 as " +
				"(select distinct od.output_id, dn.number " +
				"from output_detail as od, delivery_note_detail as dnd, delivery_note dn where od.id = dnd.output_detail_id and dn.id = dnd.delivery_note_id and dn.cancelled = 0";

		if (informAnmat) {
			sentence += " and dn.inform_anmat = 1 and dn.informed = 0);";
		} else {
			sentence += ");";
		}

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		query.executeUpdate();

		sentence =	"select * from associated_outputs_temp1 where output_id in (select output_id from associated_outputs_temp2";

		if (!deliveryNoteNumb.isEmpty()) {
			sentence += " where number = '" + deliveryNoteNumb + "');";
		} else {
			sentence += ");";
		}

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		Iterator<Object[]> it = query.list().iterator();
		while (it.hasNext()) {
			Object[] outputDeliveryNotePair = it.next();
			Integer outputId = (Integer) outputDeliveryNotePair[0];
			String outputKey = "E".concat(outputId.toString());
			String deliveryNoteNumber = (String) outputDeliveryNotePair[1];
			List<String> deliveryNotes = null;
			if (!associatedOutputs.containsKey(outputKey)) {
				deliveryNotes = new ArrayList<String>();
				deliveryNotes.add(deliveryNoteNumber);
				associatedOutputs.put(outputKey, deliveryNotes);
			} else {
				deliveryNotes = associatedOutputs.get(outputKey);
				deliveryNotes.add(deliveryNoteNumber);
				associatedOutputs.put(outputKey, deliveryNotes);
			}
		}

		sentence = "drop temporary table if exists associated_outputs_temp1;";
		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		query.executeUpdate();

		sentence = "drop temporary table if exists associated_outputs_temp2;";
		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		query.executeUpdate();

		return associatedOutputs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, List<DeliveryNote>> getAssociatedOutputs() {
		Map<Integer, List<DeliveryNote>> associatedOutputs = new HashMap<Integer, List<DeliveryNote>>();
		String sentence = "select distinct od.output_id, dn.* from output_detail as od, delivery_note_detail as dnd, delivery_note dn where od.id = dnd.output_detail_id and dn.id = dnd.delivery_note_id";

		Query query;
		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);

		Iterator<Object[]> it = query.list().iterator();
		while (it.hasNext()) {
			Object[] outputDeliveryNotePair = it.next();
			Integer outputId = (Integer) outputDeliveryNotePair[0];
			DeliveryNote deliveryNote = new DeliveryNote();
			deliveryNote.setId((Integer) outputDeliveryNotePair[1]);
			deliveryNote.setNumber((String) outputDeliveryNotePair[2]);
			deliveryNote.setDate((Date) outputDeliveryNotePair[3]);
			deliveryNote.setTransactionCodeANMAT((String) outputDeliveryNotePair[4]);
			deliveryNote.setCancelled((Boolean) outputDeliveryNotePair[5]);
			deliveryNote.setInformAnmat((Boolean) outputDeliveryNotePair[6]);
			deliveryNote.setInformed((Boolean) outputDeliveryNotePair[7]);
			deliveryNote.setFake((Boolean) outputDeliveryNotePair[8]);
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
	public Map<String, List<String>> getAssociatedSupplyings(boolean informAnmat, String deliveryNoteNumb) {
		Map<String, List<String>> associatedSupplyings = new HashMap<String, List<String>>();
		Query query;

		String sentence = "create temporary table if not exists associated_supplyings_temp1 as " +
				"(select distinct sd.supplying_id, dn.number " +
				"from supplying_detail as sd, delivery_note_detail as dnd, delivery_note dn where sd.id = dnd.supplying_detail_id and dn.id = dnd.delivery_note_id and dn.cancelled = 0";

		if (informAnmat) {
			sentence += " and dn.inform_anmat = 1 and dn.informed = 0);";
		} else {
			sentence += ");";
		}

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		query.executeUpdate();

		sentence = "create temporary table if not exists associated_supplyings_temp2 as " +
				"(select distinct sd.supplying_id, dn.number " +
				"from supplying_detail as sd, delivery_note_detail as dnd, delivery_note dn where sd.id = dnd.supplying_detail_id and dn.id = dnd.delivery_note_id and dn.cancelled = 0";

		if (informAnmat) {
			sentence += " and dn.inform_anmat = 1 and dn.informed = 0);";
		} else {
			sentence += ");";
		}

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		query.executeUpdate();

		sentence =	"select * from associated_supplyings_temp1 where supplying_id in (select supplying_id from associated_supplyings_temp2";

		if (!deliveryNoteNumb.isEmpty()) {
			sentence += " where number = '" + deliveryNoteNumb + "');";
		} else {
			sentence += ");";
		}

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		Iterator<Object[]> it = query.list().iterator();
		while (it.hasNext()) {
			Object[] supplyingDeliveryNotePair = it.next();
			Integer supplyingId = (Integer) supplyingDeliveryNotePair[0];
			String supplyingKey = "D".concat(supplyingId.toString());
			String deliveryNoteNumber = (String) supplyingDeliveryNotePair[1];
			List<String> deliveryNotes = null;
			if (!associatedSupplyings.containsKey(supplyingKey)) {
				deliveryNotes = new ArrayList<String>();
				deliveryNotes.add(deliveryNoteNumber);
				associatedSupplyings.put(supplyingKey, deliveryNotes);
			} else {
				deliveryNotes = associatedSupplyings.get(supplyingKey);
				deliveryNotes.add(deliveryNoteNumber);
				associatedSupplyings.put(supplyingKey, deliveryNotes);
			}
		}

		sentence = "drop temporary table if exists associated_supplyings_temp1;";
		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		query.executeUpdate();

		sentence = "drop temporary table if exists associated_supplyings_temp2;";
		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		query.executeUpdate();

		return associatedSupplyings;
	}

	@Override
	public Map<Integer, List<DeliveryNote>> getAssociatedSupplyings() {
		Map<Integer, List<DeliveryNote>> associatedSupplyings = new HashMap<Integer, List<DeliveryNote>>();
		String sentence = "select distinct sd.supplying_id, dn.* from supplying_detail as sd, delivery_note_detail as dnd, delivery_note dn where sd.id = dnd.supplying_detail_id and dn.id = dnd.delivery_note_id";

		Query query;
		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		Iterator it = query.list().iterator();
		while (it.hasNext()) {
			Object[] supplyingDeliveryNotePair = (Object[]) it.next();
			Integer supplyingId = (Integer) supplyingDeliveryNotePair[0];
			DeliveryNote deliveryNote = new DeliveryNote();
			deliveryNote.setId((Integer) supplyingDeliveryNotePair[1]);
			deliveryNote.setNumber((String) supplyingDeliveryNotePair[2]);
			deliveryNote.setDate((Date) supplyingDeliveryNotePair[3]);
			deliveryNote.setTransactionCodeANMAT((String) supplyingDeliveryNotePair[4]);
			deliveryNote.setCancelled((Boolean) supplyingDeliveryNotePair[5]);
			deliveryNote.setInformAnmat((Boolean) supplyingDeliveryNotePair[6]);
			deliveryNote.setInformed((Boolean) supplyingDeliveryNotePair[7]);
			deliveryNote.setFake((Boolean) supplyingDeliveryNotePair[8]);
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
		String dateFromFormated = null;
		String dateToFormated = null;

		try {
			String sentence = "select dn.*, dnd.* from order_detail as od, delivery_note_detail as dnd, delivery_note as dn,`order` as o, provisioning_request as p where od.id = dnd.order_detail_id and dn.id = dnd.delivery_note_id and od.order_id = o.id and o.provisioning_request_id = p.id";

			if (!deliveryNoteQuery.getDeliveryNoteNumber().equals("")) {
				sentence += " and dn.number = '" + deliveryNoteQuery.getDeliveryNoteNumber() + "'";
			}
			if (!StringUtils.isEmpty(deliveryNoteQuery.getDateFrom())) {
				dateFromFormated = StringUtility.toUSDateFormat(deliveryNoteQuery.getDateFrom());
				sentence += " and date(dn.date) >= '" + dateFromFormated + "'";
			}
			if (!StringUtils.isEmpty(deliveryNoteQuery.getDateTo())) {
				dateToFormated = StringUtility.toUSDateFormat(deliveryNoteQuery.getDateTo());
				sentence += " and date(dn.date) <= '" + dateToFormated + "'";
			}
			if (deliveryNoteQuery.getProvisioningRequestId() != null) {
				sentence += " and p.id = " + deliveryNoteQuery.getProvisioningRequestId();
			}
			if (deliveryNoteQuery.getClientId() != null) {
				sentence += " and p.client_id = " + deliveryNoteQuery.getClientId();
			}
			if (deliveryNoteQuery.getAffiliateId() != null) {
				sentence += " and p.affiliate_id = " + deliveryNoteQuery.getAffiliateId();
			}
			if (deliveryNoteQuery.getDeliveryLocationId() != null) {
				sentence += " and p.delivery_location_id = " + deliveryNoteQuery.getDeliveryLocationId();
			}
			if (deliveryNoteQuery.getAgreementId() != null) {
				sentence += " and p.agreement_id = " + deliveryNoteQuery.getAgreementId();
			}
			if (deliveryNoteQuery.getProductId() != null) {
				sentence += " and od.product_id = " + deliveryNoteQuery.getProductId();
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
		String dateFromFormated = null;
		String dateToFormated = null;

		try {
			String sentence = "select dn.*, dnd.* from output_detail as od, delivery_note_detail as dnd, delivery_note as dn,output as o where od.id = dnd.output_detail_id and dn.id = dnd.delivery_note_id and od.output_id = o.id";

			if (!deliveryNoteQuery.getDeliveryNoteNumber().equals("")) {
				sentence += " and dn.number = '" + deliveryNoteQuery.getDeliveryNoteNumber() + "'";
			}
			if (!StringUtils.isEmpty(deliveryNoteQuery.getDateFrom())) {
				dateFromFormated = StringUtility.toUSDateFormat(deliveryNoteQuery.getDateFrom());
				sentence += " and date(dn.date) >= '" + dateFromFormated + "'";
			}
			if (!StringUtils.isEmpty(deliveryNoteQuery.getDateTo())) {
				dateToFormated = StringUtility.toUSDateFormat(deliveryNoteQuery.getDateTo());
				sentence += " and date(dn.date) <= '" + dateToFormated + "'";
			}
			if (deliveryNoteQuery.getOutputId() != null) {
				sentence += " and o.id = " + deliveryNoteQuery.getOutputId();
			}
			if (deliveryNoteQuery.getConceptId() != null) {
				sentence += " and o.concept_id = " + deliveryNoteQuery.getConceptId();
			}
			if (deliveryNoteQuery.getProviderId() != null) {
				sentence += " and o.provider_id = " + deliveryNoteQuery.getProviderId();
			}
			if (deliveryNoteQuery.getDeliveryLocationId() != null) {
				sentence += " and o.delivery_location_id = " + deliveryNoteQuery.getDeliveryLocationId();
			}
			if (deliveryNoteQuery.getAgreementId() != null) {
				sentence += " and o.agreement_id = " + deliveryNoteQuery.getAgreementId();
			}
			if (deliveryNoteQuery.getProductId() != null) {
				sentence += " and od.product_id = " + deliveryNoteQuery.getProductId();
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
		String dateFromFormated = null;
		String dateToFormated = null;

        try {
            String sentence = "select dn.*, dnd.* from supplying_detail as sd, delivery_note_detail as dnd, delivery_note as dn,supplying as s where sd.id = dnd.supplying_detail_id and dn.id = dnd.delivery_note_id and sd.supplying_id = s.id";

			if (!deliveryNoteQuery.getDeliveryNoteNumber().equals("")) {
				sentence += " and dn.number = '" + deliveryNoteQuery.getDeliveryNoteNumber() + "'";
			}
			if (!StringUtils.isEmpty(deliveryNoteQuery.getDateFrom())) {
				dateFromFormated = StringUtility.toUSDateFormat(deliveryNoteQuery.getDateFrom());
				sentence += " and date(dn.date) >= '" + dateFromFormated + "'";
			}
			if (!StringUtils.isEmpty(deliveryNoteQuery.getDateTo())) {
				dateToFormated = StringUtility.toUSDateFormat(deliveryNoteQuery.getDateTo());
				sentence += " and date(dn.date) <= '" + dateToFormated + "'";
			}
			if (deliveryNoteQuery.getSupplyingId() != null) {
				sentence += " and s.id = " + deliveryNoteQuery.getSupplyingId();
			}
			if (deliveryNoteQuery.getClientId() != null) {
                sentence += " and s.client_id = " + deliveryNoteQuery.getClientId();
            }
			if (deliveryNoteQuery.getAffiliateId() != null) {
				sentence += " and s.affiliate_id = " + deliveryNoteQuery.getAffiliateId();
			}
            if (deliveryNoteQuery.getAgreementId() != null) {
                sentence += " and s.agreement_id = " + deliveryNoteQuery.getAgreementId();
            }
			if (deliveryNoteQuery.getProductId() != null) {
				sentence += " and sd.product_id = " + deliveryNoteQuery.getProductId();
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
		String sentence = "select distinct concat(case fake when true THEN 'X' when false then 'R' END, dn.number) " +
				"from supplying_detail as sd, delivery_note_detail as dnd, delivery_note as dn, supplying as s " +
				"where sd.id = dnd.supplying_detail_id and dn.id = dnd.delivery_note_id and sd.supplying_id = s.id and s.id ="
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
            String sentence = "select distinct concat(case fake when true THEN 'X' when false then 'R' END, dn.number) " +
					"from output_detail as od, delivery_note_detail as dnd, delivery_note as dn, output as o " +
					"where od.id = dnd.output_detail_id and dn.id = dnd.delivery_note_id and od.output_id = o.id and o.id ="
                    + outputId;
            Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
            return query.list();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}

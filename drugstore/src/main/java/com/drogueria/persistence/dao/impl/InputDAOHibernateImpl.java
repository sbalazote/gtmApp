package com.drogueria.persistence.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.drogueria.constant.Constants;
import com.drogueria.model.Input;
import com.drogueria.persistence.dao.InputDAO;
import com.drogueria.query.InputQuery;
import com.drogueria.util.StringUtility;

@Repository
public class InputDAOHibernateImpl implements InputDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Boolean existsSerial(String serialNumber, Integer productId, String gtin) {
		Query query = this.sessionFactory
				.getCurrentSession()
				.createQuery(
						"select distinct(inputsD) from Input as i inner join i.inputDetails as inputsD inner join inputsD.product as p inner join p.gtins as g where (inputsD.serialNumber = :serialNumber and inputsD.product.id = :productId and i.cancelled = false and g.number = :gtin)");
		query.setParameter("serialNumber", serialNumber);
		query.setParameter("productId", productId);
		query.setParameter("gtin", gtin);

		return !query.list().isEmpty();
	}

	@Override
	public void save(Input input) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(input);
	}

	@Override
	public Input get(Integer id) {
		return (Input) this.sessionFactory.getCurrentSession().get(Input.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Input> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(Input.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Input> getInputForSearch(InputQuery inputQuery) {

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Input.class, "input");
		criteria.createAlias("input.inputDetails", "inputDetail");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Date dateFromFormated = null;
		Date dateToFormated = null;

		if (inputQuery.getId() != null) {
			criteria.add(Restrictions.eq("id", inputQuery.getId()));
		}

		if (!StringUtils.isEmpty(inputQuery.getDateFrom())) {
			try {
				dateFromFormated = dateFormatter.parse(inputQuery.getDateFrom());
				criteria.add(Restrictions.ge("date", dateFromFormated));
			} catch (ParseException e) {
				throw new RuntimeException("El formato de la fecha ingresada no es valido.", e);
			}
		}
		if (!StringUtils.isEmpty(inputQuery.getDateTo())) {
			try {
				dateToFormated = dateFormatter.parse(inputQuery.getDateTo());
				criteria.add(Restrictions.le("date", dateToFormated));
			} catch (ParseException e) {
				throw new RuntimeException("El formato de la fecha ingresada no es valido.", e);
			}
		}

		if (inputQuery.getConceptId() != null) {
			criteria.add(Restrictions.eq("concept.id", inputQuery.getConceptId()));
		}

		if (inputQuery.getProviderId() != null) {
			criteria.add(Restrictions.eq("provider.id", inputQuery.getProviderId()));
		}

		if (inputQuery.getDeliveryLocationId() != null) {
			criteria.add(Restrictions.eq("deliveryLocation.id", inputQuery.getDeliveryLocationId()));
		}

		if (inputQuery.getAgreementId() != null) {
			criteria.add(Restrictions.eq("agreement.id", inputQuery.getAgreementId()));
		}

		if (!StringUtils.isEmpty(inputQuery.getDeliveryNoteNumber())) {
			criteria.add(Restrictions.eq("deliveryNoteNumber", inputQuery.getDeliveryNoteNumber()));
		}

		if (!StringUtils.isEmpty(inputQuery.getPurchaseOrderNumber())) {
			criteria.add(Restrictions.eq("purchaseOrderNumber", inputQuery.getPurchaseOrderNumber()));
		}
		if (inputQuery.getProductId() != null) {
			criteria.add(Restrictions.eq("inputDetail.product.id", inputQuery.getProductId()));
		}
		if (inputQuery.getCancelled() != null) {
			criteria.add(Restrictions.eq("cancelled", inputQuery.getCancelled()));
		}
        criteria.addOrder(Order.desc("id"));
		List<Input> results = criteria.list();

		return results;
	}

	@Override
	public boolean getCountInputSearch(InputQuery inputQuery) {
		if (this.getInputForSearch(inputQuery).size() < Constants.QUERY_MAX_RESULTS) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Input> getInputToAuthorize() {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Input where informAnmat = true and informed = false and cancelled = false");
		return query.list();
	}

    @SuppressWarnings("unchecked")
    @Override
    public List<Input> getForcedInputs() {
        Query query = this.sessionFactory.getCurrentSession().createQuery("from Input where informAnmat = true and informed = true and cancelled = false and transactionCodeANMAT is null and forcedInput = false");
        return query.list();
    }

	@Override
	public boolean exitsMovements(Input input) {
		// TODO hay que ver como se valida que no existen movimientos para un input
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Input> getInputs(boolean cancelled) {
		String sentence = "from Input where cancelled = :cancelled";
		Query query;
		query = this.sessionFactory.getCurrentSession().createQuery(sentence);
		query.setParameter("cancelled", cancelled);

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Input> getCancelables(InputQuery inputQuery) {

		String sentence = "select i.*, id.*  from input as i, input_detail as id where i.cancelled = 0 and id.input_id = i.id and (i.inform_anmat = 0 or (i.inform_anmat = 1 and i.informed = 1))";

		if (inputQuery.getId() != null) {
			sentence += " and i.id =" + inputQuery.getId();
		}

		if (!StringUtils.isEmpty(inputQuery.getDateFrom())) {
			sentence += " and i.date >= '" + StringUtility.toUSDateFormat(inputQuery.getDateFrom()) + "'";
		}

		if (!StringUtils.isEmpty(inputQuery.getDateTo())) {
			sentence += " and i.date <= '" + StringUtility.toUSDateFormat(inputQuery.getDateTo()) + "'";
		}

		if (inputQuery.getConceptId() != null) {
			sentence += " and i.concept_id = " + inputQuery.getConceptId();
		}

		if (inputQuery.getProviderId() != null) {
			sentence += " and i.provider_id = " + inputQuery.getProviderId();
		}

		if (inputQuery.getDeliveryLocationId() != null) {
			sentence += " and i.delivery_location_id = " + inputQuery.getDeliveryLocationId();
		}

		if (inputQuery.getAgreementId() != null) {
			sentence += " and i.agreement_id = " + inputQuery.getAgreementId();
		}

		if (!StringUtils.isEmpty(inputQuery.getDeliveryNoteNumber())) {
			sentence += " and i.delivery_note_number = " + inputQuery.getDeliveryNoteNumber();
		}

		if (!StringUtils.isEmpty(inputQuery.getPurchaseOrderNumber())) {
			sentence += " and i.purchase_order_number = " + inputQuery.getPurchaseOrderNumber();
		}

		sentence += " and not exists (select * from order_detail as od,input_detail as id1, `order` as o where o.cancelled = 0 and id1.input_id = i.id and id1.product_id = od.product_id and od.batch = id1.batch and od.expiration_date = id1.expiration_date and (od.serial_number is null or od.serial_number=id1.serial_number))";

		sentence += " and not exists (select * from output_detail as oud,input_detail as id2, output as o where o.cancelled = 0 and id2.input_id = i.id and id2.product_id = oud.product_id and oud.batch = id2.batch and oud.expiration_date = id2.expiration_date and (oud.serial_number is null or oud.serial_number=id2.serial_number));";

		Query query;
		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence).addEntity("i", Input.class).addJoin("id", "i.inputDetails")
				.addEntity("i", Input.class).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List<Input> results = query.list();

		return results;
	}

	public boolean isConceptInUse(Integer conceptId){
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Input where concept.id = :conceptId");
        query.setParameter("conceptId", conceptId);
        return !query.list().isEmpty();
	}
}

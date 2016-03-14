package com.lsntsolutions.gtmApp.persistence.dao.impl;

import com.lsntsolutions.gtmApp.constant.Constants;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.dto.SearchProductDTO;
import com.lsntsolutions.gtmApp.dto.SearchProductResultDTO;
import com.lsntsolutions.gtmApp.model.Audit;
import com.lsntsolutions.gtmApp.persistence.dao.AuditDAO;
import com.lsntsolutions.gtmApp.query.AuditQuery;
import com.lsntsolutions.gtmApp.service.RoleService;
import com.lsntsolutions.gtmApp.service.UserService;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class AuditDAOHibernateImpl implements AuditDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Override
	public void save(Audit audit) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(audit);
	}

	@Override
	public Audit get(Integer id) {
		return (Audit) this.sessionFactory.getCurrentSession().get(Audit.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Audit> getAll() {
		return this.sessionFactory.getCurrentSession().createCriteria(Audit.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Audit> getAuditForSearch(AuditQuery auditQuery) {

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Audit.class);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Date dateFromFormated = null;
		Date dateToFormated = null;

		if (!StringUtils.isEmpty(auditQuery.getDateFrom())) {
			try {
				dateFromFormated = dateFormatter.parse(auditQuery.getDateFrom());
				criteria.add(Restrictions.ge("date", dateFromFormated));
			} catch (ParseException e) {
				throw new RuntimeException("El formato de la fecha ingresada no es valido.", e);
			}
		}
		if (!StringUtils.isEmpty(auditQuery.getDateTo())) {
			try {
				dateToFormated = dateFormatter.parse(auditQuery.getDateTo());
				criteria.add(Restrictions.le("date", dateToFormated));
			} catch (ParseException e) {
				throw new RuntimeException("El formato de la fecha ingresada no es valido.", e);
			}
		}

		if (auditQuery.getOperationId() != null) {
			criteria.add(Restrictions.eq("operationId", auditQuery.getOperationId()));
		}

		if (auditQuery.getRoleId() != null) {
			criteria.add(Restrictions.eq("role.id", auditQuery.getRoleId()));
		}

		if (auditQuery.getUserId() != null) {
			criteria.add(Restrictions.eq("user.id", auditQuery.getUserId()));
		}

        criteria.addOrder(Order.desc("id"));

		List<Audit> results = criteria.list();

		return results;
	}

	@Override
	public boolean getCountAuditSearch(AuditQuery auditQuery) {
		return this.getAuditForSearch(auditQuery).size() < Constants.QUERY_MAX_RESULTS;
	}

	@Override
	@SuppressWarnings("unchecked")
	public SearchProductResultDTO getAudit(Integer productId, String serialNumber, String batch, String expirationDate) {
		SearchProductResultDTO searchProductResultDTO = new SearchProductResultDTO();
		boolean isSerialBatchExpirationAssigned = false;

		String sentence = "select distinct a.*, i.cancelled, id.batch, id.expiration_date from audit as a, input_detail as id, input as i " +
				"where ((a.role_id = " + RoleOperation.INPUT.getId() + " or a.role_id = " + RoleOperation.SERIALIZED_RETURNS.getId() + ") and a.operation_id = id.input_id and i.id = id.input_id";

		if (productId != null) {
			sentence += " and id.product_id = " + productId;
		}

		if (serialNumber != null) {
			sentence += " and id.serial_number = '" + serialNumber + "'";
		}

		if(batch != null) {
			sentence += " and id.batch = '" + batch + "'";
		}

		if(expirationDate != null) {
			String[] parts = expirationDate.split("/");
			expirationDate = parts[2] + "/" + parts[1] + "/" + parts[0];
			sentence += " and id.expiration_date = '" + expirationDate + "'";
		}

		sentence += ") order by a.`date` desc";

		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat expirationDateFormatter = new SimpleDateFormat("yyMMdd");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		List<Object[]> inputsAudit = query.list();

		if (!inputsAudit.isEmpty()) {
			Object[] inputAuditAux = inputsAudit.get(0);
			searchProductResultDTO.setBatch((String) inputAuditAux[6]);
			searchProductResultDTO.setExpirationDate(expirationDateFormatter.format(inputAuditAux[7]));
			isSerialBatchExpirationAssigned = true;
		}
		List<SearchProductDTO> inputsSearchProductDTO = new ArrayList<>();
		for (Object[] audit : inputsAudit) {
			SearchProductDTO searchProductDTO = new SearchProductDTO((Integer)audit[0], roleService.get((Integer)audit[1]).getDescription(), (Integer)audit[2], (String)dateFormatter.format(audit[3]), userService.get((Integer)audit[4]).getName(), (Boolean)audit[5]);
			inputsSearchProductDTO.add(searchProductDTO);
		}
		searchProductResultDTO.setInputs(inputsSearchProductDTO);

		sentence = "select distinct a.*, o.cancelled, od.batch, od.expiration_date from audit as a, output_detail as od, output as o " +
				"where ((a.role_id = " + RoleOperation.OUTPUT.getId() + " or a.role_id = " + RoleOperation.PRODUCT_DESTRUCTION.getId() + ") and a.operation_id = od.output_id and o.id = od.output_id";

		if (productId != null) {
			sentence += " and od.product_id = " + productId;
		}

		if (serialNumber != null) {
			sentence += " and od.serial_number = '" + serialNumber + "'";
		}

		if(batch != null) {
			sentence += " and od.batch = '" + batch + "'";
		}

		if(expirationDate != null) {
			String[] parts = expirationDate.split("/");
			expirationDate = parts[2] + "/" + parts[1] + "/" + parts[0];
			sentence += " and od.expiration_date = '" + expirationDate + "'";
		}

		sentence += ") order by a.`date` desc";

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		List<Object[]> outputsAudit = query.list();

		if (!outputsAudit.isEmpty() && !isSerialBatchExpirationAssigned) {
			Object[] outputAuditAux = outputsAudit.get(0);
			searchProductResultDTO.setBatch((String) outputAuditAux[6]);
			searchProductResultDTO.setExpirationDate(expirationDateFormatter.format(outputAuditAux[7]));
			isSerialBatchExpirationAssigned = true;
		}
		List<SearchProductDTO> outputsSearchProductDTO = new ArrayList<>();
		for (Object[] audit : outputsAudit) {
			SearchProductDTO searchProductDTO = new SearchProductDTO((Integer)audit[0], roleService.get((Integer)audit[1]).getDescription(), (Integer)audit[2], (String)dateFormatter.format(audit[3]), userService.get((Integer)audit[4]).getName(), (Boolean)audit[5]);
			outputsSearchProductDTO.add(searchProductDTO);
		}
		searchProductResultDTO.setOutputs(outputsSearchProductDTO);

		sentence = "select distinct a.*, s.cancelled, sd.batch, sd.expiration_date from audit as a, supplying_detail as sd, supplying as s " +
				"where (a.role_id = " + RoleOperation.SUPPLYING.getId() + " and a.operation_id = sd.supplying_id and s.id = sd.supplying_id";

		if (productId != null) {
			sentence += " and sd.product_id = " + productId;
		}

		if (serialNumber != null) {
			sentence += " and sd.serial_number = '" + serialNumber + "'";
		}

		if(batch != null) {
			sentence += " and sd.batch = '" + batch + "'";
		}

		if(expirationDate != null) {
			String[] parts = expirationDate.split("/");
			expirationDate = parts[2] + "/" + parts[1] + "/" + parts[0];
			sentence += " and sd.expiration_date = '" + expirationDate + "'";
		}

		sentence += ") order by a.`date` desc";

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		List<Object[]> supplyingsAudit = query.list();

		if (!supplyingsAudit.isEmpty() && !isSerialBatchExpirationAssigned) {
			Object[] supplyingsAuditAux = supplyingsAudit.get(0);
			searchProductResultDTO.setBatch((String) supplyingsAuditAux[6]);
			searchProductResultDTO.setExpirationDate(expirationDateFormatter.format(supplyingsAuditAux[7]));
			isSerialBatchExpirationAssigned = true;
		}
		List<SearchProductDTO> supplyingsSearchProductDTO = new ArrayList<>();
		for (Object[] audit : supplyingsAudit) {
			SearchProductDTO searchProductDTO = new SearchProductDTO((Integer)audit[0], roleService.get((Integer)audit[1]).getDescription(), (Integer)audit[2], (String)dateFormatter.format(audit[3]), userService.get((Integer)audit[4]).getName(), (Boolean)audit[5]);
			supplyingsSearchProductDTO.add(searchProductDTO);
		}
		searchProductResultDTO.setSupplyings(supplyingsSearchProductDTO);

		sentence = "select distinct a.*, o.cancelled, od.batch, od.expiration_date from audit as a, order_detail as od, `order` as o " +
					"where (a.role_id = " + RoleOperation.ORDER_ASSEMBLY.getId() + " and a.operation_id = od.order_id and o.id = od.order_id";

		if (productId != null) {
			sentence += " and od.product_id = " + productId;
		}

		if (serialNumber != null) {
			sentence += " and od.serial_number = '" + serialNumber + "'";
		}

		if(batch != null) {
			sentence += " and od.batch = '" + batch + "'";
		}

		if(expirationDate != null) {
			String[] parts = expirationDate.split("/");
			expirationDate = parts[2] + "/" + parts[1] + "/" + parts[0];
			sentence += " and od.expiration_date = '" + expirationDate + "'";
		}

		sentence += ") order by a.`date` desc";

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		List<Object[]> ordersAudit = query.list();

		if (!ordersAudit.isEmpty() && !isSerialBatchExpirationAssigned) {
			Object[] ordersAuditAux = ordersAudit.get(0);
			searchProductResultDTO.setBatch((String) ordersAuditAux[6]);
			searchProductResultDTO.setExpirationDate(expirationDateFormatter.format(ordersAuditAux[7]));
			isSerialBatchExpirationAssigned = true;
		}
		List<SearchProductDTO> ordersSearchProductDTO = new ArrayList<>();
		for (Object[] audit : ordersAudit) {
			SearchProductDTO searchProductDTO = new SearchProductDTO((Integer)audit[0], roleService.get((Integer)audit[1]).getDescription(), (Integer)audit[2], (String)dateFormatter.format(audit[3]), userService.get((Integer)audit[4]).getName(), (Boolean)audit[5]);
			ordersSearchProductDTO.add(searchProductDTO);
		}
		searchProductResultDTO.setOrders(ordersSearchProductDTO);

		sentence = "select distinct a.*, dn.cancelled, od.batch, od.expiration_date from audit as a, delivery_note_detail as dnd, order_detail as od, delivery_note as dn " +
					"where (a.role_id = " + RoleOperation.DELIVERY_NOTE_PRINT.getId() + " and a.operation_id = dnd.delivery_note_id and dn.id = dnd.delivery_note_id and dnd.order_detail_id = od.id and dnd.output_detail_id is null and dnd.supplying_detail_id is null";

		if (productId != null) {
			sentence += " and od.product_id = " + productId;
		}

		if (serialNumber != null) {
			sentence += " and od.serial_number = '" + serialNumber + "'";
		}

		if(batch != null) {
			sentence += " and od.batch = '" + batch + "'";
		}

		if(expirationDate != null) {
			String[] parts = expirationDate.split("/");
			expirationDate = parts[2] + "/" + parts[1] + "/" + parts[0];
			sentence += " and od.expiration_date = '" + expirationDate + "'";
		}

		sentence += ") order by a.`date` desc";

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		List<Object[]> deliveryNoteAudit = query.list();

		if (!deliveryNoteAudit.isEmpty() && !isSerialBatchExpirationAssigned) {
			Object[] deliveryNoteAuditAux = deliveryNoteAudit.get(0);
			searchProductResultDTO.setBatch((String) deliveryNoteAuditAux[6]);
			searchProductResultDTO.setExpirationDate(expirationDateFormatter.format(deliveryNoteAuditAux[7]));
			isSerialBatchExpirationAssigned = true;
		}
		List<SearchProductDTO> deliveryNoteSearchProductDTO = new ArrayList<>();
		for (Object[] audit : deliveryNoteAudit) {
			SearchProductDTO searchProductDTO = new SearchProductDTO((Integer)audit[0], roleService.get((Integer)audit[1]).getDescription(), (Integer)audit[2], (String)dateFormatter.format(audit[3]), userService.get((Integer)audit[4]).getName(), (Boolean)audit[5]);
			deliveryNoteSearchProductDTO.add(searchProductDTO);
		}

		sentence = "select distinct a.*, dn.cancelled, od.batch, od.expiration_date from audit as a, delivery_note_detail as dnd, output_detail as od, delivery_note as dn " +
					"where (a.role_id = " + RoleOperation.DELIVERY_NOTE_PRINT.getId() + " and a.operation_id = dnd.delivery_note_id and dn.id = dnd.delivery_note_id and dnd.output_detail_id = od.id and dnd.order_detail_id is null";

		if (productId != null) {
			sentence += " and od.product_id = " + productId;
		}

		if (serialNumber != null) {
			sentence += " and od.serial_number = '" + serialNumber + "'";
		}

		if(batch != null) {
			sentence += " and od.batch = '" + batch + "'";
		}

		if(expirationDate != null) {
			String[] parts = expirationDate.split("/");
			expirationDate = parts[2] + "/" + parts[1] + "/" + parts[0];
			sentence += " and od.expiration_date = '" + expirationDate + "'";
		}

		sentence += ") order by a.`date` desc";

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
		deliveryNoteAudit = query.list();

		if (!deliveryNoteAudit.isEmpty() && !isSerialBatchExpirationAssigned) {
			Object[] deliveryNoteAuditAux = deliveryNoteAudit.get(0);
			searchProductResultDTO.setBatch((String) deliveryNoteAuditAux[6]);
			searchProductResultDTO.setExpirationDate(expirationDateFormatter.format(deliveryNoteAuditAux[7]));
			isSerialBatchExpirationAssigned = true;
		}
		for (Object[] audit : deliveryNoteAudit) {
			SearchProductDTO searchProductDTO = new SearchProductDTO((Integer)audit[0], roleService.get((Integer)audit[1]).getDescription(), (Integer)audit[2], (String)dateFormatter.format(audit[3]), userService.get((Integer)audit[4]).getName(), (Boolean)audit[5]);
			deliveryNoteSearchProductDTO.add(searchProductDTO);
		}

		sentence = "select distinct a.*, dn.cancelled, sd.batch, sd.expiration_date from audit as a, delivery_note_detail as dnd, supplying_detail as sd, delivery_note as dn " +
					"where (a.role_id = " + RoleOperation.DELIVERY_NOTE_PRINT.getId() + " and a.operation_id = dnd.delivery_note_id and dn.id = dnd.delivery_note_id and dnd.supplying_detail_id = sd.id and dnd.order_detail_id is null and dnd.output_detail_id is null";

		if (productId != null) {
			sentence += " and sd.product_id = " + productId;
		}

		if (serialNumber != null) {
			sentence += " and sd.serial_number = '" + serialNumber + "'";
		}

		if(batch != null) {
			sentence += " and sd.batch = '" + batch + "'";
		}

		if(expirationDate != null) {
			String[] parts = expirationDate.split("/");
			expirationDate = parts[2] + "/" + parts[1] + "/" + parts[0];
			sentence += " and sd.expiration_date = '" + expirationDate + "'";
		}

		sentence += ") order by a.`date` desc";

        query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence);
        deliveryNoteAudit = query.list();

		if (!deliveryNoteAudit.isEmpty() && !isSerialBatchExpirationAssigned) {
			Object[] deliveryNoteAuditAux = deliveryNoteAudit.get(0);
			searchProductResultDTO.setBatch((String) deliveryNoteAuditAux[6]);
			searchProductResultDTO.setExpirationDate(expirationDateFormatter.format(deliveryNoteAuditAux[7]));
		}
        for (Object[] audit : deliveryNoteAudit) {
			SearchProductDTO searchProductDTO = new SearchProductDTO((Integer)audit[0], roleService.get((Integer)audit[1]).getDescription(), (Integer)audit[2], (String)dateFormatter.format(audit[3]), userService.get((Integer)audit[4]).getName(), (Boolean)audit[5]);
			deliveryNoteSearchProductDTO.add(searchProductDTO);
        }

		searchProductResultDTO.setDeliveryNotes(deliveryNoteSearchProductDTO);

		return searchProductResultDTO;
	}

	@Override
	public Date getDate(RoleOperation roleOperation, Integer operationId) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Audit where role.id = :roleId and operationId = :operationId");
		query.setParameter("roleId",roleOperation.getId());
		query.setParameter("operationId",operationId);

		if(query.list() != null)
			return ((Audit)query.list().get(0)).getDate();
		else
			return null;
	}
}

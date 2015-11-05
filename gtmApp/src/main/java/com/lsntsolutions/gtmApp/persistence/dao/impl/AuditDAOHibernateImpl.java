package com.lsntsolutions.gtmApp.persistence.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.constant.Constants;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.dto.AuditDTO;
import com.lsntsolutions.gtmApp.model.Audit;
import com.lsntsolutions.gtmApp.persistence.dao.AuditDAO;
import com.lsntsolutions.gtmApp.query.AuditQuery;
import com.lsntsolutions.gtmApp.dto.AuditDTO;
import com.lsntsolutions.gtmApp.dto.AuditResultDTO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.lsntsolutions.gtmApp.dto.AuditDTO;

@Repository
public class AuditDAOHibernateImpl implements AuditDAO {

	@Autowired
	private SessionFactory sessionFactory;

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

		if (auditQuery.getActionId() != null) {
			criteria.add(Restrictions.eq("auditAction.id", auditQuery.getActionId()));
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
		if (this.getAuditForSearch(auditQuery).size() < Constants.QUERY_MAX_RESULTS) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public AuditResultDTO getAudit(Integer productId, String serialNumber) {
		AuditResultDTO auditResultDTO = new AuditResultDTO();
		String sentence = "select distinct a.* from audit as a, input_detail as id where (a.role_id = " + RoleOperation.INPUT.getId()
				+ " and id.serial_number = '" + serialNumber + "' and a.operation_id = id.input_id and a.action_id = " + AuditState.COMFIRMED.getId();

		if (productId != null) {
			sentence += " and id.product_id = " + productId;
		}

		sentence += ") order by a.`date` desc";

		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence).addEntity("a", Audit.class);
		List<Audit> inputsAudit = query.list();
		List<AuditDTO> inputsAuditDTO = new ArrayList<AuditDTO>();
		for (Audit audit : inputsAudit) {

			AuditDTO auditDTO = new AuditDTO(audit.getId(), audit.getRole().getDescription(), audit.getOperationId(), audit.getAuditAction().getDescription(),
					dateFormatter.format(audit.getDate()), audit.getUser().getName());
			inputsAuditDTO.add(auditDTO);
		}
		auditResultDTO.setInputs(inputsAuditDTO);

		sentence = "select distinct a.* from audit as a, output_detail as od where (a.role_id = " + RoleOperation.OUTPUT.getId() + " and od.serial_number = '"
				+ serialNumber + "' and a.operation_id = od.output_id and a.action_id = " + AuditState.COMFIRMED.getId();
		if (productId != null) {
			sentence += " and od.product_id = " + productId;
		}
		sentence += ") order by a.`date` desc";

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence).addEntity("a", Audit.class);
		List<Audit> outputsAudit = query.list();
		List<AuditDTO> outputsAuditDTO = new ArrayList<AuditDTO>();
		for (Audit audit : outputsAudit) {
			dateFormatter.format(audit.getDate());
			AuditDTO auditDTO = new AuditDTO(audit.getId(), audit.getRole().getDescription(), audit.getOperationId(), audit.getAuditAction().getDescription(),
					dateFormatter.format(audit.getDate()), audit.getUser().getName());
			outputsAuditDTO.add(auditDTO);
		}
		auditResultDTO.setOutputs(outputsAuditDTO);

		sentence = "select distinct a.* from audit as a, supplying_detail as sd where (a.role_id = " + RoleOperation.SUPPLYING.getId() + " and sd.serial_number = '"
				+ serialNumber + "' and a.operation_id = sd.supplying_id and a.action_id = " + AuditState.COMFIRMED.getId();
		if (productId != null) {
			sentence += " and sd.product_id = " + productId;
		}
		sentence += ") order by a.`date` desc";

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence).addEntity("a", Audit.class);
		List<Audit> supplyingsAudit = query.list();
		List<AuditDTO> supplyingsAuditDTO = new ArrayList<AuditDTO>();
		for (Audit audit : supplyingsAudit) {
			dateFormatter.format(audit.getDate());
			AuditDTO auditDTO = new AuditDTO(audit.getId(), audit.getRole().getDescription(), audit.getOperationId(), audit.getAuditAction().getDescription(),
					dateFormatter.format(audit.getDate()), audit.getUser().getName());
			supplyingsAuditDTO.add(auditDTO);
		}
		auditResultDTO.setSupplyings(supplyingsAuditDTO);

		if (productId == null) {
			sentence = "select distinct a.* from audit as a, order_detail as od where (a.role_id = " + RoleOperation.ORDER_ASSEMBLY.getId()
					+ " and od.serial_number = '" + serialNumber + "' and a.operation_id = od.order_id and a.action_id = " + AuditState.COMFIRMED.getId()+") order by a.`date` desc";
		}

		if (productId != null) {
			sentence = "select distinct a.* from audit as a, order_detail as od where (a.role_id = " + RoleOperation.ORDER_ASSEMBLY.getId()
					+ " and od.product_id = " + productId + " and od.serial_number = '" + serialNumber + "' and a.operation_id = od.order_id and a.action_id = " + AuditState.COMFIRMED.getId()+") order by a.`date` desc";
		}

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence).addEntity("a", Audit.class);
		List<Audit> ordersAudit = query.list();
		List<AuditDTO> ordersAuditDTO = new ArrayList<AuditDTO>();
		for (Audit audit : ordersAudit) {
			dateFormatter.format(audit.getDate());
			AuditDTO auditDTO = new AuditDTO(audit.getId(), audit.getRole().getDescription(), audit.getOperationId(), audit.getAuditAction().getDescription(),
					dateFormatter.format(audit.getDate()), audit.getUser().getName());
			ordersAuditDTO.add(auditDTO);
		}
		auditResultDTO.setOrders(ordersAuditDTO);
		if (productId == null) {
			sentence = "select distinct a.* from audit as a, delivery_note_detail as dnd, order_detail as od where (a.role_id = "
					+ RoleOperation.DELIVERY_NOTE_PRINT.getId() + " and od.serial_number = '" + serialNumber
					+ "' and a.operation_id = dnd.delivery_note_id and dnd.order_detail_id = od.id and dnd.output_detail_id is null and dnd.supplying_detail_id is null and a.action_id = " + AuditState.COMFIRMED.getId()+") order by a.`date` desc";
		}
		if (productId != null) {
			sentence = "select distinct a.* from audit as a, delivery_note_detail as dnd, order_detail as od where (a.role_id = "
					+ RoleOperation.DELIVERY_NOTE_PRINT.getId() + " and od.product_id = " + productId + " and od.serial_number = '" + serialNumber
					+ "' and a.operation_id = dnd.delivery_note_id and dnd.order_detail_id = od.id and dnd.output_detail_id is null and dnd.supplying_detail_id is null and a.action_id = " + AuditState.COMFIRMED.getId()+") order by a.`date` desc";
		}

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence).addEntity("a", Audit.class);
		List<Audit> deliveryNoteAudit = query.list();
		List<AuditDTO> deliveryNoteAuditDTO = new ArrayList<AuditDTO>();
		for (Audit audit : deliveryNoteAudit) {
			dateFormatter.format(audit.getDate());
			AuditDTO auditDTO = new AuditDTO(audit.getId(), audit.getRole().getDescription(), audit.getOperationId(), audit.getAuditAction().getDescription(),
					dateFormatter.format(audit.getDate()), audit.getUser().getName());
			deliveryNoteAuditDTO.add(auditDTO);
		}

		if (productId == null) {
			sentence = "select distinct a.* from audit as a, delivery_note_detail as dnd, output_detail as od where (a.role_id = "
					+ RoleOperation.DELIVERY_NOTE_PRINT.getId() + " and od.serial_number = '" + serialNumber
					+ "' and a.operation_id = dnd.delivery_note_id and dnd.output_detail_id = od.id and dnd.order_detail_id is null and a.action_id = " + AuditState.COMFIRMED.getId()+") order by a.`date` desc";
		}
		if (productId != null) {
			sentence = "select distinct a.* from audit as a, delivery_note_detail as dnd, output_detail as od where (a.role_id = "
					+ RoleOperation.DELIVERY_NOTE_PRINT.getId() + " and od.product_id = " + productId + " and od.serial_number = '" + serialNumber
					+ "' and a.operation_id = dnd.delivery_note_id and dnd.output_detail_id = od.id and dnd.order_detail_id is null and dnd.supplying_detail_id is null and a.action_id = " + AuditState.COMFIRMED.getId()+") order by a.`date` desc";
		}

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence).addEntity("a", Audit.class);
		deliveryNoteAudit = query.list();
		for (Audit audit : deliveryNoteAudit) {
			dateFormatter.format(audit.getDate());
			AuditDTO auditDTO = new AuditDTO(audit.getId(), audit.getRole().getDescription(), audit.getOperationId(), audit.getAuditAction().getDescription(),
					dateFormatter.format(audit.getDate()), audit.getUser().getName());
			deliveryNoteAuditDTO.add(auditDTO);
		}

        if (productId == null) {
            sentence = "select distinct a.* from audit as a, delivery_note_detail as dnd, supplying_detail as sd where (a.role_id = "
                    + RoleOperation.DELIVERY_NOTE_PRINT.getId() + " and sd.serial_number = '" + serialNumber
                    + "' and a.operation_id = dnd.delivery_note_id and dnd.supplying_detail_id = sd.id and dnd.order_detail_id is null and dnd.output_detail_id is null and a.action_id = " + AuditState.COMFIRMED.getId()+") order by a.`date` desc";
        }
        if (productId != null) {
            sentence = "select distinct a.* from audit as a, delivery_note_detail as dnd, supplying_detail as sd where (a.role_id = "
                    + RoleOperation.DELIVERY_NOTE_PRINT.getId() + " and sd.product_id = " + productId + " and sd.serial_number = '" + serialNumber
                    + "' and a.operation_id = dnd.delivery_note_id and dnd.supplying_detail_id = sd.id and dnd.order_detail_id is null and dnd.output_detail_id is null and a.action_id = " + AuditState.COMFIRMED.getId()+") order by a.`date` desc";
        }

        query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence).addEntity("a", Audit.class);
        deliveryNoteAudit = query.list();
        for (Audit audit : deliveryNoteAudit) {
            dateFormatter.format(audit.getDate());
            AuditDTO auditDTO = new AuditDTO(audit.getId(), audit.getRole().getDescription(), audit.getOperationId(), audit.getAuditAction().getDescription(),
                    dateFormatter.format(audit.getDate()), audit.getUser().getName());
            deliveryNoteAuditDTO.add(auditDTO);
        }

		auditResultDTO.setDeliveryNotes(deliveryNoteAuditDTO);

		return auditResultDTO;
	}

	@Override
	@SuppressWarnings("unchecked")
	public AuditResultDTO getAudit(Integer productId, String batch, String expirateDate) {
		AuditResultDTO auditResultDTO = new AuditResultDTO();
		String[] parts = expirateDate.split("/");
		expirateDate = parts[2] + "/" + parts[1] + "/" + parts[0];

		String sentence = "select distinct a.* from audit as a, input_detail as id where (a.role_id = " + RoleOperation.INPUT.getId() + " and id.product_id = "
				+ productId + " and id.batch = '" + batch + "' and id.expiration_date = '" + expirateDate
				+ "' and a.operation_id = id.input_id and a.action_id = " + AuditState.COMFIRMED.getId()+" )  order by a.`date` desc";
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence).addEntity("a", Audit.class);
		List<Audit> inputsAudit = query.list();
		List<AuditDTO> inputsAuditDTO = new ArrayList<AuditDTO>();
		for (Audit audit : inputsAudit) {

			AuditDTO auditDTO = new AuditDTO(audit.getId(), audit.getRole().getDescription(), audit.getOperationId(), audit.getAuditAction().getDescription(),
					dateFormatter.format(audit.getDate()), audit.getUser().getName());
			inputsAuditDTO.add(auditDTO);
		}
		auditResultDTO.setInputs(inputsAuditDTO);

		sentence = "select distinct a.* from audit as a, output_detail as od where (a.role_id = " + RoleOperation.OUTPUT.getId() + " and od.product_id = "
				+ productId + " and od.batch = '" + batch + "' and od.expiration_date = '" + expirateDate
				+ "' and a.operation_id = od.output_id and a.action_id = " + AuditState.COMFIRMED.getId()+") order by a.`date` desc";

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence).addEntity("a", Audit.class);
		List<Audit> outputsAudit = query.list();
		List<AuditDTO> outputsAuditDTO = new ArrayList<AuditDTO>();
		for (Audit audit : outputsAudit) {
			dateFormatter.format(audit.getDate());
			AuditDTO auditDTO = new AuditDTO(audit.getId(), audit.getRole().getDescription(), audit.getOperationId(), audit.getAuditAction().getDescription(),
					dateFormatter.format(audit.getDate()), audit.getUser().getName());
			outputsAuditDTO.add(auditDTO);
		}
		auditResultDTO.setOutputs(outputsAuditDTO);

		sentence = "select distinct a.* from audit as a, supplying_detail as sd where (a.role_id = " + RoleOperation.SUPPLYING.getId() + " and sd.product_id = "
				+ productId + " and sd.batch = '" + batch + "' and sd.expiration_date = '" + expirateDate
				+ "' and a.operation_id = sd.supplying_id and a.action_id = " + AuditState.COMFIRMED.getId()+") order by a.`date` desc";

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence).addEntity("a", Audit.class);
		List<Audit> supplyingsAudit = query.list();
		List<AuditDTO> supplyingsAuditDTO = new ArrayList<AuditDTO>();
		for (Audit audit : supplyingsAudit) {
			dateFormatter.format(audit.getDate());
			AuditDTO auditDTO = new AuditDTO(audit.getId(), audit.getRole().getDescription(), audit.getOperationId(), audit.getAuditAction().getDescription(),
					dateFormatter.format(audit.getDate()), audit.getUser().getName());
			supplyingsAuditDTO.add(auditDTO);
		}
		auditResultDTO.setSupplyings(supplyingsAuditDTO);

		sentence = "select distinct a.* from audit as a, order_detail as od where (a.role_id = " + RoleOperation.ORDER_ASSEMBLY.getId()
				+ " and od.product_id = " + productId + " and od.batch = '" + batch + "' and od.expiration_date = '" + expirateDate
				+ "' and a.operation_id = od.order_id and a.action_id = " + AuditState.COMFIRMED.getId()+") order by a.`date` desc";

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence).addEntity("a", Audit.class);
		List<Audit> ordersAudit = query.list();
		List<AuditDTO> ordersAuditDTO = new ArrayList<AuditDTO>();
		for (Audit audit : ordersAudit) {
			dateFormatter.format(audit.getDate());
			AuditDTO auditDTO = new AuditDTO(audit.getId(), audit.getRole().getDescription(), audit.getOperationId(), audit.getAuditAction().getDescription(),
					dateFormatter.format(audit.getDate()), audit.getUser().getName());
			ordersAuditDTO.add(auditDTO);
		}
		auditResultDTO.setOrders(ordersAuditDTO);
		sentence = "select distinct a.* from audit as a, delivery_note_detail as dnd, order_detail as od where (a.role_id = "
				+ RoleOperation.DELIVERY_NOTE_PRINT.getId() + " and od.product_id = " + productId + " and od.batch = '" + batch
				+ "' and od.expiration_date = '" + expirateDate
				+ "' and a.operation_id = dnd.delivery_note_id and dnd.order_detail_id = od.id and dnd.output_detail_id is null and a.action_id = " + AuditState.COMFIRMED.getId()+") order by a.`date` desc";

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence).addEntity("a", Audit.class);
		List<Audit> deliveryNoteAudit = query.list();
		List<AuditDTO> deliveryNoteAuditDTO = new ArrayList<AuditDTO>();
		for (Audit audit : deliveryNoteAudit) {
			dateFormatter.format(audit.getDate());
			AuditDTO auditDTO = new AuditDTO(audit.getId(), audit.getRole().getDescription(), audit.getOperationId(), audit.getAuditAction().getDescription(),
					dateFormatter.format(audit.getDate()), audit.getUser().getName());
			deliveryNoteAuditDTO.add(auditDTO);
		}

		sentence = "select distinct a.* from audit as a, delivery_note_detail as dnd, output_detail as od where (a.role_id = "
				+ RoleOperation.DELIVERY_NOTE_PRINT.getId() + " and od.product_id = " + productId + " and od.batch = '" + batch
				+ "' and od.expiration_date = '" + expirateDate
				+ "' and a.operation_id = dnd.delivery_note_id and dnd.output_detail_id = od.id and dnd.order_detail_id is null and a.action_id = " + AuditState.COMFIRMED.getId()+") order by a.`date` desc";

		query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence).addEntity("a", Audit.class);
		deliveryNoteAudit = query.list();
		for (Audit audit : deliveryNoteAudit) {
			dateFormatter.format(audit.getDate());
			AuditDTO auditDTO = new AuditDTO(audit.getId(), audit.getRole().getDescription(), audit.getOperationId(), audit.getAuditAction().getDescription(),
					dateFormatter.format(audit.getDate()), audit.getUser().getName());
			deliveryNoteAuditDTO.add(auditDTO);
		}

        sentence = "select distinct a.* from audit as a, delivery_note_detail as dnd, supplying_detail as sd where (a.role_id = "
                + RoleOperation.DELIVERY_NOTE_PRINT.getId() + " and sd.product_id = " + productId + " and sd.batch = '" + batch
                + "' and sd.expiration_date = '" + expirateDate
                + "' and a.operation_id = dnd.delivery_note_id and dnd.supplying_detail_id = sd.id and a.action_id = " + AuditState.COMFIRMED.getId()+") order by a.`date` desc";

        System.out.println(sentence);
        query = this.sessionFactory.getCurrentSession().createSQLQuery(sentence).addEntity("a", Audit.class);
        deliveryNoteAudit = query.list();
        for (Audit audit : deliveryNoteAudit) {
            dateFormatter.format(audit.getDate());
            AuditDTO auditDTO = new AuditDTO(audit.getId(), audit.getRole().getDescription(), audit.getOperationId(), audit.getAuditAction().getDescription(),
                    dateFormatter.format(audit.getDate()), audit.getUser().getName());
            deliveryNoteAuditDTO.add(auditDTO);
        }

        auditResultDTO.setDeliveryNotes(deliveryNoteAuditDTO);
		return auditResultDTO;
	}

	@Override
	public Date getDate(RoleOperation roleOperation, Integer operationId, AuditState auditState) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from Audit where role.id = :roleId and operationId = :operationId and auditAction.id = :auditStateId");
		query.setParameter("roleId",roleOperation.getId());
		query.setParameter("operationId",operationId);
		query.setParameter("auditStateId", auditState.getId());

		if(query.list() != null)
			return ((Audit)query.list().get(0)).getDate();
		else
			return null;
	}
}

package com.lsntsolutions.gtmApp.service.impl;

import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.dto.AuditDTO;
import com.lsntsolutions.gtmApp.dto.AuditResultDTO;
import com.lsntsolutions.gtmApp.dto.OutputOrderResultDTO;
import com.lsntsolutions.gtmApp.model.Audit;
import com.lsntsolutions.gtmApp.model.Input;
import com.lsntsolutions.gtmApp.model.Role;
import com.lsntsolutions.gtmApp.persistence.dao.AuditDAO;
import com.lsntsolutions.gtmApp.persistence.dao.InputDAO;
import com.lsntsolutions.gtmApp.query.AuditQuery;
import com.lsntsolutions.gtmApp.service.AuditService;
import com.lsntsolutions.gtmApp.service.RoleService;
import com.lsntsolutions.gtmApp.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
@Transactional
public class AuditServiceImpl implements AuditService {

	private static final Logger logger = Logger.getLogger(AuditServiceImpl.class);

	@Autowired
	private AuditDAO auditDAO;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private InputDAO inputService;

	@Override
	public void save(Audit audit) {
		this.auditDAO.save(audit);
	}

	@Override
	public Audit get(Integer id) {
		return this.auditDAO.get(id);
	}

	@Override
	public List<Audit> getAll() {
		return this.auditDAO.getAll();
	}

	@Override
	public void addAudit(String username, Integer roleId, Integer operationId) {
		Audit audit = new Audit();
		Date date = new Date();
		if(roleId == RoleOperation.DELIVERY_NOTE_PRINT.getId()){
			date = new Date( System.currentTimeMillis() + 1000L);
		}
		audit.setDate(date);
		audit.setOperationId(operationId);
		Role role = this.roleService.get(roleId);
		audit.setRole(role);
		audit.setUser(this.userService.getByName(username));

		this.save(audit);
		logger.info("Se ha registrado la Operacion: " + role.getDescription().toUpperCase() + ", con Identificador Nro.: " + audit.getOperationId()
				+ " realizada por: " + username);
	}

	@Override
	public List<Audit> getAuditForSearch(AuditQuery auditQuery) {
		return this.auditDAO.getAuditForSearch(auditQuery);
	}

	@Override
	public boolean getCountAuditSearch(AuditQuery auditQuery) {
		return this.auditDAO.getCountAuditSearch(auditQuery);
	}

	@Override
	public AuditResultDTO getAudit(Integer productId, String serialNumber) {
		return this.auditDAO.getAudit(productId, serialNumber);
	}

	@Override
	public AuditResultDTO getAudit(Integer productId, String batch, String expirateDate) {
		return this.auditDAO.getAudit(productId, batch, expirateDate);
	}

	@Override
	public OutputOrderResultDTO getOutputOrOrder(Integer productId, String serialNumber) throws ParseException {
		OutputOrderResultDTO outputOrderResultDTO = new OutputOrderResultDTO();
		AuditResultDTO auditResultDTO = this.getAudit(productId, serialNumber);
		List<AuditDTO> orders = auditResultDTO.getOrders();
		List<AuditDTO> inputs = auditResultDTO.getInputs();
		List<AuditDTO> outputs = auditResultDTO.getOutputs();

		// Si egresos y armados no existen, no hay devolucion.
		if (outputs.isEmpty() && orders.isEmpty()) {
			outputOrderResultDTO.setOutputId(null);
			outputOrderResultDTO.setOrderId(null);
		}
		// Si egresos existen, pero no armados verifico que sea lo ultimo y devuelvo los egresos.
		// EGRESO.
		else if (!outputs.isEmpty() && orders.isEmpty()) {
			if (inputs.isEmpty()) {
				outputOrderResultDTO.setOutputId(outputs.get(0).getOperationId());
				outputOrderResultDTO.setOrderId(null);
			} else {
				SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date outputDate = dateFormatter.parse(outputs.get(0).getDate());
				Date inputDate = dateFormatter.parse(inputs.get(0).getDate());
				Input input = inputService.get(inputs.get(0).getOperationId());
				if (outputDate.after(inputDate) || input.isCancelled()) {
					outputOrderResultDTO.setOutputId(outputs.get(0).getOperationId());
					outputOrderResultDTO.setOrderId(null);
				}
			}
		}
		// Si armados existen, pero no egresos verifico que sea lo ultimo y devuelvo los armados.
		// ARMADO.
		else if (outputs.isEmpty() && !orders.isEmpty()) {
			if (inputs.isEmpty()) {
				outputOrderResultDTO.setOutputId(null);
				outputOrderResultDTO.setOrderId(orders.get(0).getOperationId());
			} else {
				SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date orderDate = dateFormatter.parse(orders.get(0).getDate());
				Date inputDate = dateFormatter.parse(inputs.get(0).getDate());
				Input input = inputService.get(inputs.get(0).getOperationId());
				if (orderDate.after(inputDate) || input.isCancelled()) {
					outputOrderResultDTO.setOutputId(null);
					outputOrderResultDTO.setOrderId(orders.get(0).getOperationId());
				}
			}
		}
		// Si ambos egresos y armados existen, verifico cual es el ultimo y lo devuelvo.
		// EGRESO o ARMADO.
		else {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			if (inputs.isEmpty()) {
				Date outputDate = dateFormatter.parse(outputs.get(0).getDate());
				Date orderDate = dateFormatter.parse(orders.get(0).getDate());
				if (outputDate.after(orderDate)) {
					outputOrderResultDTO.setOutputId(outputs.get(0).getOperationId());
					outputOrderResultDTO.setOrderId(null);
				} else {
					outputOrderResultDTO.setOutputId(null);
					outputOrderResultDTO.setOrderId(orders.get(0).getOperationId());
				}
			} else {
				Date outputDate = dateFormatter.parse(outputs.get(0).getDate());
				Date orderDate = dateFormatter.parse(orders.get(0).getDate());
				Date inputDate = dateFormatter.parse(inputs.get(0).getDate());
				Input input = inputService.get(inputs.get(0).getOperationId());
				if ((outputDate.after(inputDate) && outputDate.after(orderDate)) || input.isCancelled()) {
					outputOrderResultDTO.setOutputId(outputs.get(0).getOperationId());
					outputOrderResultDTO.setOrderId(null);
				}
				if ((orderDate.after(inputDate) && orderDate.after(outputDate)) || input.isCancelled()) {
					outputOrderResultDTO.setOutputId(null);
					outputOrderResultDTO.setOrderId(orders.get(0).getOperationId());
				}
			}
		}
		return outputOrderResultDTO;
	}

	@Override
	public Date getDate(RoleOperation roleOperation, Integer operationId) {
		return this.auditDAO.getDate(roleOperation,operationId);
	}
}

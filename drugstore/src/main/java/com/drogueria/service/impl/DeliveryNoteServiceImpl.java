package com.drogueria.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.constant.AuditState;
import com.drogueria.constant.RoleOperation;
import com.drogueria.model.DeliveryNote;
import com.drogueria.model.Order;
import com.drogueria.model.Output;
import com.drogueria.model.Supplying;
import com.drogueria.persistence.dao.DeliveryNoteDAO;
import com.drogueria.query.DeliveryNoteQuery;
import com.drogueria.service.AuditService;
import com.drogueria.service.DeliveryNoteService;
import com.drogueria.service.OutputService;
import com.drogueria.service.SupplyingService;
import com.drogueria.service.TraceabilityService;
import com.drogueria.util.OperationResult;

@Service
@Transactional
public class DeliveryNoteServiceImpl implements DeliveryNoteService {

	private static final Logger logger = Logger.getLogger(DeliveryNoteServiceImpl.class);

	@Autowired
	private DeliveryNoteDAO deliveryNoteDAO;
	@Autowired
	private TraceabilityService traceabilityService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private OutputService outputService;
	@Autowired
	private SupplyingService supplyingService;

	@Override
	public DeliveryNote get(Integer id) {
		return this.deliveryNoteDAO.get(id);
	}

	@Override
	public List<DeliveryNote> getAll() {
		return this.deliveryNoteDAO.getAll();
	}

	@Override
	public Map<Integer, List<String>> getAssociatedOrders(boolean informAnmat) {
		return this.deliveryNoteDAO.getAssociatedOrders(informAnmat);
	}

	@Override
	public Map<Integer, List<DeliveryNote>> getAssociatedOrders() {
		return this.deliveryNoteDAO.getAssociatedOrders();
	}

	@Override
	public Map<Integer, List<String>> getAssociatedOutputs(boolean informAnmat) {
		return this.deliveryNoteDAO.getAssociatedOutputs(informAnmat);
	}

	@Override
	public Map<Integer, List<DeliveryNote>> getAssociatedOutputs() {
		return this.deliveryNoteDAO.getAssociatedOutputs();
	}

	@Override
	public void save(DeliveryNote deliveryNote) throws Exception {
		this.deliveryNoteDAO.save(deliveryNote);
		logger.info("Se han guardado los cambios exitosamente. Id de Remito: " + deliveryNote.getId());
	}

	@Override
	public DeliveryNote getDeliveryNoteFromNumber(String deliveryNoteNumber) {
		return this.deliveryNoteDAO.getDeliveryNoteFromNumber(deliveryNoteNumber);
	}

	@Override
	public List<DeliveryNote> getDeliveryNoteFromOrderForSearch(DeliveryNoteQuery deliveryNoteQuery) {
		return this.deliveryNoteDAO.getDeliveryNoteFromOrderForSearch(deliveryNoteQuery);
	}

	@Override
	public List<DeliveryNote> getDeliveryNoteFromOutputForSearch(DeliveryNoteQuery deliveryNoteQuery) {
		return this.deliveryNoteDAO.getDeliveryNoteFromOutputForSearch(deliveryNoteQuery);
	}

	@Override
	public List<DeliveryNote> getDeliveryNoteFromSupplyingForSearch(DeliveryNoteQuery deliveryNoteQuery) {
		return this.deliveryNoteDAO.getDeliveryNoteFromSupplyingForSearch(deliveryNoteQuery);
	}

	@Override
	public Order getOrder(DeliveryNote deliveryNote) {
		return this.deliveryNoteDAO.gerOrder(deliveryNote);
	}

	@Override
	public Output getOutput(DeliveryNote deliveryNote) {
		return this.deliveryNoteDAO.gerOutput(deliveryNote);
	}

	@Override
	public Supplying getSupplying(DeliveryNote deliveryNote) {
		return this.deliveryNoteDAO.getSupplying(deliveryNote);
	}

	@Override
	@Async
	public void sendTrasactionAsync(DeliveryNote deliveryNote) throws Exception {
		Order order = this.getOrder(deliveryNote);
		Output output = this.getOutput(deliveryNote);
		Supplying supplying = this.getSupplying(deliveryNote);

		OperationResult result = this.traceabilityService.processDeliveryNotePendingTransactions(deliveryNote, order, output, supplying);
		if (result != null) {
			// Si no hubo errores se setea el codigo de transaccion del ingreso y se ingresa la mercaderia en stock.
			if (result.getResultado()) {
				deliveryNote.setTransactionCodeANMAT(result.getCodigoTransaccion());
				deliveryNote.setInformed(true);
				this.save(deliveryNote);
			}
		}
	}

	@Override
	public OperationResult saveAndInform(DeliveryNote deliveryNote) throws Exception {
		this.save(deliveryNote);
		Order order = this.getOrder(deliveryNote);
		Output output = this.getOutput(deliveryNote);
		Supplying supplying = this.getSupplying(deliveryNote);

		OperationResult result = this.traceabilityService.processDeliveryNotePendingTransactions(deliveryNote, order, output, supplying);
		if (result != null) {
			// Si no hubo errores se setea el codigo de transaccion del ingreso y se ingresa la mercaderia en stock.
			if (result.getResultado()) {
				deliveryNote.setTransactionCodeANMAT(result.getCodigoTransaccion());
				deliveryNote.setInformed(true);
				this.save(deliveryNote);
			}
		}
		result.setOperationId(deliveryNote.getId());
		return result;
	}

	@Override
	public void authorizeWithoutInform(List<Integer> deliveryNoteIds, String name) {
		for (Integer deliveryNoteId : deliveryNoteIds) {
			DeliveryNote deliveryNote = this.get(deliveryNoteId);
			deliveryNote.setInformed(true);
			try {
				this.save(deliveryNote);
			} catch (Exception e) {
				logger.info("No se ha podido actualizar el estado al remito " + deliveryNoteId);
			}
		}
	}

	@Override
	public void cancelDeliveryNotes(List<String> deliveryNoteNumbers, String username) {
		Output output = null;
		Supplying supplying = null;

		for (String deliveryNoteNumber : deliveryNoteNumbers) {
			DeliveryNote deliveryNote = this.getDeliveryNoteFromNumber(deliveryNoteNumber);
			deliveryNote.setCancelled(true);
			try {
				try {
					if (deliveryNote.isInformAnmat() && deliveryNote.isInformed()) {
						this.traceabilityService.cancelDeliveryNoteTransaction(deliveryNote);
					}
				} catch (Exception e) {
					logger.info("No se ha podido informar la cancelacion a ANMAT " + deliveryNote.getId());
					e.printStackTrace();
				}
				this.save(deliveryNote);
			} catch (Exception e) {
				logger.info("No se ha podido actualizar el estado al remito " + deliveryNote.getId());
			}

			if (output == null) {
				output = this.getOutput(deliveryNote);
				if (output != null) {
					this.outputService.cancel(output);
				}
			}

			if (supplying == null) {
				supplying = this.getSupplying(deliveryNote);
				if (supplying != null) {
					this.supplyingService.cancel(supplying);
				}
			}

			this.auditService.addAudit(username, RoleOperation.DELIVERY_NOTE_CANCELLATION.getId(), AuditState.CANCELLED, deliveryNote.getId());
		}
	}

	@Override
	public Map<Integer, List<String>> getAssociatedSupplyings(boolean informAnmat) {
		return this.deliveryNoteDAO.getAssociatedSupplyings(informAnmat);
	}

	@Override
	public Map<Integer, List<DeliveryNote>> getAssociatedSupplyings() {
		return this.deliveryNoteDAO.getAssociatedSupplyings();
	}

	@Override
	public List<String> getSupplyingsDeliveriesNoteNumbers(Integer supplyingId){
		return this.deliveryNoteDAO.getSupplyingsDeliveriesNoteNumbers(supplyingId);
	}
}

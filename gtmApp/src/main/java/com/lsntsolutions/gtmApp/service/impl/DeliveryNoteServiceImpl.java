package com.lsntsolutions.gtmApp.service.impl;

import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.persistence.dao.DeliveryNoteDAO;
import com.lsntsolutions.gtmApp.query.DeliveryNoteQuery;
import com.lsntsolutions.gtmApp.service.*;
import com.lsntsolutions.gtmApp.util.OperationResult;
import com.lsntsolutions.gtmApp.model.DeliveryNote;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
	@Autowired
	private OrderService orderService;

	@Override
	public DeliveryNote get(Integer id) {
		return this.deliveryNoteDAO.get(id);
	}

	@Override
	public List<DeliveryNote> getAll() {
		return this.deliveryNoteDAO.getAll();
	}

	@Override
	public Map<String, List<String>> getAssociatedOrders(boolean informAnmat, String deliveryNoteNumber) {
		return this.deliveryNoteDAO.getAssociatedOrders(informAnmat, deliveryNoteNumber);
	}

	@Override
	public Map<Integer, List<DeliveryNote>> getAssociatedOrders() {
		return this.deliveryNoteDAO.getAssociatedOrders();
	}

	@Override
	public Map<String, List<String>> getAssociatedOutputs(boolean informAnmat, String deliveryNoteNumber) {
		return this.deliveryNoteDAO.getAssociatedOutputs(informAnmat, deliveryNoteNumber);
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
		result.setOperationId(deliveryNote.getNumber());
		return result;
	}

	@Override
	public void authorizeWithoutInform(Map<String,String> deliveryNoteIds, String name) {
		for (String deliveryNoteId : deliveryNoteIds.keySet()) {
			DeliveryNote deliveryNote = this.getDeliveryNoteFromNumber(deliveryNoteId);
			deliveryNote.setInformed(true);
			String transactionCode = deliveryNoteIds.get(deliveryNoteId) == "" ? null : deliveryNoteIds.get(deliveryNoteId);
			deliveryNote.setTransactionCodeANMAT(transactionCode);
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
		Order order = null;

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
	public Map<String, List<String>> getAssociatedSupplyings(boolean informAnmat, String deliveryNoteNumber) {
		return this.deliveryNoteDAO.getAssociatedSupplyings(informAnmat, deliveryNoteNumber);
	}

	@Override
	public Map<Integer, List<DeliveryNote>> getAssociatedSupplyings() {
		return this.deliveryNoteDAO.getAssociatedSupplyings();
	}


	@Override
	public List<String> getSupplyingsDeliveriesNoteNumbers(Integer supplyingId){
		return this.deliveryNoteDAO.getSupplyingsDeliveriesNoteNumbers(supplyingId);
	}

	@Override
	public List<String> getOutputsDeliveriesNoteNumbers(Integer outputId){
		return this.deliveryNoteDAO.getOutputsDeliveriesNoteNumbers(outputId);
	}

	@Override
	public List<String> getOrdersDeliveriesNoteNumbers(Integer orderId){
		return this.deliveryNoteDAO.getOrdersDeliveriesNoteNumbers(orderId);
	}
}

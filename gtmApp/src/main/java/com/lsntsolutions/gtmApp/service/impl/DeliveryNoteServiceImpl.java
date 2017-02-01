package com.lsntsolutions.gtmApp.service.impl;

import com.inssjp.mywebservice.business.WebServiceError;
import com.inssjp.mywebservice.business.WebServiceResult;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.constant.State;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.persistence.dao.DeliveryNoteDAO;
import com.lsntsolutions.gtmApp.query.DeliveryNoteQuery;
import com.lsntsolutions.gtmApp.service.*;
import com.lsntsolutions.gtmApp.util.CancelDeliveryNoteResult;
import com.lsntsolutions.gtmApp.util.OperationResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
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
	private ProvisioningRequestStateService provisioningRequestStateService;
	@Autowired
	private ProvisioningRequestService provisioningRequestService;

	@Override
	public DeliveryNote get(Integer id) {
		return this.deliveryNoteDAO.get(id);
	}

	@Override
	public List<DeliveryNote> getAll() {
		return this.deliveryNoteDAO.getAll();
	}

	@Override
	public Map<String, List<DeliveryNote>> getAssociatedOrders(boolean informAnmat, String deliveryNoteNumber) {
		return this.deliveryNoteDAO.getAssociatedOrders(informAnmat, deliveryNoteNumber);
	}

	@Override
	public Map<Integer, List<DeliveryNote>> getAssociatedOrders() {
		return this.deliveryNoteDAO.getAssociatedOrders();
	}

	@Override
	public Map<String, List<DeliveryNote>> getAssociatedOutputs(boolean informAnmat, String deliveryNoteNumber) {
		return this.deliveryNoteDAO.getAssociatedOutputs(informAnmat, deliveryNoteNumber);
	}

	@Override
	public Map<Integer, List<DeliveryNote>> getAssociatedOutputs() {
		return this.deliveryNoteDAO.getAssociatedOutputs();
	}

	@Override
	public DeliveryNote save(DeliveryNote deliveryNote) throws Exception {
		return this.deliveryNoteDAO.save(deliveryNote);
		//logger.info("Se han guardado los cambios exitosamente. Id de Remito: " + deliveryNote.getId());
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
		return this.deliveryNoteDAO.getOrder(deliveryNote);
	}

	@Override
	public Output getOutput(DeliveryNote deliveryNote) {
		return this.deliveryNoteDAO.getOutput(deliveryNote);
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
		saveResult(deliveryNote, result);
	}

	private void saveResult(DeliveryNote deliveryNote, OperationResult result) throws Exception {
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
		deliveryNote = this.save(deliveryNote);
		Order order = this.getOrder(deliveryNote);
		Output output = this.getOutput(deliveryNote);
		Supplying supplying = this.getSupplying(deliveryNote);

		OperationResult result = this.traceabilityService.processDeliveryNotePendingTransactions(deliveryNote, order, output, supplying);
		saveResult(deliveryNote, result);
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
	public List<CancelDeliveryNoteResult> cancelDeliveryNotes(List<String> deliveryNoteNumbers, String username) {
		Output output = null;
		Supplying supplying = null;
		WebServiceResult result = null;
		List<CancelDeliveryNoteResult> results = new ArrayList<>();
		Order order = null;
		for (String deliveryNoteNumber : deliveryNoteNumbers) {
			DeliveryNote deliveryNote = this.getDeliveryNoteFromNumber(deliveryNoteNumber);
			try {
				try {
					CancelDeliveryNoteResult cancelDeliveryNoteResult = null;
					if (deliveryNote.isInformAnmat() && deliveryNote.isInformed()) {
						result = this.traceabilityService.cancelDeliveryNoteTransaction(deliveryNote);
						if((result != null && result.getResultado()) ) {
							cancelDeliveryNoteResult = new CancelDeliveryNoteResult(result);
							cancelDeliveryNoteResult.setDeliveryNoteNumber(deliveryNoteNumber);
						}
					}
					if((result != null && result.getResultado()) || !deliveryNote.isInformAnmat() || (deliveryNote.isInformAnmat() && !deliveryNote.isInformed())) {
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
						if(order == null){
							order = this.getOrder(deliveryNote);
							if(order != null){
								ProvisioningRequest provisioningRequest = order.getProvisioningRequest();
								provisioningRequest.setState(this.provisioningRequestStateService.get(State.ASSEMBLED.getId()));
								this.provisioningRequestService.save(provisioningRequest);
							}
						}

						deliveryNote.setCancelled(true);

						if(!deliveryNote.isInformAnmat() || (deliveryNote.isInformAnmat() && !deliveryNote.isInformed())){
							cancelDeliveryNoteResult = new CancelDeliveryNoteResult(true);
							cancelDeliveryNoteResult.setDeliveryNoteNumber(deliveryNoteNumber);
						}
						try {
							this.save(deliveryNote);
							logger.info("Se anulo el remito: " + deliveryNote.getId());
						} catch (Exception e) {
							e.printStackTrace();
						}

						this.auditService.addAudit(username, RoleOperation.DELIVERY_NOTE_CANCELLATION.getId(), deliveryNote.getId());
					}else{
						logger.info("No se ha podido anular el remito " + deliveryNote.getId());
						logger.info("Error en ANMAT al anular " + result);
						cancelDeliveryNoteResult = new CancelDeliveryNoteResult(false);
						cancelDeliveryNoteResult.setError("Error en ANMAT al anular ");
						cancelDeliveryNoteResult.setDeliveryNoteNumber(deliveryNoteNumber);
					}
					results.add(cancelDeliveryNoteResult);
				} catch (Exception e) {
					logger.info("No se ha podido informar la cancelacion a ANMAT " + deliveryNote.getId());
					e.printStackTrace();
				}
			} catch (Exception e) {
				logger.info("No se ha podido actualizar el estado al remito " + deliveryNote.getId());
			}
		}
		return results;
	}

	@Override
	public Map<String, List<DeliveryNote>> getAssociatedSupplyings(boolean informAnmat, String deliveryNoteNumber) {
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

	@Override
	public Boolean existsDeliveryNoteNumber(Integer deliveryNotePOS, Integer lastDeliveryNoteNumberInput, boolean fake) {
		return this.deliveryNoteDAO.existsDeliveryNoteNumber(deliveryNotePOS, lastDeliveryNoteNumberInput,fake);
	}

	@Override
	public Map<String, List<DeliveryNote>>  getDeliveryNotes(String deliveryNoteNumber){
		Map<String, List<DeliveryNote>> canceleablesMap = new HashMap<>();

		DeliveryNote deliveryNote = this.getDeliveryNoteFromNumber(deliveryNoteNumber);
		Map<String, List<DeliveryNote>> orderDeliveryNotes = new HashMap<>();
		Map<String, List<DeliveryNote>> outputDeliveryNotes = new HashMap<>();
		Map<String, List<DeliveryNote>> supplyingDeliveryNotes = new HashMap<>();

		if(deliveryNote != null) {
			Order order = this.getOrder(deliveryNote);
			if (order != null) {
				List<DeliveryNote> deliveryNotes = this.deliveryNoteDAO.getDeliveryNoteByOrderId(order.getId());
				orderDeliveryNotes.put("A" + order.getId(), deliveryNotes);
			}
			Supplying supplying = this.getSupplying(deliveryNote);
			if (supplying != null) {
				List<DeliveryNote> deliveryNotes = this.deliveryNoteDAO.getDeliveryNoteBySupplyingId(supplying.getId());
				supplyingDeliveryNotes.put("D" + supplying.getId(), deliveryNotes);
			}
			Output output = this.getOutput(deliveryNote);
			if (output != null) {
				List<DeliveryNote> deliveryNotes = this.deliveryNoteDAO.getDeliveryNoteByOutpuId(output.getId());
				outputDeliveryNotes.put("E" + output.getId(), deliveryNotes);
			}
		}

		canceleablesMap.putAll(orderDeliveryNotes);
		canceleablesMap.putAll(outputDeliveryNotes);
		canceleablesMap.putAll(supplyingDeliveryNotes);

		return canceleablesMap;
	}

	@Override
	public Boolean isCancelled(String deliveryNoteNumber) {
		return this.deliveryNoteDAO.isCancelled(deliveryNoteNumber);
	}
}

package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.model.Order;
import com.lsntsolutions.gtmApp.model.Output;
import com.lsntsolutions.gtmApp.model.Supplying;
import com.lsntsolutions.gtmApp.query.DeliveryNoteQuery;
import com.lsntsolutions.gtmApp.util.OperationResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DeliveryNoteService {

	DeliveryNote get(Integer id);

	List<DeliveryNote> getAll();

	Map<String, List<String>> getAssociatedOrders(boolean informAnmat, String deliveryNoteNumber);

	Map<Integer, List<DeliveryNote>> getAssociatedOrders();

	Map<String, List<String>> getAssociatedOutputs(boolean informAnmat, String deliveryNoteNumber);

	Map<Integer, List<DeliveryNote>> getAssociatedOutputs();

	Map<String, List<String>> getAssociatedSupplyings(boolean informAnmat, String deliveryNoteNumber);

	Map<Integer, List<DeliveryNote>> getAssociatedSupplyings();

	void save(DeliveryNote deliveryNote) throws Exception;

	DeliveryNote getDeliveryNoteFromNumber(String deliveryNoteNumber);

	List<DeliveryNote> getDeliveryNoteFromOrderForSearch(DeliveryNoteQuery deliveryNoteQuery);

	List<DeliveryNote> getDeliveryNoteFromOutputForSearch(DeliveryNoteQuery deliveryNoteQuery);

    List<DeliveryNote> getDeliveryNoteFromSupplyingForSearch(DeliveryNoteQuery deliveryNoteQuery);

    Order getOrder(DeliveryNote deliveryNote);

	Output getOutput(DeliveryNote deliveryNote);

	Supplying getSupplying(DeliveryNote deliveryNote);

	OperationResult saveAndInform(DeliveryNote deliveryNote) throws Exception;

	void sendTrasactionAsync(DeliveryNote deliveryNote) throws Exception;

	void authorizeWithoutInform(Map<String,String> deliveryNoteIds, String name);

	void cancelDeliveryNotes(HashMap<String, List<String>> deliveryNoteNumbers, String username) throws Exception;

	List<String> getSupplyingsDeliveriesNoteNumbers(Integer supplyingId);

	List<String> getOutputsDeliveriesNoteNumbers(Integer supplyingId);
}

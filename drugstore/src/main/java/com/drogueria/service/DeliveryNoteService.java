package com.drogueria.service;

import java.util.List;
import java.util.Map;

import com.drogueria.model.DeliveryNote;
import com.drogueria.model.Order;
import com.drogueria.model.Output;
import com.drogueria.model.Supplying;
import com.drogueria.query.DeliveryNoteQuery;
import com.drogueria.util.OperationResult;

public interface DeliveryNoteService {

	DeliveryNote get(Integer id);

	List<DeliveryNote> getAll();

	Map<Integer, List<String>> getAssociatedOrders(boolean informAnmat);

	Map<Integer, List<DeliveryNote>> getAssociatedOrders();

	Map<Integer, List<String>> getAssociatedOutputs(boolean informAnmat);

	Map<Integer, List<DeliveryNote>> getAssociatedOutputs();

	Map<Integer, List<String>> getAssociatedSupplyings(boolean informAnmat);

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

	void cancelDeliveryNotes(List<String> deliveryNoteNumbers, String username);

	List<String> getSupplyingsDeliveriesNoteNumbers(Integer supplyingId);

	List<String> getOutputsDeliveriesNoteNumbers(Integer supplyingId);
}

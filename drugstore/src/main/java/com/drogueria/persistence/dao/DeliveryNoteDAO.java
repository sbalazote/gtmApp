package com.drogueria.persistence.dao;

import com.drogueria.model.DeliveryNote;
import com.drogueria.model.Order;
import com.drogueria.model.Output;
import com.drogueria.model.Supplying;
import com.drogueria.query.DeliveryNoteQuery;

import java.util.List;
import java.util.Map;

public interface DeliveryNoteDAO {

	DeliveryNote get(Integer id);

	List<DeliveryNote> getAll();

	Map<String, List<String>> getAssociatedOrders(boolean informAnmat, String deliveryNoteNumber);

	Map<Integer, List<DeliveryNote>> getAssociatedOrders();

	Map<String, List<String>> getAssociatedOutputs(boolean informAnmat, String deliveryNoteNumber);

	Map<Integer, List<DeliveryNote>> getAssociatedOutputs();

	void save(DeliveryNote deliveryNote);

	DeliveryNote getDeliveryNoteFromNumber(String deliveryNoteNumber);

	Order gerOrder(DeliveryNote deliveryNote);

	Output gerOutput(DeliveryNote deliveryNote);

	Supplying getSupplying(DeliveryNote deliveryNote);

	List<DeliveryNote> getDeliveryNoteFromOrderForSearch(DeliveryNoteQuery deliveryNoteQuery);

	List<DeliveryNote> getDeliveryNoteFromOutputForSearch(DeliveryNoteQuery deliveryNoteQuery);

    List<DeliveryNote> getDeliveryNoteFromSupplyingForSearch(DeliveryNoteQuery deliveryNoteQuery);

	Map<String, List<String>> getAssociatedSupplyings(boolean informAnmat, String deliveryNoteNumber);

	Map<Integer, List<DeliveryNote>> getAssociatedSupplyings();

	List<String> getSupplyingsDeliveriesNoteNumbers(Integer supplyingId);

    List<String> getOutputsDeliveriesNoteNumbers(Integer outputId);
}

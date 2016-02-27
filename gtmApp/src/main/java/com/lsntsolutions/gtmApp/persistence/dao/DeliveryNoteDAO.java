package com.lsntsolutions.gtmApp.persistence.dao;

import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.model.Order;
import com.lsntsolutions.gtmApp.model.Output;
import com.lsntsolutions.gtmApp.model.Supplying;
import com.lsntsolutions.gtmApp.query.DeliveryNoteQuery;

import java.util.List;
import java.util.Map;

public interface DeliveryNoteDAO {

	DeliveryNote get(Integer id);

	List<DeliveryNote> getAll();

	Map<String, List<DeliveryNote>> getAssociatedOrders(boolean informAnmat, String deliveryNoteNumber);

	Map<Integer, List<DeliveryNote>> getAssociatedOrders();

	Map<String, List<DeliveryNote>> getAssociatedOutputs(boolean informAnmat, String deliveryNoteNumber);

	Map<Integer, List<DeliveryNote>> getAssociatedOutputs();

	Map<String, List<DeliveryNote>> getAssociatedSupplyings(boolean informAnmat, String deliveryNoteNumber);

	Map<Integer, List<DeliveryNote>> getAssociatedSupplyings();

	void save(DeliveryNote deliveryNote);

	DeliveryNote getDeliveryNoteFromNumber(String deliveryNoteNumber);

	Order gerOrder(DeliveryNote deliveryNote);

	Output gerOutput(DeliveryNote deliveryNote);

	Supplying getSupplying(DeliveryNote deliveryNote);

	List<DeliveryNote> getDeliveryNoteFromOrderForSearch(DeliveryNoteQuery deliveryNoteQuery);

	List<DeliveryNote> getDeliveryNoteFromOutputForSearch(DeliveryNoteQuery deliveryNoteQuery);

    List<DeliveryNote> getDeliveryNoteFromSupplyingForSearch(DeliveryNoteQuery deliveryNoteQuery);

	List<String> getSupplyingsDeliveriesNoteNumbers(Integer supplyingId);

    List<String> getOutputsDeliveriesNoteNumbers(Integer outputId);

	List<String> getOrdersDeliveriesNoteNumbers(Integer orderId);

	Boolean existsDeliveryNoteNumber(Integer deliveryNotePOS, Integer lastDeliveryNoteNumberInput, boolean fake);
}

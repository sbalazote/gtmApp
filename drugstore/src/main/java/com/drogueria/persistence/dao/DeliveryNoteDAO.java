package com.drogueria.persistence.dao;

import java.util.List;
import java.util.Map;

import com.drogueria.model.DeliveryNote;
import com.drogueria.model.Order;
import com.drogueria.model.Output;
import com.drogueria.model.Supplying;
import com.drogueria.query.DeliveryNoteQuery;

public interface DeliveryNoteDAO {

	DeliveryNote get(Integer id);

	List<DeliveryNote> getAll();

	Map<Integer, List<String>> getAssociatedOrders(boolean informAnmat);

	Map<Integer, List<String>> getAssociatedOutputs(boolean informAnmat);

	void save(DeliveryNote deliveryNote);

	DeliveryNote getDeliveryNoteFromNumber(String deliveryNoteNumber);

	Order gerOrder(DeliveryNote deliveryNote);

	Output gerOutput(DeliveryNote deliveryNote);

	Supplying getSupplying(DeliveryNote deliveryNote);

	List<DeliveryNote> getDeliveryNoteFromOrderForSearch(DeliveryNoteQuery deliveryNoteQuery);

	List<DeliveryNote> getDeliveryNoteFromOutputForSearch(DeliveryNoteQuery deliveryNoteQuery);

    List<DeliveryNote> getDeliveryNoteFromSupplyingForSearch(DeliveryNoteQuery deliveryNoteQuery);

	Map<Integer, List<String>> getAssociatedSupplyings(boolean informAnmat);

}

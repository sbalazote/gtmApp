package com.drogueria.persistence.dao;

import java.util.List;

import com.drogueria.model.Order;
import com.drogueria.query.DeliveryNoteQuery;

public interface OrderDAO {

	void save(Order order);

	Order get(Integer id);

	Order getOrderByProvisioningRequestId(Integer provisioningRequestId);

	boolean getCountDeliveryNoteSearch(DeliveryNoteQuery deliveryNoteQuery);

	List<Order> getDeliveryNoteSearch(DeliveryNoteQuery deliveryNoteQuery);

	List<Order> getAllByState(Integer stateId);

	boolean existSerial(Integer productId, String serial);

	List<Order> getAllFilter(Integer agreementId, Integer clientId, Integer stateId);
}
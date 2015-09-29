package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.dto.OrderDTO;
import com.lsntsolutions.gtmApp.model.Order;
import com.lsntsolutions.gtmApp.query.DeliveryNoteQuery;

public interface OrderService {

	Order save(OrderDTO orderDTO);

	Order get(Integer id);

	Order getOrderByProvisioningRequestId(Integer provisioningRequestId);

	boolean getCountDeliveryNoteSearch(DeliveryNoteQuery deliveryNoteQuery);

	List<Order> getDeliveryNoteSearch(DeliveryNoteQuery deliveryNoteQuery);

	List<Order> getAllByState(Integer stateId);

	void cancelOrders(List<Integer> ordersId);

	boolean existSerial(Integer productId, String serial);

	public List<Order> getAllFilter(Integer agreementId, Integer clientId, Integer stateId);

	void reassignOperators(List<Integer> ordersToPrint, Integer logisticOperatorId);
}

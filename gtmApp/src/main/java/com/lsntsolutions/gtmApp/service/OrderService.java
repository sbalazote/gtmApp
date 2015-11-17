package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.dto.OrderDTO;
import com.lsntsolutions.gtmApp.model.Order;
import com.lsntsolutions.gtmApp.query.DeliveryNoteQuery;

import java.util.List;

public interface OrderService {

	Order save(OrderDTO orderDTO);

	Order get(Integer id);

	Order getOrderByProvisioningRequestId(Integer provisioningRequestId);

	boolean getCountDeliveryNoteSearch(DeliveryNoteQuery deliveryNoteQuery);

	List<Order> getDeliveryNoteSearch(DeliveryNoteQuery deliveryNoteQuery);

	List<Order> getAllByState(Integer stateId);

	void cancel(Order order);

	void cancelOrders(List<Integer> orderIds);

	boolean existSerial(Integer productId, String serial);

	List<Order> getAllFilter(Integer agreementId, Integer clientId, Integer stateId);

	void reassignOperators(List<Integer> ordersToPrint, Integer logisticOperatorId);

	void changeToPrintState(Order order);
}

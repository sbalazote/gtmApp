package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.dto.OrderDTO;
import com.lsntsolutions.gtmApp.dto.PrinterResultDTO;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.query.DeliveryNoteQuery;

import java.util.List;

public interface OrderService {

	Order save(OrderDTO orderDTO);

	Order save(Order order);

	Order get(Integer id);

	Order getOrderByProvisioningRequestId(Integer provisioningRequestId);

	boolean getCountDeliveryNoteSearch(DeliveryNoteQuery deliveryNoteQuery);

	List<Order> getAllByState(Integer stateId);

	void cancel(Order order);

	void cancelOrders(List<Integer> orderIds);

	List<Order> getAllFilter(Integer provisioningRequestId, Integer agreementId, Integer logisticsOperatorId, Integer clientId, Integer deliveryLocationId, Integer stateId);

	List<Integer> filterAlreadyPrinted(List<Integer> ordersToPrint, PrinterResultDTO printerResultDTO);

	List<Agreement> getAgreementForOrderToPrint();

	List<Client> getClientForOrderToPrint();

	List<DeliveryLocation> getDeliveryLocationsForOrderToPrint();

	List<LogisticsOperator> getLogisticsOperatorsForOrderToPrint();

	List<Order> getPrintableOrCancelableOrder(Integer provisioningId, boolean isCancellation);

	String searchSerialNumberOnOutput(Integer outputId, String serialNumber);
}

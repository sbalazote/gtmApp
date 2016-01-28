package com.lsntsolutions.gtmApp.persistence.dao;

import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.query.DeliveryNoteQuery;

import java.util.List;

public interface OrderDAO {

	void save(Order order);

	Order get(Integer id);

	Order getOrderByProvisioningRequestId(Integer provisioningRequestId);

	boolean getCountDeliveryNoteSearch(DeliveryNoteQuery deliveryNoteQuery);

	List<Order> getDeliveryNoteSearch(DeliveryNoteQuery deliveryNoteQuery);

	List<Order> getAllByState(Integer stateId);

	boolean existSerial(Integer productId, String serial);

	List<Order> getAllFilter(Integer provisioningRequestId, Integer agreementId, Integer logisticsOperatorId, Integer clientId, Integer deliveryLocationId, Integer stateId);

	List<Agreement> getAgreementForOrderToPrint();

	List<Client> getClientForOrderToPrint();

	List<DeliveryLocation> getDeliveryLocationsForOrderToPrint();

	List<LogisticsOperator> getLogisticsOperatorsForOrderToPrint();

	List<Order> getPrintableOrCancelableOrder(Integer provisioningId, boolean isCancellation);
}
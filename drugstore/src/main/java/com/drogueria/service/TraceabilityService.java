package com.drogueria.service;

import com.drogueria.model.DeliveryNote;
import com.drogueria.model.Input;
import com.drogueria.model.Order;
import com.drogueria.model.Output;
import com.drogueria.model.Supplying;
import com.drogueria.util.OperationResult;
import com.inssjp.mywebservice.business.WebServiceResult;

public interface TraceabilityService {

	public WebServiceResult cancelInputTransaction(Input input) throws Exception;

	public OperationResult processInputPendingTransactions(Input input) throws Exception;

	OperationResult processDeliveryNotePendingTransactions(DeliveryNote deliveryNote, Order order, Output output, Supplying supplying) throws Exception;

	WebServiceResult cancelDeliveryNoteTransaction(DeliveryNote deliveryNote) throws Exception;

}

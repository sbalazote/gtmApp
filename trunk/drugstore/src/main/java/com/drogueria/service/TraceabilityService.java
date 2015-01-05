package com.drogueria.service;

import com.drogueria.model.DeliveryNote;
import com.drogueria.model.Input;
import com.drogueria.model.Order;
import com.drogueria.model.Output;
import com.drogueria.util.OperationResult;
import com.inssjp.mywebservice.business.WebServiceResult;

public interface TraceabilityService {

	public WebServiceResult cancelInputTransaction(Input input) throws Exception;

	public OperationResult processInputPendingTransactions(Input input) throws Exception;

	public WebServiceResult cancelOutputTransaction(Output output) throws Exception;

	OperationResult processOutputPendingTransactions(Output output) throws Exception;

	OperationResult processDeliveryNotePendingTransactions(DeliveryNote deliveryNote, Order order, Output output) throws Exception;

	WebServiceResult cancelDeliveryNoteTransaction(DeliveryNote deliveryNote) throws Exception;

}

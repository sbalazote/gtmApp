package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.model.Input;
import com.lsntsolutions.gtmApp.model.Order;
import com.lsntsolutions.gtmApp.model.Output;
import com.lsntsolutions.gtmApp.model.Supplying;
import com.lsntsolutions.gtmApp.util.OperationResult;
import com.inssjp.mywebservice.business.WebServiceResult;

public interface TraceabilityService {

	WebServiceResult cancelInputTransaction(String transactionCodeANMAT) throws Exception;

	OperationResult processInputPendingTransactions(Input input) throws Exception;

	OperationResult processSelfSerializedInputPendingTransactions(Input input) throws Exception;

	OperationResult processDeliveryNotePendingTransactions(DeliveryNote deliveryNote, Order order, Output output, Supplying supplying) throws Exception;

	WebServiceResult cancelDeliveryNoteTransaction(DeliveryNote deliveryNote) throws Exception;

}

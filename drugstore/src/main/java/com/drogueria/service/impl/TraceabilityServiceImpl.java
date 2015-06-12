package com.drogueria.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.helper.EncryptionHelper;
import com.drogueria.model.DeliveryNote;
import com.drogueria.model.Input;
import com.drogueria.model.Order;
import com.drogueria.model.Output;
import com.drogueria.model.Supplying;
import com.drogueria.service.PropertyService;
import com.drogueria.service.TraceabilityService;
import com.drogueria.util.OperationResult;
import com.drogueria.webservice.WebServiceHelper;
import com.drogueria.webservice.helper.DeliveryNoteWSHelper;
import com.drogueria.webservice.helper.InputWSHelper;
import com.inssjp.mywebservice.business.WebServiceResult;

@Service
@Transactional
public class TraceabilityServiceImpl implements TraceabilityService {

	@Autowired
	private WebServiceHelper webServiceHelper;

	@Autowired
	private InputWSHelper inputWSHelper;

	@Autowired
	private DeliveryNoteWSHelper deliveryNoteWSHelper;

	@Autowired
	private PropertyService PropertyService;

	@Override
	public OperationResult processInputPendingTransactions(Input input) throws Exception {
		OperationResult result = this.inputWSHelper.confirmPendings(input);
		return result;
	}

	@Override
	public WebServiceResult cancelDeliveryNoteTransaction(DeliveryNote deliveryNote) throws Exception {
		Long transactionCodeANMAT = Long.valueOf(deliveryNote.getTransactionCodeANMAT());
		WebServiceResult result = null;
		if (transactionCodeANMAT != null) {
			result = this.webServiceHelper.sendCancelacTransacc(Long.valueOf(transactionCodeANMAT), this.PropertyService.get().getANMATName(),
					EncryptionHelper.AESDecrypt(this.PropertyService.get().getANMATPassword()));
		}
		return result;
	}

	@Override
	public WebServiceResult cancelInputTransaction(Input input) throws Exception {
		//TODO refactorizar esto.
		/*String transactionCodeANMAT = input.getTransactionCodeANMAT();
		WebServiceResult result = null;
		if (transactionCodeANMAT != null) {
			result = this.webServiceHelper.sendCancelacTransacc(Long.valueOf(transactionCodeANMAT), this.PropertyService.get().getANMATName(),
					EncryptionHelper.AESDecrypt(this.PropertyService.get().getANMATPassword()));
		}*/
		return null;
	}

	@Override
	public OperationResult processDeliveryNotePendingTransactions(DeliveryNote deliveryNote, Order order, Output output, Supplying supplying) throws Exception {
		OperationResult result = this.deliveryNoteWSHelper.sendDrugInformationToAnmat(deliveryNote, order, output, supplying);
		return result;
	}
}

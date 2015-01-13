package com.drogueria.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.helper.EncryptionHelper;
import com.drogueria.model.DeliveryNote;
import com.drogueria.model.Input;
import com.drogueria.model.Order;
import com.drogueria.model.Output;
import com.drogueria.service.DrugstorePropertyService;
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
	private DrugstorePropertyService drugstorePropertyService;

	@Override
	public OperationResult processInputPendingTransactions(Input input) throws Exception {
		OperationResult result = this.inputWSHelper.sendDrugInformationToAnmat(input);
		return result;
	}

	@Override
	public WebServiceResult cancelDeliveryNoteTransaction(DeliveryNote deliveryNote) throws Exception {
		String transactionCodeANMAT = deliveryNote.getTransactionCodeANMAT();
		WebServiceResult result = this.webServiceHelper.sendCancelacTransacc(Long.valueOf(transactionCodeANMAT), this.drugstorePropertyService.get()
				.getANMATName(), EncryptionHelper.AESDecrypt(this.drugstorePropertyService.get().getANMATPassword()));
		return result;
	}

	@Override
	public WebServiceResult cancelInputTransaction(Input input) throws Exception {
		long transactionCodeAnmat = Long.valueOf(input.getTransactionCodeANMAT());
		WebServiceResult result = this.webServiceHelper.sendCancelacTransacc(transactionCodeAnmat, this.drugstorePropertyService.get().getANMATName(),
				EncryptionHelper.AESDecrypt(this.drugstorePropertyService.get().getANMATPassword()));
		return result;
	}

	@Override
	public OperationResult processDeliveryNotePendingTransactions(DeliveryNote deliveryNote, Order order, Output output) throws Exception {
		OperationResult result = this.deliveryNoteWSHelper.sendDrugInformationToAnmat(deliveryNote, order, output);
		return result;
	}
}

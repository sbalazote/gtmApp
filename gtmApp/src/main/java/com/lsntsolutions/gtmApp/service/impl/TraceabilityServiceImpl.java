package com.lsntsolutions.gtmApp.service.impl;

import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.helper.EncryptionHelper;
import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.model.Supplying;
import com.lsntsolutions.gtmApp.service.TraceabilityService;
import com.lsntsolutions.gtmApp.util.OperationResult;
import com.lsntsolutions.gtmApp.webservice.WebServiceHelper;
import com.lsntsolutions.gtmApp.webservice.helper.DeliveryNoteWSHelper;
import com.lsntsolutions.gtmApp.webservice.helper.InputWSHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lsntsolutions.gtmApp.model.Input;
import com.lsntsolutions.gtmApp.model.Order;
import com.lsntsolutions.gtmApp.model.Output;
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
	private com.lsntsolutions.gtmApp.service.PropertyService PropertyService;

	@Override
	public OperationResult processInputPendingTransactions(Input input) throws Exception {
		OperationResult result = this.inputWSHelper.sendDrugInformationToAnmat(input, Boolean.valueOf(PropertyProvider.getInstance().getProp(PropertyProvider.IS_PRODUCTION)));
		return result;
	}

	@Override
	public OperationResult processSelfSerializedInputPendingTransactions(Input input) throws Exception {
		OperationResult result = this.inputWSHelper.sendDrugs(input);
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
	public WebServiceResult cancelInputTransaction(String transactionCodeANMAT) throws Exception {
        WebServiceResult result = null;
		if (transactionCodeANMAT != null) {
            result = this.webServiceHelper.sendCancelacTransacc(Long.valueOf(transactionCodeANMAT), this.PropertyService.get().getANMATName(), EncryptionHelper.AESDecrypt(this.PropertyService.get().getANMATPassword()));
        }
		return result;
	}

	@Override
	public OperationResult processDeliveryNotePendingTransactions(DeliveryNote deliveryNote, Order order, Output output, Supplying supplying) throws Exception {
		OperationResult result = this.deliveryNoteWSHelper.sendDrugInformationToAnmat(deliveryNote, order, output, supplying);
		return result;
	}
}

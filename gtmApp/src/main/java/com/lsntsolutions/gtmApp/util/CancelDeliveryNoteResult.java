package com.lsntsolutions.gtmApp.util;

import com.inssjp.mywebservice.business.WebServiceResult;

/**
 * Created by a983060 on 01/02/2017.
 */
public class CancelDeliveryNoteResult extends WebServiceResult {

    private String deliveryNoteNumber;
    private String error;

    public CancelDeliveryNoteResult(WebServiceResult  webServiceResult){
        setFromWebServiceResult(webServiceResult);
    }

    public CancelDeliveryNoteResult(boolean result){
        super.setResultado(result);
    }

    public String getDeliveryNoteNumber() {
        return deliveryNoteNumber;
    }

    public void setDeliveryNoteNumber(String deliveryNoteNumber) {
        this.deliveryNoteNumber = deliveryNoteNumber;
    }

    public void setFromWebServiceResult(WebServiceResult webServiceResult) {
        super.setCodigoTransaccion(webServiceResult.getCodigoTransaccion());
        super.setErrores(webServiceResult.getErrores());
        super.setResultado(webServiceResult.getResultado());
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

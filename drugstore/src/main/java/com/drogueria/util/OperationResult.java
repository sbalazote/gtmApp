package com.drogueria.util;

import java.util.List;

import com.inssjp.mywebservice.business.WebServiceResult;

public class OperationResult extends WebServiceResult {

	private static final long serialVersionUID = 1L;

	private String operationId;
	private List<String> myOwnErrors;

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public List<String> getMyOwnErrors() {
		return this.myOwnErrors;
	}

	public void setMyOwnErrors(List<String> myOwnErrors) {
		this.myOwnErrors = myOwnErrors;
	}

	public void setFromWebServiceResult(WebServiceResult webServiceResult) {
		super.setCodigoTransaccion(webServiceResult.getCodigoTransaccion());
		super.setErrores(webServiceResult.getErrores());
		super.setResultado(webServiceResult.getResultado());
	}
}

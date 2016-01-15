package com.lsntsolutions.gtmApp.util;

import java.util.List;

import com.inssjp.mywebservice.business.WebServiceConfirmResult;
import com.inssjp.mywebservice.business.WebServiceResult;

public class OperationResult extends WebServiceResult {

	private static final long serialVersionUID = 1L;

	private String operationId;
	private List<String> myOwnErrors;
	private List<String> mySelfSerializedOwnErrors;
	private boolean selfSerializedInform;
	private boolean providerSerializedInform;
	private String selfSerializedTransactionCode;

	public List<String> getMySelfSerializedOwnErrors() {
		return mySelfSerializedOwnErrors;
	}

	public void setMySelfSerializedOwnErrors(List<String> mySelfSerializedOwnErrors) {
		this.mySelfSerializedOwnErrors = mySelfSerializedOwnErrors;
	}

	public boolean isSelfSerializedInform() {
		return selfSerializedInform;
	}

	public void setSelfSerializedInform(boolean selfSerializedInform) {
		this.selfSerializedInform = selfSerializedInform;
	}

	public boolean isProviderSerializedInform() {
		return providerSerializedInform;
	}

	public void setProviderSerializedInform(boolean providerSerializedInform) {
		this.providerSerializedInform = providerSerializedInform;
	}

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

	public String getSelfSerializedTransactionCode() {
		return selfSerializedTransactionCode;
	}

	public void setSelfSerializedTransactionCode(String selfSerializedTransactionCode) {
		this.selfSerializedTransactionCode = selfSerializedTransactionCode;
	}

	public void setFromWebServiceResult(WebServiceResult webServiceResult) {
		super.setCodigoTransaccion(webServiceResult.getCodigoTransaccion());
		super.setErrores(webServiceResult.getErrores());
		super.setResultado(webServiceResult.getResultado());
	}

	public void setFromWebServiceConfirmResult(WebServiceConfirmResult webServiceResult) {
		super.setCodigoTransaccion(webServiceResult.getId_transac_asociada());
		super.setErrores(webServiceResult.getErrores());
		super.setResultado(webServiceResult.getResultado());
	}
}

package com.drogueria.dto;

import java.io.Serializable;

public class ProvisioningRequestDetailDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer productId;
	private Integer amount;

	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getAmount() {
		return this.amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

}

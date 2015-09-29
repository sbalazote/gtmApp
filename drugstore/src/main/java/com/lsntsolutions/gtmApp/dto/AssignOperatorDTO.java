package com.lsntsolutions.gtmApp.dto;

import java.util.List;

public class AssignOperatorDTO {
	private Integer logisticOperatorId;
	private List<Integer> ordersIdsToReassign;

	public Integer getLogisticOperatorId() {
		return this.logisticOperatorId;
	}

	public void setLogisticOperatorId(Integer logisticOperatorId) {
		this.logisticOperatorId = logisticOperatorId;
	}

	public List<Integer> getOrdersIdsToReassign() {
		return this.ordersIdsToReassign;
	}

	public void setOrdersIdsToReassign(List<Integer> ordersIdsToReassign) {
		this.ordersIdsToReassign = ordersIdsToReassign;
	}

}

package com.lsntsolutions.gtmApp.dto;

import java.util.List;

public class AssignOperatorDTO {
	private Integer logisticOperatorId;
	private List<Integer> provisioningsIdsToReassign;

	public Integer getLogisticOperatorId() {
		return this.logisticOperatorId;
	}

	public void setLogisticOperatorId(Integer logisticOperatorId) {
		this.logisticOperatorId = logisticOperatorId;
	}

	public List<Integer> getProvisioningsIdsToReassign() {
		return this.provisioningsIdsToReassign;
	}

	public void setProvisioningsIdsToReassign(List<Integer> provisioningsIdsToReassign) {
		this.provisioningsIdsToReassign = provisioningsIdsToReassign;
	}

}

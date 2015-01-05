package com.drogueria.dto;

import java.io.Serializable;
import java.util.List;

public class AgreementTransferDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer originAgreementId;
	private Integer destinationAgreementId;

	private List<AgreementTransferDetailDTO> agreementTransferDetails;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOriginAgreementId() {
		return this.originAgreementId;
	}

	public void setOriginAgreementId(Integer originAgreementId) {
		this.originAgreementId = originAgreementId;
	}

	public Integer getDestinationAgreementId() {
		return this.destinationAgreementId;
	}

	public void setDestinationAgreementId(Integer destinationAgreementId) {
		this.destinationAgreementId = destinationAgreementId;
	}

	public List<AgreementTransferDetailDTO> getAgreementTransferDetails() {
		return this.agreementTransferDetails;
	}

	public void setAgreementTransferDetails(List<AgreementTransferDetailDTO> agreementTransferDetails) {
		this.agreementTransferDetails = agreementTransferDetails;
	}

}

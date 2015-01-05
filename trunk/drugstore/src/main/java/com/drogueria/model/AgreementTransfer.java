package com.drogueria.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "agreement_transfer")
public class AgreementTransfer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "origin_agreement_id", nullable = false)
	private Agreement originAgreement;

	@ManyToOne
	@JoinColumn(name = "destination_agreement_id", nullable = false)
	private Agreement destinationAgreement;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "agreement_transfer_id", referencedColumnName = "id", nullable = false)
	private List<AgreementTransferDetail> agreementTransferDetails;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Agreement getOriginAgreement() {
		return this.originAgreement;
	}

	public void setOriginAgreement(Agreement originAgreement) {
		this.originAgreement = originAgreement;
	}

	public Agreement getDestinationAgreement() {
		return this.destinationAgreement;
	}

	public void setDestinationAgreement(Agreement destinationAgreement) {
		this.destinationAgreement = destinationAgreement;
	}

	public List<AgreementTransferDetail> getAgreementTransferDetail() {
		return this.agreementTransferDetails;
	}

	public void setAgreementTransferDetail(List<AgreementTransferDetail> agreementTransferDetail) {
		this.agreementTransferDetails = agreementTransferDetail;
	}

}

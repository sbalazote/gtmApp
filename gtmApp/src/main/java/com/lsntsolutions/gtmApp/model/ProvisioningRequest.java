package com.lsntsolutions.gtmApp.model;

import com.lsntsolutions.gtmApp.constant.State;
import com.lsntsolutions.gtmApp.util.StringUtility;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "provisioning_request")
public class ProvisioningRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "agreement_id", nullable = false)
	private Agreement agreement;

	@ManyToOne
	@JoinColumn(name = "client_id", nullable = false)
	private Client client;

	@ManyToOne
	@JoinColumn(name = "delivery_location_id", nullable = false)
	private DeliveryLocation deliveryLocation;

	@ManyToOne
	@JoinColumn(name = "logistics_operator_id")
	private LogisticsOperator logisticsOperator;

	@ManyToOne
	@JoinColumn(name = "affiliate_id", nullable = false)
	private Affiliate affiliate;

	@Column(name = "delivery_date", nullable = false)
	private Date deliveryDate;

	@Column(name = "comment")
	private String comment;

	@ManyToOne
	@JoinColumn(name = "state_id", nullable = false)
	private ProvisioningRequestState state;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "provisioning_request_id", referencedColumnName = "id", nullable = false)
	private List<ProvisioningRequestDetail> provisioningRequestDetails;

	public Integer getId() {
		return this.id;
	}

	public String getFormatId(){
		return StringUtility.addLeadingZeros(this.getId(), 8);
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Agreement getAgreement() {
		return this.agreement;
	}

	public void setAgreement(Agreement agreement) {
		this.agreement = agreement;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public DeliveryLocation getDeliveryLocation() {
		return this.deliveryLocation;
	}

	public void setDeliveryLocation(DeliveryLocation deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}

	public LogisticsOperator getLogisticsOperator() {
		return this.logisticsOperator;
	}

	public void setLogisticsOperator(LogisticsOperator logisticsOperator) {
		this.logisticsOperator = logisticsOperator;
	}

	public Affiliate getAffiliate() {
		return this.affiliate;
	}

	public void setAffiliate(Affiliate affiliate) {
		this.affiliate = affiliate;
	}

	public Date getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ProvisioningRequestState getState() {
		return this.state;
	}

	public void setState(ProvisioningRequestState state) {
		this.state = state;
	}

	public List<ProvisioningRequestDetail> getProvisioningRequestDetails() {
		return this.provisioningRequestDetails;
	}

	public void setProvisioningRequestDetails(List<ProvisioningRequestDetail> provisioningRequestDetails) {
		this.provisioningRequestDetails = provisioningRequestDetails;
	}

	public Boolean canModify(){
		return (this.state.getId().equals(State.ENTERED.getId()) || this.state.getId().equals(State.AUTHORIZED.getId()) || this.state.getId().equals(State.PRINTED.getId()));
	}

}

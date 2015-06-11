package com.drogueria.model;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "input")
public class Input implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "concept_id", nullable = false)
	private Concept concept;

	@ManyToOne
	@JoinColumn(name = "provider_id")
	private Provider provider;

	@ManyToOne
	@JoinColumn(name = "delivery_location_id")
	private DeliveryLocation deliveryLocation;

	@ManyToOne
	@JoinColumn(name = "agreement_id", nullable = false)
	private Agreement agreement;

	@Column(name = "delivery_note_number")
	private String deliveryNoteNumber;

	@Column(name = "purchase_order_number")
	private String purchaseOrderNumber;

	@Column(name = "date", nullable = false)
	private Date date;

	@Column(name = "transaction_code_anmat")
	private String transactionCodeANMAT;

	@Column(name = "cancelled", nullable = false)
	private boolean cancelled;

	@Column(name = "inform_anmat", nullable = false)
	private boolean informAnmat;

	@Column(name = "informed", nullable = false)
	private boolean informed;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "input_id", referencedColumnName = "id", nullable = false)
	private List<InputDetail> inputDetails;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Concept getConcept() {
		return this.concept;
	}

	public void setConcept(Concept concept) {
		this.concept = concept;
	}

	public Provider getProvider() {
		return this.provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public Agreement getAgreement() {
		return this.agreement;
	}

	public void setAgreement(Agreement agreement) {
		this.agreement = agreement;
	}

	public String getDeliveryNoteNumber() {
		return this.deliveryNoteNumber;
	}

	public void setDeliveryNoteNumber(String deliveryNoteNumber) {
		this.deliveryNoteNumber = deliveryNoteNumber;
	}

	public String getPurchaseOrderNumber() {
		return this.purchaseOrderNumber;
	}

	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<InputDetail> getInputDetails() {
		return this.inputDetails;
	}

	public void setInputDetails(List<InputDetail> inputDetails) {
		this.inputDetails = inputDetails;
	}

	public DeliveryLocation getDeliveryLocation() {
		return this.deliveryLocation;
	}

	public void setDeliveryLocation(DeliveryLocation deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}

	public String getTransactionCodeANMAT() {
		return this.transactionCodeANMAT;
	}

	public void setTransactionCodeANMAT(String transactionCodeANMAT) {
		this.transactionCodeANMAT = transactionCodeANMAT;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public boolean isInformAnmat() {
		return this.informAnmat;
	}

	public void setInformAnmat(boolean informAnmat) {
		this.informAnmat = informAnmat;
	}

	public boolean isInformed() {
		return this.informed;
	}

	public void setInformed(boolean informed) {
		this.informed = informed;
	}

	public boolean hasToInform() throws Exception {
		boolean hasToInform = false;
		if (this.getConcept().isInformAnmat()) {
			for (InputDetail inputDetail : this.getInputDetails()) {
				if (inputDetail.getProduct().isInformAnmat()
						&& ("PS".equals(inputDetail.getProduct().getType()) || "SS".equals(inputDetail.getProduct().getType()))) {
					hasToInform = true;
				}
			}
		} else {
			hasToInform = false;
		}
		return hasToInform;
	}

	public boolean hasNotProviderSerialized() throws Exception {
		boolean hasToInform = true;
		for (InputDetail inputDetail : this.getInputDetails()) {
			if (inputDetail.getProduct().isInformAnmat() && ("PS".equals(inputDetail.getProduct().getType()))) {
				hasToInform = false;
			}
		}
		return hasToInform;
	}

	public boolean hasSelfSerialized() throws Exception {
		boolean hasToInform = false;
		for (InputDetail inputDetail : this.getInputDetails()) {
			if (inputDetail.getProduct().isInformAnmat() && ("SS".equals(inputDetail.getProduct().getType()))) {
				hasToInform = true;
			}
		}
		return hasToInform;
	}

	public String getEvent() {
		String eventId = null;
		if (this.getProvider() != null) {
			eventId = this.getConcept().getEventOnInput(this.getProvider().getAgent().getId());
		}
		if (this.getDeliveryLocation() != null) {
			eventId = this.getConcept().getEventOnInput(this.getDeliveryLocation().getAgent().getId());
		}
		return eventId;
	}

	public String getOriginTax() {
		String originTax = null;
		if (this.getProvider() != null) {
			originTax = this.getProvider().getTaxId();
		}
		if (this.getDeliveryLocation() != null) {
			originTax = this.getDeliveryLocation().getTaxId();
		}
		return originTax;
	}

	public String getOriginGln() {
		String originGln = null;
		if (this.getProvider() != null) {
			originGln = this.getProvider().getGln();
		}
		if (this.getDeliveryLocation() != null) {
			originGln = this.getDeliveryLocation().getGln();
		}
		return originGln;
	}

	public String getClientOrProviderDescription() {
		String description = null;
		if (this.getProvider() != null) {
			description = this.getProvider().getCorporateName();
		}
		if (this.getDeliveryLocation() != null) {
			description = this.getDeliveryLocation().getCorporateName();
		}
		return description;
	}

	public String getClientOrProviderAgentDescription() {
		String description = null;
		if (this.getProvider() != null) {
			description = this.getProvider().getAgent().getDescription();
		}
		if (this.getDeliveryLocation() != null) {
			description = this.getDeliveryLocation().getAgent().getDescription();
		}
		return description;
	}

	public Integer getClientOrProviderAgent() {
		Integer agentId = null;
		if (this.getProvider() != null) {
			agentId = this.getProvider().getAgent().getId();
		}
		if (this.getDeliveryLocation() != null) {
			agentId = this.getDeliveryLocation().getAgent().getId();
		}
		return agentId;
	}

	public boolean hasBeenInformedAllProducts() {
		for (InputDetail inputDetail : this.getInputDetails()) {
			if (inputDetail.getProduct().isInformAnmat() && !inputDetail.isInformed()) {
				return false;
			}
		}
		return true;
	}
}

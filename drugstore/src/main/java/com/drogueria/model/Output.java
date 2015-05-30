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
@Table(name = "output")
public class Output implements Serializable {

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

	@Column(name = "date", nullable = false)
	private Date date;

	@Column(name = "cancelled", nullable = false)
	private boolean cancelled;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "output_id", referencedColumnName = "id", nullable = false)
	private List<OutputDetail> outputDetails;

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

	public DeliveryLocation getDeliveryLocation() {
		return this.deliveryLocation;
	}

	public void setDeliveryLocation(DeliveryLocation deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}

	public Agreement getAgreement() {
		return this.agreement;
	}

	public void setAgreement(Agreement agreement) {
		this.agreement = agreement;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public List<OutputDetail> getOutputDetails() {
		return this.outputDetails;
	}

	public void setOutputDetails(List<OutputDetail> outputDetails) {
		this.outputDetails = outputDetails;
	}

	public boolean hasToInform() throws Exception {
		boolean hasToInform = false;
		if (this.getConcept().isInformAnmat() && !this.getConcept().isPrintDeliveryNote()) {
			for (OutputDetail outputDetail : this.getOutputDetails()) {
				if (outputDetail.getProduct().isInformAnmat()
						&& ("PS".equals(outputDetail.getProduct().getType()) || "SS".equals(outputDetail.getProduct().getType()))) {
					hasToInform = true;
				}
			}
		} else {
			hasToInform = false;
		}
		return hasToInform;
	}

	public boolean hasProductThatInform() throws Exception {
		boolean hasToInform = false;
		for (OutputDetail outputDetail : this.getOutputDetails()) {
			if (outputDetail.getProduct().isInformAnmat()
					&& ("PS".equals(outputDetail.getProduct().getType()) || "SS".equals(outputDetail.getProduct().getType()))) {
				hasToInform = true;
			}
		}
		return hasToInform;
	}

	public String getDestinationTax() {
		String originTax = null;
		if (this.getProvider() != null) {
			originTax = this.getProvider().getTaxId();
		}
		if (this.getDeliveryLocation() != null) {
			originTax = this.getDeliveryLocation().getTaxId();
		}
		return originTax;
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

	public String getDestinationGln() {
		String originGln = null;
		if (this.getProvider() != null) {
			originGln = this.getProvider().getGln();
		}
		if (this.getDeliveryLocation() != null) {
			originGln = this.getDeliveryLocation().getGln();
		}
		return originGln;
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

}

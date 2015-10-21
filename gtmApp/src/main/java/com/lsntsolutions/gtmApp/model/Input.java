package com.lsntsolutions.gtmApp.model;

import com.lsntsolutions.gtmApp.util.StringUtility;

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
	@JoinColumn(name = "logistics_operator_id")
	private LogisticsOperator logisticsOperator;

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

	@Column(name = "forced_input", nullable = false)
	private boolean forcedInput;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "input_id", referencedColumnName = "id", nullable = false)
	private List<InputDetail> inputDetails;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

    public String getFormatId(){
        return StringUtility.addLeadingZeros(this.getId(), 8);
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

	public void setInformed(boolean informed) {
		this.informed = informed;
	}

	public boolean isForcedInput() {
		return this.forcedInput;
	}

	public void setForcedInput(boolean forcedInput) {
		this.forcedInput = forcedInput;
	}

	public LogisticsOperator getLogisticsOperator() {
		return logisticsOperator;
	}

	public void setLogisticsOperator(LogisticsOperator logisticsOperator) {
		this.logisticsOperator = logisticsOperator;
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
			if(this.getLogisticsOperator() != null){
				originTax = this.getLogisticsOperator().getTaxId();
			}else {
				originTax = this.getProvider().getTaxId();
			}
		}
		if (this.getDeliveryLocation() != null) {
			originTax = this.getDeliveryLocation().getTaxId();
		}
		return originTax;
	}

	public String getOriginGln() {
		String originGln = null;
		if (this.getProvider() != null) {
			if(this.getLogisticsOperator() != null){
				originGln = this.getLogisticsOperator().getGln();
			}else {
				originGln = this.getProvider().getGln();
			}
		}
		if (this.getDeliveryLocation() != null) {
			originGln = this.getDeliveryLocation().getGln();
		}
		return originGln;
	}

	public String getClientOrProviderDescription() {
		String description = null;
		if (this.getProvider() != null) {
			description = this.getProvider().getName();
		}
		if (this.getDeliveryLocation() != null) {
			description = this.getDeliveryLocation().getName();
		}
		return description;
	}

	public String getClientOrProviderCode() {
		String description = null;
		if (this.getProvider() != null) {
			description = String.valueOf(this.getProvider().getCode());
		}
		if (this.getDeliveryLocation() != null) {
			description =  String.valueOf(this.getDeliveryLocation().getCode());
		}
        return StringUtility.addLeadingZeros(description, 4);
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

}

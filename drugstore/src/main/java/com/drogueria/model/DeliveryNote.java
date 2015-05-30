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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "delivery_note")
public class DeliveryNote implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "number", nullable = false)
	private String number;

	@Column(name = "date", nullable = false)
	private Date date;

	@Column(name = "transaction_code_anmat")
	private String transactionCodeANMAT;

	@Column(name = "cancelled", nullable = false)
	private boolean cancelled;

	@Column(name = "inform_anmat", nullable = false)
	private boolean informAnmat;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "delivery_note_id", referencedColumnName = "id", nullable = false)
	private List<DeliveryNoteDetail> deliveryNoteDetails;

	@Column(name = "informed", nullable = false)
	private boolean informed;

	@Column(name = "fake", nullable = false)
	private boolean fake;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
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

	public List<DeliveryNoteDetail> getDeliveryNoteDetails() {
		return this.deliveryNoteDetails;
	}

	public void setDeliveryNoteDetails(List<DeliveryNoteDetail> deliveryNoteDetails) {
		this.deliveryNoteDetails = deliveryNoteDetails;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isInformed() {
		return this.informed;
	}

	public void setInformed(boolean informed) {
		this.informed = informed;
	}

	public boolean isFake() {
		return this.fake;
	}

	public void setFake(boolean fake) {
		this.fake = fake;
	}

}

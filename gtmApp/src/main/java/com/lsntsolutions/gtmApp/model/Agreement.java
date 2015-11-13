package com.lsntsolutions.gtmApp.model;

import com.lsntsolutions.gtmApp.util.StringUtility;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "agreement")
public class Agreement implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "code", unique = true, nullable = false)
	private Integer code;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "number_of_delivery_note_details_per_page", nullable = false)
	private Integer numberOfDeliveryNoteDetailsPerPage;

	@Column(name = "order_label_printer", nullable = false)
	private String orderLabelPrinter;

	@Column(name = "delivery_note_printer", nullable = false)
	private String deliveryNotePrinter;

	@ManyToOne
	@JoinColumn(name = "delivery_note_concept_id", nullable = false)
	private Concept deliveryNoteConcept;

	@ManyToOne
	@JoinColumn(name = "destruction_concept_id", nullable = false)
	private Concept destructionConcept;

	@Column(name = "active", nullable = false)
	private boolean active;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getFormatCode() {
		return StringUtility.addLeadingZeros(this.getCode().toString(), 5);
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNumberOfDeliveryNoteDetailsPerPage() {
		return this.numberOfDeliveryNoteDetailsPerPage;
	}

	public void setNumberOfDeliveryNoteDetailsPerPage(Integer numberOfDeliveryNoteDetailsPerPage) {
		this.numberOfDeliveryNoteDetailsPerPage = numberOfDeliveryNoteDetailsPerPage;
	}

	public String getOrderLabelPrinter() {
		return this.orderLabelPrinter;
	}

	public void setOrderLabelPrinter(String orderLabelPrinter) {
		this.orderLabelPrinter = orderLabelPrinter;
	}

	public String getDeliveryNotePrinter() {
		return this.deliveryNotePrinter;
	}

	public void setDeliveryNotePrinter(String deliveryNotePrinter) {
		this.deliveryNotePrinter = deliveryNotePrinter;
	}

	public Concept getDeliveryNoteConcept() {
		return this.deliveryNoteConcept;
	}

	public void setDeliveryNoteConcept(Concept deliveryNoteConcept) {
		this.deliveryNoteConcept = deliveryNoteConcept;
	}

	public Concept getDestructionConcept() {
		return this.destructionConcept;
	}

	public void setDestructionConcept(Concept destructionConcept) {
		this.destructionConcept = destructionConcept;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}

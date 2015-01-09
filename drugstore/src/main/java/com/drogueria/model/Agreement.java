package com.drogueria.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

	@Column(name = "order_label_filepath", nullable = false)
	private String orderLabelFilepath;

	@Column(name = "delivery_note_filepath", nullable = false)
	private String deliveryNoteFilepath;

	@Column(name = "picking_filepath", nullable = false)
	private String pickingFilepath;

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

	public String getOrderLabelFilepath() {
		return this.orderLabelFilepath;
	}

	public void setOrderLabelFilepath(String orderLabelFilepath) {
		this.orderLabelFilepath = orderLabelFilepath;
	}

	public String getDeliveryNoteFilepath() {
		return this.deliveryNoteFilepath;
	}

	public void setDeliveryNoteFilepath(String deliveryNoteFilepath) {
		this.deliveryNoteFilepath = deliveryNoteFilepath;
	}

	public String getPickingFilepath() {
		return this.pickingFilepath;
	}

	public void setPickingFilepath(String pickingFilepath) {
		this.pickingFilepath = pickingFilepath;
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

package com.lsntsolutions.gtmApp.model;

import com.lsntsolutions.gtmApp.util.StringUtility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "concept")
public class Concept implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "code", unique = true, nullable = false)
	private Integer code;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "input", nullable = false)
	private boolean input;

	@Column(name = "print_delivery_note", nullable = false)
	private boolean printDeliveryNote;

	@Column(name = "delivery_note_copies", nullable = false)
	private Integer deliveryNoteCopies;

	@Column(name = "refund", nullable = false)
	private boolean refund;

	@Column(name = "inform_anmat", nullable = false)
	private boolean informAnmat;

	@Column(name = "active", nullable = false)
	private boolean active;

	@Column(name = "client", nullable = false)
	private boolean client;

	@Column(name = "destruction", nullable = false)
	private boolean destruction;

	@ManyToOne
	@JoinColumn(name = "delivery_note_enumerator_id")
	private DeliveryNoteEnumerator deliveryNoteEnumerator;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "concept_event", joinColumns = @JoinColumn(name = "concept_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "event_id", nullable = false))
	private List<Event> events;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getFormatCode(){
		return StringUtility.addLeadingZeros(this.getDeliveryNoteCopies(), 4);
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

	public boolean isInput() {
		return this.input;
	}

	public void setInput(boolean input) {
		this.input = input;
	}

	public boolean isPrintDeliveryNote() {
		return this.printDeliveryNote;
	}

	public void setPrintDeliveryNote(boolean printDeliveryNote) {
		this.printDeliveryNote = printDeliveryNote;
	}

	public Integer getDeliveryNoteCopies() {
		return this.deliveryNoteCopies;
	}

	public void setDeliveryNoteCopies(Integer deliveryNoteCopies) {
		this.deliveryNoteCopies = deliveryNoteCopies;
	}

	public boolean isRefund() {
		return this.refund;
	}

	public void setRefund(boolean refund) {
		this.refund = refund;
	}

	public boolean isInformAnmat() {
		return this.informAnmat;
	}

	public void setInformAnmat(boolean informAnmat) {
		this.informAnmat = informAnmat;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isClient() {
		return this.client;
	}

	public void setClient(boolean client) {
		this.client = client;
	}

	public List<Event> getEvents() {
		return ( events.isEmpty() ? new ArrayList<Event>() : events );
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public DeliveryNoteEnumerator getDeliveryNoteEnumerator() {
		return this.deliveryNoteEnumerator;
	}

	public void setDeliveryNoteEnumerator(DeliveryNoteEnumerator deliveryNoteEnumerator) {
		this.deliveryNoteEnumerator = deliveryNoteEnumerator;
	}

	public boolean isDestruction() {
		return destruction;
	}

	public void setDestruction(boolean destruction) {
		this.destruction = destruction;
	}

	public String getEventOnInput(Integer agentId) {
		String eventId = null;
		try {
			for (Event event : this.getEvents()) {
				if (event.getOriginAgent().getId().equals(agentId)) {
					eventId = event.getCode().toString();
				}
			}
		} catch (Exception e) {
			return null;
		}
		return eventId;
	}

	public String getEventOnOutput(Integer agentId) {
		String eventId = null;
		try {
			if(isDestruction()){
				if(getEvents() != null && !getEvents().isEmpty()){
					return getEvents().get(0).toString();
				}else{
					return null;
				}
			}else {
				for (Event event : this.getEvents()) {
					if (event.getDestinationAgent().getId().equals(agentId)) {
						eventId = event.getCode().toString();
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		return eventId;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Concept)) {
			return false;
		}
		Concept concept = (Concept) obj;
		return this.id != null && concept.id != null && this.id.equals(concept.getId());
	}

	@Override
	public int hashCode() {
		assert false : "hashCode not designed";
		return 1;
	}

	@Override
	public String toString() {
		return this.getCode() + " - " + this.getDescription();

	}
}

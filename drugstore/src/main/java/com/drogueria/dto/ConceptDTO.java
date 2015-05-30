package com.drogueria.dto;

import java.io.Serializable;
import java.util.List;

public class ConceptDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer code;
	private String description;
	private String deliveryNotePOS;
	private boolean input;
	private boolean printDeliveryNote;
	private boolean refund;
	private boolean informAnmat;
	private Integer deliveryNotesCopies;
	private List<Integer> events;
	private boolean active;
	private boolean client;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Integer> getEvents() {
		return this.events;
	}

	public void setEvents(List<Integer> events) {
		this.events = events;
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

	public String getDeliveryNotePOS() {
		return this.deliveryNotePOS;
	}

	public void setDeliveryNotePOS(String deliveryNotePOS) {
		this.deliveryNotePOS = deliveryNotePOS;
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

	public Integer getDeliveryNotesCopies() {
		return this.deliveryNotesCopies;
	}

	public void setDeliveryNotesCopies(Integer deliveryNotesCopies) {
		this.deliveryNotesCopies = deliveryNotesCopies;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

}

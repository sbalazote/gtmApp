package com.drogueria.dto;

import java.io.Serializable;

public class EventDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer code;
	private String description;
	private Integer originAgentId;
	private Integer destinationAgentId;
	private boolean selected;
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

	public Integer getOriginAgentId() {
		return this.originAgentId;
	}

	public void setOriginAgentId(Integer originAgentId) {
		this.originAgentId = originAgentId;
	}

	public Integer getDestinationAgentId() {
		return this.destinationAgentId;
	}

	public void setDestinationAgentId(Integer destinationAgentId) {
		this.destinationAgentId = destinationAgentId;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}

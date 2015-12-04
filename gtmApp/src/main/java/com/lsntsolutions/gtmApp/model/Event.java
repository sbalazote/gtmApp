package com.lsntsolutions.gtmApp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "event")
public class Event implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "code", unique = true, nullable = false)
	private Integer code;

	@Column(name = "description", nullable = false)
	private String description;

	@ManyToOne
	@JoinColumn(name = "origin_agent_id", nullable = false)
	private Agent originAgent;

	@ManyToOne
	@JoinColumn(name = "destination_agent_id", nullable = false)
	private Agent destinationAgent;

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

	public Agent getOriginAgent() {
		return this.originAgent;
	}

	public void setOriginAgent(Agent originAgent) {
		this.originAgent = originAgent;
	}

	public Agent getDestinationAgent() {
		return this.destinationAgent;
	}

	public void setDestinationAgent(Agent destinationAgent) {
		this.destinationAgent = destinationAgent;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString(){
		return this.code + " - " + this.description;
	}

}

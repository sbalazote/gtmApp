package com.drogueria.dto;

import java.io.Serializable;

public class ProviderSerializedProductDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String gtin;
	private String serialNumber;
	private String expirationDate;
	private String batch;
	private String sequence;

	public void setValue(String fieldType, String value) {
		if ("G".equalsIgnoreCase(fieldType)) {
			this.setGtin(value);
		} else if ("S".equalsIgnoreCase(fieldType)) {
			this.setSerialNumber(value);
		} else if ("E".equalsIgnoreCase(fieldType)) {
			this.setExpirationDate(value);
		} else if ("B".equalsIgnoreCase(fieldType)) {
			this.setBatch(value);
		}
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGtin() {
		return this.gtin;
	}

	public void setGtin(String gtin) {
		this.gtin = gtin;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getBatch() {
		return this.batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getSequence() {
		return this.sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

}

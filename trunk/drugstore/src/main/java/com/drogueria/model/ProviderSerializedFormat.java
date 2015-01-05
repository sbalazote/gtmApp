package com.drogueria.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "provider_serialized_format")
public class ProviderSerializedFormat implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "gtin_length", unique = true, nullable = false)
	private Integer gtinLength;

	@Column(name = "serial_number_length")
	private Integer serialNumberLength;

	@Column(name = "expiration_date_length")
	private Integer expirationDateLength;

	@Column(name = "batch_length")
	private Integer batchLength;

	@Column(name = "sequence", nullable = false)
	private String sequence;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGtinLength() {
		return this.gtinLength;
	}

	public void setGtinLength(Integer gtinLength) {
		this.gtinLength = gtinLength;
	}

	public Integer getSerialNumberLength() {
		return this.serialNumberLength;
	}

	public void setSerialNumberLength(Integer serialNumberLength) {
		this.serialNumberLength = serialNumberLength;
	}

	public Integer getExpirationDateLength() {
		return this.expirationDateLength;
	}

	public void setExpirationDateLength(Integer expirationDateLength) {
		this.expirationDateLength = expirationDateLength;
	}

	public Integer getBatchLength() {
		return this.batchLength;
	}

	public void setBatchLength(Integer batchLength) {
		this.batchLength = batchLength;
	}

	public String getSequence() {
		return this.sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String[] getSplittedSequence() {
		return this.sequence.split("-");
	}

	public Integer getTotalLength() {
		Integer totalLength = 0;
		totalLength += this.gtinLength != null ? this.gtinLength : 0;
		totalLength += this.serialNumberLength != null ? this.serialNumberLength : 0;
		totalLength += this.expirationDateLength != null ? this.expirationDateLength : 0;
		totalLength += this.batchLength != null ? this.batchLength : 0;
		return totalLength;
	}

	public Integer getLength(String fieldType) {
		if ("G".equalsIgnoreCase(fieldType)) {
			return this.gtinLength;
		} else if ("S".equalsIgnoreCase(fieldType)) {
			return this.serialNumberLength;
		} else if ("E".equalsIgnoreCase(fieldType)) {
			return this.expirationDateLength;
		} else if ("B".equalsIgnoreCase(fieldType)) {
			return this.batchLength;
		}
		return null;
	}
}

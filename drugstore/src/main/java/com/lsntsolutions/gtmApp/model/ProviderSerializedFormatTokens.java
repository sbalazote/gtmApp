package com.lsntsolutions.gtmApp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "serial_separation_mapping")
public class ProviderSerializedFormatTokens implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "separator_token", unique = true, nullable = false)
	private String separatorToken;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSeparatorToken() {
		return this.separatorToken;
	}

	public void setSeparatorToken(String separatorToken) {
		this.separatorToken = separatorToken;
	}

}

package com.lsntsolutions.gtmApp.dto;

import java.io.Serializable;
import java.util.Date;

public class ProductGtinDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String number;
	private Date date;

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

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}

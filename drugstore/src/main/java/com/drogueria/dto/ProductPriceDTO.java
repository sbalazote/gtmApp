package com.drogueria.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProductPriceDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private BigDecimal price;
	private Date date;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
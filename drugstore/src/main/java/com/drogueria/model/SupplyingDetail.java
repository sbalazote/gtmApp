package com.drogueria.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "supplying_detail")
public class SupplyingDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(name = "serial_number")
	private String serialNumber;

	@ManyToOne
	@JoinColumn(name = "gtin_id")
	private ProductGtin gtin;

	@Column(name = "batch", nullable = false)
	private String batch;

	@Column(name = "expiration_date", nullable = false)
	private Date expirationDate;

	@Column(name = "amount", nullable = false)
	private Integer amount;

	@Column(name = "in_stock", nullable = false)
	private boolean inStock;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public ProductGtin getGtin() {
		return this.gtin;
	}

	public void setGtin(ProductGtin gtin) {
		this.gtin = gtin;
	}

	public String getBatch() {
		return this.batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public Date getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Integer getAmount() {
		return this.amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public boolean getInStock() {
		return this.inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}
}

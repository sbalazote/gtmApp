package com.drogueria.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.drogueria.model.ProductGtin;

public class ProductDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer code;
	private String description;
	private String gtin;
	private Integer brandId;
	private Integer monodrugId;
	private Integer groupId;
	private Integer drugCategoryId;
	private BigDecimal price;
	private boolean cold;
	private boolean informAnmat;
	private String type;
	private boolean active;

	private List<ProductGtin> gtins;

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

	public String getGtin() {
		return this.gtin;
	}

	public void setGtin(String gtin) {
		this.gtin = gtin;
	}

	public Integer getBrandId() {
		return this.brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public Integer getMonodrugId() {
		return this.monodrugId;
	}

	public void setMonodrugId(Integer monodrugId) {
		this.monodrugId = monodrugId;
	}

	public Integer getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getDrugCategoryId() {
		return this.drugCategoryId;
	}

	public void setDrugCategoryId(Integer drugCategoryId) {
		this.drugCategoryId = drugCategoryId;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public boolean isCold() {
		return this.cold;
	}

	public void setCold(boolean cold) {
		this.cold = cold;
	}

	public boolean isInformAnmat() {
		return this.informAnmat;
	}

	public void setInformAnmat(boolean informAnmat) {
		this.informAnmat = informAnmat;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<ProductGtin> getGtins() {
		return this.gtins;
	}

	public void setGtins(List<ProductGtin> gtins) {
		this.gtins = gtins;
	}
}

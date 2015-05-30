package com.drogueria.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product implements Serializable {

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
	@JoinColumn(name = "brand_id", nullable = false)
	private ProductBrand brand;

	@ManyToOne
	@JoinColumn(name = "monodrug_id", nullable = false)
	private ProductMonodrug monodrug;

	@ManyToOne
	@JoinColumn(name = "group_id", nullable = false)
	private ProductGroup group;

	@ManyToOne
	@JoinColumn(name = "drug_category_id", nullable = false)
	private ProductDrugCategory drugCategory;

	@Column(name = "cold", nullable = false)
	private boolean cold;

	@Column(name = "inform_anmat", nullable = false)
	private boolean informAnmat;

	@Column(name = "type", nullable = false)
	private String type;

	@Column(name = "active", nullable = false)
	private boolean active;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
	private List<ProductGtin> gtins;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
	private List<ProductPrice> prices;

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

	public ProductBrand getBrand() {
		return this.brand;
	}

	public void setBrand(ProductBrand brand) {
		this.brand = brand;
	}

	public ProductMonodrug getMonodrug() {
		return this.monodrug;
	}

	public void setMonodrug(ProductMonodrug monodrug) {
		this.monodrug = monodrug;
	}

	public ProductGroup getGroup() {
		return this.group;
	}

	public void setGroup(ProductGroup group) {
		this.group = group;
	}

	public ProductDrugCategory getDrugCategory() {
		return this.drugCategory;
	}

	public void setDrugCategory(ProductDrugCategory drugCategory) {
		this.drugCategory = drugCategory;
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

	public List<ProductGtin> getGtins() {
		return this.gtins;
	}

	public void setGtins(List<ProductGtin> gtins) {
		this.gtins = gtins;
	}

	public List<ProductPrice> getPrices() {
		return this.prices;
	}

	public void setPrices(List<ProductPrice> prices) {
		this.prices = prices;
	}

	public boolean existsGtin(String gtin) {
		boolean found = false;
		if (this.getGtins() != null) {
			for (ProductGtin productGtin : this.getGtins()) {
				if (gtin.equals(productGtin.getNumber())) {
					found = true;
				}
			}
		}
		return found;
	}

	public boolean existsPrice(BigDecimal price) {
		boolean found = false;
		if (this.getPrices() != null) {
			for (ProductPrice productPrice : this.getPrices()) {
				if (price.equals(productPrice.getPrice())) {
					found = true;
				}
			}
		}
		return found;
	}

	public String getLastGtin() {
		String gtin = null;
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		try {
			date = df.parse("01-01-1900");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for (ProductGtin productGtin : this.gtins) {
			if (productGtin.getDate().after(date)) {
				gtin = productGtin.getNumber();
				date = productGtin.getDate();
			}
		}
		return gtin;
	}

	public ProductGtin getLastProductGtin() {
		ProductGtin gtin = null;
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		try {
			date = df.parse("01-01-1900");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for (ProductGtin productGtin : this.gtins) {
			if (productGtin.getDate().after(date)) {
				gtin = productGtin;
				date = productGtin.getDate();
			}
		}
		return gtin;
	}

	public BigDecimal getLastPrice() {
		BigDecimal price = null;
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		try {
			date = df.parse("01-01-1900");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for (ProductPrice productPrice : this.prices) {
			if (productPrice.getDate().after(date)) {
				price = productPrice.getPrice();
				date = productPrice.getDate();
			}
		}
		return price;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Product)) {
			return false;
		}
		Product product = (Product) obj;
		return this.id != null && product.id != null && this.id.equals(product.getId());
	}

	@Override
	public int hashCode() {
		return this.description.hashCode() * this.getCode().hashCode();
	}

}
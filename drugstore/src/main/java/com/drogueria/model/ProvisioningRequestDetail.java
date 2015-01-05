package com.drogueria.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "provisioning_request_detail")
public class ProvisioningRequestDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(name = "amount", nullable = false)
	private Integer amount;

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

	public Integer getAmount() {
		return this.amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ProvisioningRequestDetail)) {
			return false;
		}
		ProvisioningRequestDetail detail = (ProvisioningRequestDetail) obj;
		return this.product != null && this.product.getId() != null && detail.product != null && detail.product.getId() != null
				&& this.product.getId().equals(detail.product.getId());
	}

	@Override
	public int hashCode() {
		assert false : "hashCode not designed";
		return 1;
	}

}

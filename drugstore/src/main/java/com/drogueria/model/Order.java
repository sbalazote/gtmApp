package com.drogueria.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Table(name = "`order`")
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "provisioning_request_id", nullable = false)
	private ProvisioningRequest provisioningRequest;

	@Column(name = "cancelled", nullable = false)
	private boolean cancelled;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
	private List<OrderDetail> orderDetails;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ProvisioningRequest getProvisioningRequest() {
		return this.provisioningRequest;
	}

	public void setProvisioningRequest(ProvisioningRequest provisioningRequest) {
		this.provisioningRequest = provisioningRequest;
	}

	public List<OrderDetail> getOrderDetails() {
		return this.orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public boolean hasToInform() throws Exception {
		boolean hasToInform = false;
		for (OrderDetail orderDetail : this.getOrderDetails()) {
			if (orderDetail.getProduct().isInformAnmat()
					&& ("PS".equals(orderDetail.getProduct().getType()) || "SS".equals(orderDetail.getProduct().getType()))) {
				hasToInform = true;
			}
		}
		return hasToInform;
	}

	public Map<Product, Integer> getProducts(boolean cold) {
		Map<Product, Integer> products = new HashMap<Product, Integer>();
		boolean found = false;
		for (OrderDetail orderDetail : this.getOrderDetails()) {
			if (cold == orderDetail.getProduct().isCold()) {
				found = false;
				for (Product product : products.keySet()) {
					if (product.equals(orderDetail.getProduct())) {
						Integer amount = products.get(product) + orderDetail.getAmount();
						products.put(product, amount);
						found = true;
					}
				}
				if (!found) {
					products.put(orderDetail.getProduct(), orderDetail.getAmount());
				}
			}
		}
		return products;
	}
}

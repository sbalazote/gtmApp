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
@Table(name = "delivery_note_detail")
public class DeliveryNoteDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "order_detail_id")
	private OrderDetail orderDetail;

	@ManyToOne
	@JoinColumn(name = "output_detail_id")
	private OutputDetail outputDetail;

	@ManyToOne
	@JoinColumn(name = "supplying_detail_id")
	private SupplyingDetail supplyingDetail;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OrderDetail getOrderDetail() {
		return this.orderDetail;
	}

	public void setOrderDetail(OrderDetail orderDetail) {
		this.orderDetail = orderDetail;
	}

	public OutputDetail getOutputDetail() {
		return this.outputDetail;
	}

	public void setOutputDetail(OutputDetail outputDetail) {
		this.outputDetail = outputDetail;
	}

	public SupplyingDetail getSupplyingDetail() {
		return this.supplyingDetail;
	}

	public void setSupplyingDetail(SupplyingDetail supplyingDetail) {
		this.supplyingDetail = supplyingDetail;
	}

}

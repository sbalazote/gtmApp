package com.drogueria.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.constant.State;
import com.drogueria.dto.OrderDTO;
import com.drogueria.dto.OrderDetailDTO;
import com.drogueria.model.Agreement;
import com.drogueria.model.Order;
import com.drogueria.model.OrderDetail;
import com.drogueria.model.Product;
import com.drogueria.model.ProductGtin;
import com.drogueria.model.ProvisioningRequest;
import com.drogueria.model.Stock;
import com.drogueria.persistence.dao.OrderDAO;
import com.drogueria.query.DeliveryNoteQuery;
import com.drogueria.service.OrderService;
import com.drogueria.service.ProductGtinService;
import com.drogueria.service.ProductService;
import com.drogueria.service.ProvisioningRequestService;
import com.drogueria.service.ProvisioningRequestStateService;
import com.drogueria.service.StockService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private ProvisioningRequestService provisioningRequestService;
	@Autowired
	private ProvisioningRequestStateService provisioningRequestStateService;
	@Autowired
	private ProductService productService;
	@Autowired
	private StockService stockService;
	@Autowired
	private ProductGtinService productGtinService;

	@Override
	public Order save(OrderDTO orderDTO) {
		ProvisioningRequest provisioningRequest = this.provisioningRequestService.get(orderDTO.getProvisioningRequestId());
		Order order = this.buildOrder(orderDTO, provisioningRequest);
		this.orderDAO.save(order);

		provisioningRequest.setState(this.provisioningRequestStateService.get(State.ASSEMBLED.getId()));
		this.provisioningRequestService.save(provisioningRequest);

		logger.info("Se ha armado el pedido para la para la Solicitud de Abastecimiento n�mero: " + provisioningRequest.getId() + ". Id de pedido: "
				+ order.getId());

		return order;
	}

	@Override
	public Order get(Integer id) {
		return this.orderDAO.get(id);
	}

	private Order buildOrder(OrderDTO orderDTO, ProvisioningRequest provisioningRequest) {
		SimpleDateFormat expirationDateFormatter = new SimpleDateFormat("dd/MM/yyyy");

		try {
			Agreement agreement = provisioningRequest.getAgreement();

			Order order = new Order();
			order.setProvisioningRequest(provisioningRequest);
			order.setCancelled(orderDTO.isCancelled());

			List<OrderDetail> details = new ArrayList<>();
			Product product = null;
			for (OrderDetailDTO orderDetailDTO : orderDTO.getOrderDetails()) {
				if (product == null || !product.getId().equals(orderDetailDTO.getProductId())) {
					product = this.productService.get(orderDetailDTO.getProductId());
				}

				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setAmount(orderDetailDTO.getAmount());
				orderDetail.setBatch(orderDetailDTO.getBatch());
				if (orderDetailDTO.getExpirationDate() != null && !orderDetailDTO.getExpirationDate().isEmpty()) {
					orderDetail.setExpirationDate(expirationDateFormatter.parse(orderDetailDTO.getExpirationDate()));
				}
				orderDetail.setSerialNumber(orderDetailDTO.getSerialNumber());
				orderDetail.setProduct(product);
				if (orderDetailDTO.getGtin() != null) {
					ProductGtin productGtin = this.productGtinService.getByNumber(orderDetailDTO.getGtin());
					orderDetail.setGtin(productGtin);
				} else {
					if (product.getLastProductGtin() != null) {
						orderDetail.setGtin(product.getLastProductGtin());
					}
				}
				this.updateStock(orderDetail, agreement);
				details.add(orderDetail);
			}

			order.setOrderDetails(details);

			return order;

		} catch (Exception e) {
			throw new RuntimeException("No se ha podido mappear el OrderDTO relacionado con la Solicitud de Abastecimiento " + provisioningRequest.getId(), e);
		}
	}

	private void updateStock(OrderDetail orderDetail, Agreement agreement) {
		Stock stock = new Stock();
		stock.setAgreement(agreement);
		stock.setAmount(orderDetail.getAmount());
		stock.setBatch(orderDetail.getBatch());
		stock.setExpirationDate(orderDetail.getExpirationDate());
		stock.setProduct(orderDetail.getProduct());
		stock.setSerialNumber(orderDetail.getSerialNumber());
		if (orderDetail.getGtin() != null) {
			stock.setGtin(orderDetail.getGtin());
		}
		this.stockService.removeFromStock(stock);
	}

	@Override
	public Order getOrderByProvisioningRequestId(Integer provisioningRequestId) {
		return this.orderDAO.getOrderByProvisioningRequestId(provisioningRequestId);
	}

	@Override
	public boolean getCountDeliveryNoteSearch(DeliveryNoteQuery deliveryNoteQuery) {
		return this.orderDAO.getCountDeliveryNoteSearch(deliveryNoteQuery);
	}

	@Override
	public List<Order> getDeliveryNoteSearch(DeliveryNoteQuery deliveryNoteQuery) {
		return this.orderDAO.getDeliveryNoteSearch(deliveryNoteQuery);
	}

	@Override
	public List<Order> getAllByState(Integer stateId) {
		return this.orderDAO.getAllByState(stateId);
	}

	@Override
	public void cancelOrders(List<Integer> ordersId) {
		for (Integer id : ordersId) {
			Order order = this.get(id);
			order.setCancelled(true);
			order.getProvisioningRequest().setState(this.provisioningRequestStateService.get(State.CANCELED.getId()));
			for (OrderDetail orderDetail : order.getOrderDetails()) {
				this.addToStock(orderDetail, order.getProvisioningRequest().getAgreement());
			}
			logger.info("Se ha anulado el Armado de Pedido n�mero: " + id);
		}
	}

	private void addToStock(OrderDetail orderDetail, Agreement agreement) {
		Stock stock = new Stock();
		stock.setAgreement(agreement);
		stock.setAmount(orderDetail.getAmount());
		stock.setBatch(orderDetail.getBatch());
		stock.setExpirationDate(orderDetail.getExpirationDate());
		stock.setProduct(orderDetail.getProduct());
		stock.setSerialNumber(orderDetail.getSerialNumber());

		if (orderDetail.getGtin() != null) {
			stock.setGtin(orderDetail.getGtin());
		}

		this.stockService.addToStock(stock);
	}

	@Override
	public boolean existSerial(Integer productId, String serial) {
		return this.orderDAO.existSerial(productId, serial);
	}

	@Override
	public List<Order> getAllFilter(Integer agreementId, Integer clientId, Integer stateId) {
		return this.orderDAO.getAllFilter(agreementId, clientId, stateId);
	}

	@Override
	public void reassignOperators(List<Integer> ordersToPrint, Integer logisticOperatorId) {
		for (Integer orderId : ordersToPrint) {
			Order order = this.get(orderId);
			this.provisioningRequestService.reassignOperators(order.getProvisioningRequest(), logisticOperatorId);
		}
	}
}

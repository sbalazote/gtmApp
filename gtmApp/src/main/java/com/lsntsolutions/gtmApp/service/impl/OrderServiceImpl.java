package com.lsntsolutions.gtmApp.service.impl;

import com.lsntsolutions.gtmApp.constant.State;
import com.lsntsolutions.gtmApp.dto.OrderDTO;
import com.lsntsolutions.gtmApp.dto.OrderDetailDTO;
import com.lsntsolutions.gtmApp.dto.PrinterResultDTO;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.persistence.dao.OrderDAO;
import com.lsntsolutions.gtmApp.query.DeliveryNoteQuery;
import com.lsntsolutions.gtmApp.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

		logger.info("Se ha armado el pedido para la para el Pedido n�mero: " + provisioningRequest.getId() + ". Id de pedido: "
				+ order.getId());

		return order;
	}

	@Override
	public Order save(Order order) {
		this.orderDAO.save(order);
		for(OrderDetail orderDetail : order.getOrderDetails()){
			this.stockService.removeFromStock(orderDetail, order.getAgreement());
		}
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
				this.stockService.removeFromStock(orderDetail, agreement);
				details.add(orderDetail);
			}

			order.setOrderDetails(details);

			return order;

		} catch (Exception e) {
			throw new RuntimeException("No se ha podido mappear el OrderDTO relacionado con el Pedido " + provisioningRequest.getId(), e);
		}
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
	public void cancel(Order order) {
		order.setCancelled(true);
		order.getProvisioningRequest().setState(this.provisioningRequestStateService.get(State.PRINTED.getId()));
		for (OrderDetail orderDetail : order.getOrderDetails()) {
			this.stockService.updateStock(orderDetail, order.getProvisioningRequest().getAgreement());
		}
		this.orderDAO.save(order);
		logger.info("Se ha anulado el Armado de Pedido n�mero: " + order.getId());
	}

	@Override
	public void cancelOrders(List<Integer> orderIds){
		Order order;
		for(Integer orderId : orderIds){
			order = this.get(orderId);
			if(order != null) {
				cancel(order);
			}
		}
	}

	@Override
	public boolean existSerial(Integer productId, String serial) {
		return this.orderDAO.existSerial(productId, serial);
	}

	@Override
	public List<Order> getAllFilter(Integer provisioningRequestId, Integer agreementId, Integer logisticsOperatorId, Integer clientId, Integer deliveryLocationId, Integer stateId) {
		return this.orderDAO.getAllFilter(provisioningRequestId, agreementId, logisticsOperatorId, clientId, deliveryLocationId, stateId);
	}

	@Override
	public void changeToPrintState(Order order) {
		ProvisioningRequest provisioningRequest = order.getProvisioningRequest();
		provisioningRequest.setState(provisioningRequestStateService.get(State.ASSEMBLED.getId()));
		this.provisioningRequestService.save(provisioningRequest);
	}

	@Override
	public List<Integer> filterAlreadyPrinted(List<Integer> ordersToPrint, PrinterResultDTO printerResultDTO) {
		Order order;
		List<Integer> filteredIds = new ArrayList<>();
		for(Integer id : ordersToPrint){
			order = this.get(id);
			if(order != null){
				if(order.getProvisioningRequest().getState().getId().equals(State.DELIVERY_NOTE_PRINTED.getId())){
					printerResultDTO.addErrorMessage("El remito correspondiente al pedido: " + order.getProvisioningRequest().getId() + " ya fue impreso.");
				}else{
					filteredIds.add(id);
				}
			}
		}

		return  filteredIds;
	}

	@Override
	public List<Agreement> getAgreementForOrderToPrint() {
		return this.orderDAO.getAgreementForOrderToPrint();
	}

	@Override
	public List<Client> getClientForOrderToPrint() {
		return this.orderDAO.getClientForOrderToPrint();
	}

	@Override
	public List<DeliveryLocation> getDeliveryLocationsForOrderToPrint() {
		return this.orderDAO.getDeliveryLocationsForOrderToPrint();
	}
}

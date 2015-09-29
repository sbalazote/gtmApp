package com.lsntsolutions.gtmApp.controllers;

import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.constant.State;
import com.lsntsolutions.gtmApp.dto.AssignOperatorDTO;
import com.lsntsolutions.gtmApp.dto.OrderDTO;
import com.lsntsolutions.gtmApp.helper.OrderLabelCreator;
import com.lsntsolutions.gtmApp.model.Order;
import com.lsntsolutions.gtmApp.model.ProvisioningRequest;
import com.lsntsolutions.gtmApp.model.Stock;
import com.lsntsolutions.gtmApp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private ProvisioningRequestService provisioningRequestService;
	@Autowired
	private StockService stockService;
	@Autowired
	private OrderLabelCreator orderLabelCreator;
	@Autowired
	private AuditService auditService;
	@Autowired
	private AgreementService agreementService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private LogisticsOperatorService logisticsOperatorService;

	@RequestMapping(value = "/orderAssembly", method = RequestMethod.GET)
	public String orderAssembly(ModelMap modelMap, @RequestParam Map<String, String> parameters) throws Exception {
		Integer provisioningRequestId = Integer.valueOf(parameters.get("id"));
		ProvisioningRequest provisioningRequest = this.provisioningRequestService.get(provisioningRequestId);

		Assert.notNull(provisioningRequest, "Sol. de Abastecimiento con identificador = " + provisioningRequestId + " no encontrada!");

		modelMap.put("provisioningRequestId", provisioningRequest.getId());
		modelMap.put("deliveryLocation", provisioningRequest.getDeliveryLocation());
		modelMap.put("agreement", provisioningRequest.getAgreement());
		modelMap.put("logisticsOperator", provisioningRequest.getLogisticsOperator());
		modelMap.put("deliveryDate", provisioningRequest.getDeliveryDate());
		modelMap.put("affiliate", provisioningRequest.getAffiliate());
		modelMap.put("client", provisioningRequest.getClient());
		modelMap.put("provisioningRequestDetails", provisioningRequest.getProvisioningRequestDetails());

		return "orderAssembly";
	}

	@RequestMapping(value = "/orderAssemblySelection", method = RequestMethod.GET)
	public String orderAssemblySelection(ModelMap modelMap) throws Exception {
		modelMap.put("agreements", this.agreementService.getAllActives());
		modelMap.put("clients", this.clientService.getAllActives());
		return "orderAssemblySelection";
	}

	@RequestMapping(value = "/saveOrder", method = RequestMethod.POST)
	public @ResponseBody Order saveOrder(@RequestBody OrderDTO orderDTO) throws Exception {
		Order order = this.orderService.save(orderDTO);
		this.orderLabelCreator.getLabelFile(order);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			this.auditService.addAudit(auth.getName(), RoleOperation.ORDER_ASSEMBLY.getId(), AuditState.COMFIRMED, order.getId());
		}
		return order;
	}

	@RequestMapping(value = "/getBatchExpirationDateStock", method = RequestMethod.GET)
	public @ResponseBody List<Stock> getBatchExpirationDateStock(@RequestParam Map<String, String> parameters) {
		Integer productId = Integer.valueOf(parameters.get("productId"));
		Integer agreementId = Integer.valueOf(parameters.get("agreementId"));
		return this.stockService.getBatchExpirationDateStock(productId, agreementId);
	}

	@RequestMapping(value = "/getSerializedStock", method = RequestMethod.GET)
	public @ResponseBody Stock getSerializedStock(@RequestParam Map<String, String> parameters) {
		Integer productId = Integer.valueOf(parameters.get("productId"));
		String serialNumber = String.valueOf(parameters.get("serialNumber"));
		String gtin = String.valueOf(parameters.get("gtin"));
		Integer agreementId = Integer.valueOf(parameters.get("agreementId"));
		return this.stockService.getSerializedStock(productId, serialNumber, gtin, agreementId);
	}

	@RequestMapping(value = "/orderSaved", method = RequestMethod.POST)
	public String orderSaved(ModelMap modelMap, @RequestParam Map<String, String> parameters) throws Exception {
		Integer provisioningRequestId = Integer.valueOf(parameters.get("provisioningRequestId"));
		modelMap.put("provisioningRequestId", provisioningRequestId);
		return "orderSaved";
	}

	@RequestMapping(value = "/orderCancellation", method = RequestMethod.GET)
	public String provisioningRequestCancellation(ModelMap modelMap) throws Exception {
		modelMap.put("orders", this.orderService.getAllByState(State.ASSEMBLED.getId()));
		return "orderCancellation";
	}

	@RequestMapping(value = "/getOrder", method = RequestMethod.GET)
	public @ResponseBody Order getOrder(@RequestParam Integer orderId) throws Exception {
		Order order = this.orderService.get(orderId);
		return order;
	}

	@RequestMapping(value = "/cancelOrders", method = RequestMethod.POST)
	public @ResponseBody void cancelOrders(@RequestBody List<Integer> orderIds) throws Exception {
		this.orderService.cancelOrders(orderIds);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			for (Integer orderId : orderIds) {
				this.auditService.addAudit(auth.getName(), RoleOperation.ORDER_ASSEMBLY_CANCELLATION.getId(), AuditState.CANCELLED, orderId);
			}
		}
	}

	@RequestMapping(value = "/getOrdersToPrint", method = RequestMethod.GET)
	public @ResponseBody List<Order> getOrdersToPrint(@RequestParam Integer agreementId, Integer clientId) {
		return this.orderService.getAllFilter(agreementId, clientId, State.ASSEMBLED.getId());
	}

	@RequestMapping(value = "/getAuthorizedProvisioningsForOrders", method = RequestMethod.GET)
	public @ResponseBody List<ProvisioningRequest> getAuthorizedProvisioningsForOrders(@RequestParam Integer agreementId, Integer clientId) {
		List<ProvisioningRequest> provisionings = this.provisioningRequestService.getFilterProvisionings(agreementId, clientId, State.AUTHORIZED.getId());
		provisionings.addAll(this.provisioningRequestService.getFilterProvisionings(agreementId, clientId, State.PRINTED.getId()));
		return provisionings;
	}

	@RequestMapping(value = "/logisticOperatorAssignment", method = RequestMethod.GET)
	public String deliveryNoteSheet(ModelMap modelMap) throws Exception {
		modelMap.put("agreements", this.agreementService.getAllActives());
		modelMap.put("clients", this.clientService.getAllActives());
		modelMap.put("logisticsOperators", this.logisticsOperatorService.getAllActives());

		return "logisticOperatorAssignment";
	}

	@RequestMapping(value = "/assignOperatorToOrders", method = RequestMethod.POST)
	@ResponseBody
	public List<Integer> assignOperatorToOrders(@RequestBody AssignOperatorDTO assignOperatorDTO) throws Exception {
		this.orderService.reassignOperators(assignOperatorDTO.getOrdersIdsToReassign(), assignOperatorDTO.getLogisticOperatorId());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			for (Integer orderId : assignOperatorDTO.getOrdersIdsToReassign()) {
				this.auditService.addAudit(auth.getName(), RoleOperation.ORDER_ASSEMBLY.getId(), AuditState.MODIFIED, orderId);
			}
		}
		return assignOperatorDTO.getOrdersIdsToReassign();
	}
}

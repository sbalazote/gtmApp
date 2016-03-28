package com.lsntsolutions.gtmApp.controllers;

import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.constant.State;
import com.lsntsolutions.gtmApp.dto.OrderDTO;
import com.lsntsolutions.gtmApp.dto.PrinterResultDTO;
import com.lsntsolutions.gtmApp.helper.impl.printer.OrderLabelPrinter;
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

import java.util.ArrayList;
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
	private OrderLabelPrinter orderLabelPrinter;
	@Autowired
	private AuditService auditService;
	@Autowired
	private AgreementService agreementService;
	@Autowired
	private DeliveryLocationService deliveryLocationService;
	@Autowired
	private LogisticsOperatorService logisticsOperatorService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private DeliveryNoteService deliveryNoteService;

	@RequestMapping(value = "/orderAssembly", method = RequestMethod.GET)
	public String orderAssembly(ModelMap modelMap, @RequestParam Map<String, String> parameters) throws Exception {
		Integer provisioningRequestId = Integer.valueOf(parameters.get("id"));
		ProvisioningRequest provisioningRequest = this.provisioningRequestService.get(provisioningRequestId);

		String agreementSearchFilterId = parameters.get("agreementSearchFilterId");
		String clientSearchFilterId = parameters.get("clientSearchFilterId");

		Assert.notNull(provisioningRequest, "Sol. de Abastecimiento con identificador = " + provisioningRequestId + " no encontrada!");

		modelMap.put("provisioningRequestId", provisioningRequest.getId());
		modelMap.put("provisioningRequestIdFormated", provisioningRequest.getFormatId());
		modelMap.put("deliveryLocation", provisioningRequest.getDeliveryLocation());
		modelMap.put("agreement", provisioningRequest.getAgreement());
		modelMap.put("logisticsOperator", provisioningRequest.getLogisticsOperator());
		modelMap.put("deliveryDate", provisioningRequest.getDeliveryDate());
		modelMap.put("affiliate", provisioningRequest.getAffiliate());
		modelMap.put("client", provisioningRequest.getClient());
		modelMap.put("provisioningRequestDetails", provisioningRequest.getProvisioningRequestDetails());

		if (!agreementSearchFilterId.isEmpty()) {
			modelMap.put("agreementSearchFilterId", Integer.valueOf(agreementSearchFilterId));
		}
		if (!clientSearchFilterId.isEmpty()) {
			modelMap.put("clientSearchFilterId", Integer.valueOf(clientSearchFilterId));
		}

		return "orderAssembly";
	}

	@RequestMapping(value = "/orderAssemblySelection", method = RequestMethod.GET)
	public String orderAssemblySelection(ModelMap modelMap, @RequestParam(required = false) Integer agreementSearchFilterId, @RequestParam(required = false) Integer clientSearchFilterId, @RequestParam(required = false) Boolean orderAssemblySelectionReturn) throws Exception {
		modelMap.put("agreements", this.agreementService.getAllActives());
		modelMap.put("clients", this.clientService.getAllActives());

		modelMap.put("agreementSearchFilterId", agreementSearchFilterId);
		modelMap.put("clientSearchFilterId", clientSearchFilterId);
		modelMap.put("orderAssemblySelectionReturn", orderAssemblySelectionReturn);
		return "orderAssemblySelection";
	}

	@RequestMapping(value = "/saveOrder", method = RequestMethod.POST)
	synchronized public @ResponseBody
	PrinterResultDTO saveOrder(@RequestBody OrderDTO orderDTO) throws Exception {
		ProvisioningRequest provisioningRequest = this.provisioningRequestService.get(orderDTO.getProvisioningRequestId());
		if(provisioningRequest != null && provisioningRequest.getState().getId() < State.ASSEMBLED.getId()) {
			Order order = this.orderService.save(orderDTO);
			PrinterResultDTO printerResultDTO = new PrinterResultDTO(order.getFormatId());
			// imprimo rotulo para pedido solo si el parametro 'picking_list' en convenio esta seteado.
			if (order.getProvisioningRequest().getAgreement().isPickingList() && this.propertyService.get().isPrintPickingList()) {
				this.orderLabelPrinter.print(order, printerResultDTO);
			}
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {
				this.auditService.addAudit(auth.getName(), RoleOperation.ORDER_ASSEMBLY.getId(), order.getId());
			}
			return printerResultDTO;
		}else{
			PrinterResultDTO printerResultDTO = new PrinterResultDTO();
			List<String> errors = new ArrayList<>();
			errors.add("No se puede generar el armado dado que el pedido ya se encuentra cerrado");
			printerResultDTO.setErrorMessages(errors);
			return printerResultDTO;
		}
	}

	@RequestMapping(value = "/getBatchExpirationDateStock", method = RequestMethod.GET)
	public @ResponseBody List<Stock> getBatchExpirationDateStock(@RequestParam Map<String, String> parameters) {
		Integer productId = Integer.valueOf(parameters.get("productId"));
		Integer agreementId = Integer.valueOf(parameters.get("agreementId"));
		return this.stockService.getBatchExpirationDateStock(productId, agreementId);
	}

	@RequestMapping(value = "/getSerializedStock", method = RequestMethod.GET)
	public @ResponseBody Stock getSerializedStock(@RequestParam Integer productId, @RequestParam String serialNumber, @RequestParam(required = false) String batch, @RequestParam(required = false) String expirationDate, @RequestParam String gtin, @RequestParam Integer agreementId) {
		return this.stockService.getSerializedStock(productId, serialNumber, batch, expirationDate, gtin, agreementId);
	}

	@RequestMapping(value = "/getStockByParseSerial", method = RequestMethod.GET)
	public @ResponseBody Stock getStockByParseSerial(@RequestParam Map<String, String> parameters) {
		Integer productId = Integer.valueOf(parameters.get("productId"));
		String serialNumber = String.valueOf(parameters.get("serialNumber"));
		Integer agreementId = Integer.valueOf(parameters.get("agreementId"));
		return this.stockService.getStockByParseSerial(productId, serialNumber, agreementId);
	}

	@RequestMapping(value = "/orderSaved", method = RequestMethod.POST)
	public String orderSaved(ModelMap modelMap, @RequestParam Map<String, String> parameters) throws Exception {
		Integer provisioningRequestId = Integer.valueOf(parameters.get("provisioningRequestId"));
		modelMap.put("provisioningRequestId", provisioningRequestId);
		return "orderSaved";
	}

	@RequestMapping(value = "/orderCancelation", method = RequestMethod.GET)
	public String orderCancelation(ModelMap modelMap) throws Exception {
		modelMap.put("cancellation", true);
		modelMap.put("orders", this.orderService.getAllByState(State.ASSEMBLED.getId()));
		return "orderCancelation";
	}

	@RequestMapping(value = "/reprintOrderLabel", method = RequestMethod.GET)
	public String reprintOrderLabel(ModelMap modelMap) throws Exception {
		modelMap.put("cancellation", false);
		return "reprintOrderLabel";
	}

	@RequestMapping(value = "/getPrintableOrCancelableOrder", method = RequestMethod.POST)
	public @ResponseBody List<Order> getPrintableOrCancelableOrder(@RequestParam Integer provisioningId, @RequestParam Boolean isCancellation) throws Exception {
		List<Order> orders = this.orderService.getPrintableOrCancelableOrder(provisioningId, isCancellation);
		return orders;
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
				this.auditService.addAudit(auth.getName(), RoleOperation.ORDER_ASSEMBLY_CANCELLATION.getId(), orderId);
			}
		}
	}

	@RequestMapping(value = "/getOrdersToPrint", method = RequestMethod.GET)
	public @ResponseBody List<Order> getOrdersToPrint(@RequestParam Integer provisioningRequestId, Integer agreementId, Integer logisticsOperatorId, Integer clientId, Integer deliveryLocationId) {
		return this.orderService.getAllFilter(provisioningRequestId, agreementId, logisticsOperatorId, clientId, deliveryLocationId, State.ASSEMBLED.getId());
	}

	@RequestMapping(value = "/getAuthorizedProvisioningsForOrders", method = RequestMethod.GET)
	public @ResponseBody List<ProvisioningRequest> getAuthorizedProvisioningsForOrders(@RequestParam Integer provisioningRequestId, Integer agreementId, Integer logisticsOperatorId, Integer clientId, Integer deliveryLocationId, Integer stateId) {
		if(stateId != null){
			return this.provisioningRequestService.getFilterProvisionings(provisioningRequestId, agreementId, logisticsOperatorId, clientId, deliveryLocationId, stateId);
		}else {
			List<ProvisioningRequest> provisionings = this.provisioningRequestService.getFilterProvisionings(provisioningRequestId, agreementId, logisticsOperatorId, clientId, deliveryLocationId, State.AUTHORIZED.getId());
			provisionings.addAll(this.provisioningRequestService.getFilterProvisionings(provisioningRequestId, agreementId, logisticsOperatorId, clientId, deliveryLocationId, State.PRINTED.getId()));
			if (!this.propertyService.get().isProvisioningRequireAuthorization()) {
				provisionings.addAll(this.provisioningRequestService.getFilterProvisionings(provisioningRequestId, agreementId, logisticsOperatorId, clientId, deliveryLocationId, State.ENTERED.getId()));
			}
			return provisionings;
		}
	}

	@RequestMapping(value = "/logisticOperatorAssignment", method = RequestMethod.GET)
	public String deliveryNoteSheet(ModelMap modelMap) throws Exception {
		modelMap.put("agreements", this.agreementService.getAllActives());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAllActives());
		modelMap.put("logisticsOperators", this.logisticsOperatorService.getAllActives(false));
		modelMap.put("clients", this.clientService.getAllActives());
		return "logisticOperatorAssignment";
	}

	@RequestMapping(value = "/getOrdersDeliveriesNoteNumbers", method = RequestMethod.GET)
	public @ResponseBody
	List<String> getOrdersDeliveriesNoteNumbers(@RequestParam Integer orderId) {
		return this.deliveryNoteService.getOrdersDeliveriesNoteNumbers(orderId);
	}
}
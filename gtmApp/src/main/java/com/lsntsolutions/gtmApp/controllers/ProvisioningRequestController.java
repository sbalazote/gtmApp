package com.lsntsolutions.gtmApp.controllers;

import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.constant.DocumentType;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.constant.State;
import com.lsntsolutions.gtmApp.dto.AssignOperatorDTO;
import com.lsntsolutions.gtmApp.dto.ProvisioningRequestDTO;
import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.model.Order;
import com.lsntsolutions.gtmApp.model.ProvisioningRequest;
import com.lsntsolutions.gtmApp.model.ProvisioningRequestDetail;
import com.lsntsolutions.gtmApp.query.ProvisioningQuery;
import com.lsntsolutions.gtmApp.service.*;
import com.lsntsolutions.gtmApp.util.OperationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProvisioningRequestController {

	@Autowired
	private ClientService clientService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private DeliveryNoteService deliveryNoteService;
	@Autowired
	private DeliveryLocationService deliveryLocationService;
	@Autowired
	private AgreementService agreementService;
	@Autowired
	private LogisticsOperatorService logisticsOperatorService;
	@Autowired
	private ProvisioningRequestService provisioningRequestService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private AffiliateService affiliateService;

	@RequestMapping(value = "/provisioningRequest", method = RequestMethod.GET)
	public String provisioningRequest(ModelMap modelMap) throws Exception {
		modelMap.put("agreements", this.agreementService.getAllActives());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAllActives());
		modelMap.put("logisticsOperators", this.logisticsOperatorService.getAllActives());
		modelMap.put("clients", this.clientService.getAllActives());
		modelMap.put("deliveryDate", new Date());
		modelMap.put("documentTypes", DocumentType.types);
		return "provisioningRequest";
	}

	@RequestMapping(value = "/saveProvisioningRequest", method = RequestMethod.POST)
	public synchronized @ResponseBody
	OperationResult saveProvisioningRequest(@RequestBody ProvisioningRequestDTO provisioningRequestDTO) throws Exception {
		OperationResult operationResult = this.provisioningRequestService.save(provisioningRequestDTO);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && operationResult.getResultado()) {
			if (provisioningRequestDTO.getId() == null) {
				this.auditService.addAudit(auth.getName(), RoleOperation.PROVISIONING_REQUEST.getId(), AuditState.COMFIRMED, Integer.valueOf(operationResult.getOperationId()));
			} else {
				this.auditService.addAudit(auth.getName(), RoleOperation.PROVISIONING_REQUEST.getId(), AuditState.MODIFIED, Integer.valueOf(operationResult.getOperationId()));
			}
		}
		return operationResult;
	}

	@RequestMapping(value = "/getProvisioningRequest", method = RequestMethod.GET)
	public @ResponseBody
	ProvisioningRequest getProvisioningRequest(@RequestParam Integer provisioningId) {
		return this.provisioningRequestService.get(provisioningId);
	}

	@RequestMapping(value = "/getProvisioningRequestDetails", method = RequestMethod.GET)
	public @ResponseBody
	List<ProvisioningRequestDetail> getProvisioningRequestDetails(@RequestParam Integer provisioningId) {
		ProvisioningRequest provisioningRequest = this.provisioningRequestService.get(provisioningId);
		if(provisioningRequest != null){
			return provisioningRequest.getProvisioningRequestDetails();
		}else{
			return null;
		}
	}

	@RequestMapping(value = "/getAssociatedDeliveryNotes", method = RequestMethod.GET)
	public @ResponseBody
	List<DeliveryNote> getAssociatedDeliveryNotes(@RequestParam Integer provisioningId) {
		Order order = this.orderService.getOrderByProvisioningRequestId(provisioningId);
		Map<Integer, List<DeliveryNote>> associatedOrders = this.deliveryNoteService.getAssociatedOrders();
		return associatedOrders.get(order.getId());
	}

	@RequestMapping(value = "/getProvisioningForSearch", method = RequestMethod.POST)
	public @ResponseBody
	List<ProvisioningRequest> getProvisioningForSearch(@RequestBody ProvisioningQuery provisioningQuery) throws Exception {
		return this.provisioningRequestService.getProvisioningForSearch(provisioningQuery);
	}

	@RequestMapping(value = "/getCountOfProvisioningSearch", method = RequestMethod.POST)
	public @ResponseBody
	boolean getCountOfProvisioningSearch(@RequestBody ProvisioningQuery provisioningQuery) throws Exception {
		return this.provisioningRequestService.getCountOfProvisioningSearch(provisioningQuery);
	}

	@RequestMapping(value = "/searchProvisioningToUpdate", method = RequestMethod.GET)
	public String searchProvisioningToUpdate(ModelMap modelMap) throws Exception {
		modelMap.put("provisionings", this.provisioningRequestService.getAllByState(State.ENTERED.getId()));

		return "searchProvisioningToUpdate";
	}

	@RequestMapping(value = "/updateProvisioningRequest", method = RequestMethod.GET)
	public String updateProvisioningRequest(ModelMap modelMap, @RequestParam Map<String, String> parameters) throws Exception {
		Integer provisioningRequestId = Integer.valueOf(parameters.get("id"));
		ProvisioningRequest provisioningRequest = this.provisioningRequestService.get(provisioningRequestId);

		Assert.notNull(provisioningRequest, "Sol. de Abastecimiento con identificador = " + provisioningRequestId + " no encontrada!");
		Assert.isTrue(provisioningRequest.getState().getId().equals(State.ENTERED.getId()), "Pedido no es modificable!");

		modelMap.put("agreements", this.agreementService.getAllActives());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAllActives());
		modelMap.put("logisticsOperators", this.logisticsOperatorService.getAllActives());
		modelMap.put("clients", this.clientService.getAllActives());
		modelMap.put("affiliates", this.affiliateService.getAllAffiliatesByClient(provisioningRequest.getClient().getId(), true));
		modelMap.put("deliveryDate", provisioningRequest.getDeliveryDate());
		modelMap.put("comment", provisioningRequest.getComment());

		modelMap.put("provisioningRequestId", provisioningRequest.getId());
		modelMap.put("agreementId", provisioningRequest.getAgreement().getId());
		modelMap.put("deliveryLocationId", provisioningRequest.getDeliveryLocation().getId());
		if (provisioningRequest.getLogisticsOperator() != null) {
			modelMap.put("logisticsOperatorId", provisioningRequest.getLogisticsOperator().getId());
		}
		modelMap.put("clientId", provisioningRequest.getClient().getId());
		modelMap.put("affiliateId", provisioningRequest.getAffiliate().getId());
		modelMap.put("affiliate", provisioningRequest.getAffiliate());
		modelMap.put("productsSelected", provisioningRequest.getProvisioningRequestDetails());

		return "provisioningRequest";
	}

	@RequestMapping(value = "/provisioningRequestAuthorization", method = RequestMethod.GET)
	public String provisioningRequestAuthorization(ModelMap modelMap) throws Exception {
		modelMap.put("agreements", this.agreementService.getAllActives());
		modelMap.put("clients", this.clientService.getAllActives());
		return "provisioningRequestAuthorization";
	}

	@RequestMapping(value = "/getProvisioningsToAuthorize", method = RequestMethod.GET)
	public @ResponseBody
	List<ProvisioningRequest> getProvisioningsToAuthorize(@RequestParam Integer agreementId, Integer clientId) {
		return this.provisioningRequestService.getFilterProvisionings(null, agreementId, clientId, State.ENTERED.getId());
	}

	@RequestMapping(value = "/authorizeProvisioningRequests", method = RequestMethod.POST)
	public @ResponseBody
	void authorizeProvisioningRequests(@RequestBody List<Integer> provisioningIds) throws Exception {
		this.provisioningRequestService.authorizeProvisioningRequests(provisioningIds);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			for (Integer provisioningId : provisioningIds) {
				this.auditService.addAudit(auth.getName(), RoleOperation.PROVISIONING_REQUEST_AUTHORIZATION.getId(), AuditState.AUTHORITED, provisioningId);
			}
		}
	}

	@RequestMapping(value = "/provisioningRequestCancellation", method = RequestMethod.GET)
	public String provisioningRequestCancellation(ModelMap modelMap) throws Exception {
		return "provisioningRequestCancellation";
	}

	@RequestMapping(value = "/getCancelablesProvisionings", method = RequestMethod.POST)
	public @ResponseBody
	List<ProvisioningRequest> getCancelables(@RequestParam String provisioningId) throws Exception {
		ProvisioningQuery pq = new ProvisioningQuery();
		pq.setProvisioningId(provisioningId.isEmpty() ? null : Integer.parseInt(provisioningId));
		pq.setStateId(State.ENTERED.getId());
		List<ProvisioningRequest> provisionings = this.provisioningRequestService.getProvisioningForSearch(pq);
		pq.setStateId(State.AUTHORIZED.getId());
		provisionings.addAll(this.provisioningRequestService.getProvisioningForSearch(pq));
		return provisionings;
	}

	@RequestMapping(value = "/cancelProvisioningRequests", method = RequestMethod.POST)
	public @ResponseBody
	boolean cancelProvisioningRequests(@RequestBody Integer provisioningId) throws Exception {
		boolean result = this.provisioningRequestService.cancelProvisioningRequest(provisioningId);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			this.auditService.addAudit(auth.getName(), RoleOperation.PROVISIONING_REQUEST_CANCELLATION.getId(), AuditState.CANCELLED, provisioningId);
		}
		return result;
	}

	@RequestMapping(value = "/provisionings", method = RequestMethod.POST)
	public ModelAndView provisionings(HttpServletRequest request) {
		ProvisioningQuery provisioningQuery = this.getProvisioningQuery(request);
		ModelAndView modelAndView =  new ModelAndView("provisionings");
		ModelMap modelMap = new ModelMap();
		List<ProvisioningRequest> provisioningRequests = this.provisioningRequestService.getProvisioningForSearch(provisioningQuery);
		modelMap.put("provisionings", provisioningRequests);
		HashMap<Integer, Date> provisioningConfirmDates = new HashMap<>();
		for(ProvisioningRequest provisioningRequest : provisioningRequests){
			provisioningConfirmDates.put(provisioningRequest.getId(),this.auditService.getDate(RoleOperation.PROVISIONING_REQUEST, provisioningRequest.getId(),AuditState.COMFIRMED));
		}
		modelMap.put("confirmDates", provisioningConfirmDates);
		modelAndView.addAllObjects(modelMap);
		return modelAndView;
	}

	private ProvisioningQuery getProvisioningQuery(HttpServletRequest request) {
		Integer id = null;
		if (!(request.getParameterValues("id")[0]).equals("null")) {
			id = Integer.valueOf(request.getParameterValues("id")[0]);
		}
		Integer agreementId = null;
		if (!(request.getParameterValues("agreementId")[0]).equals("null")) {
			agreementId = Integer.valueOf(request.getParameterValues("agreementId")[0]);
		}
		Integer clientId = null;
		if (!(request.getParameterValues("clientId")[0]).equals("null")) {
			clientId = Integer.valueOf(request.getParameterValues("clientId")[0]);
		}

		Integer affiliateId = null;
		if (!(request.getParameterValues("affiliateId")[0]).equals("null")) {
			affiliateId = Integer.valueOf(request.getParameterValues("affiliateId")[0]);
		}

		Integer deliveryLocation = null;
		if (!(request.getParameterValues("deliveryLocation")[0]).equals("null")) {
			deliveryLocation = Integer.valueOf(request.getParameterValues("deliveryLocation")[0]);
		}

		Integer logisticsOperator = null;
		if (!(request.getParameterValues("logisticsOperator")[0]).equals("null")) {
			logisticsOperator = Integer.valueOf(request.getParameterValues("logisticsOperator")[0]);
		}

		Integer stateId = null;
		if (!(request.getParameterValues("stateId")[0]).equals("null")) {
			stateId = Integer.valueOf(request.getParameterValues("stateId")[0]);
		}
		ProvisioningQuery provisioningQuery = ProvisioningQuery.createFromParameters(id, request.getParameterValues("dateFrom")[0],
				request.getParameterValues("dateTo")[0], agreementId, clientId, affiliateId, request.getParameterValues("comment")[0], deliveryLocation,
				logisticsOperator, stateId);

		return provisioningQuery;
	}

	@RequestMapping(value = "/assignOperatorToProvisionings", method = RequestMethod.POST)
	@ResponseBody
	public List<Integer> assignOperatorToOrders(@RequestBody AssignOperatorDTO assignOperatorDTO) throws Exception {
		this.provisioningRequestService.reassignOperators(assignOperatorDTO.getProvisioningsIdsToReassign(), assignOperatorDTO.getLogisticOperatorId());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			for (Integer orderId : assignOperatorDTO.getProvisioningsIdsToReassign()) {
				this.auditService.addAudit(auth.getName(), RoleOperation.ORDER_ASSEMBLY.getId(), AuditState.MODIFIED, orderId);
			}
		}
		return assignOperatorDTO.getProvisioningsIdsToReassign();
	}
}

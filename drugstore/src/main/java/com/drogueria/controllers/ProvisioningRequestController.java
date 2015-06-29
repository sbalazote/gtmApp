package com.drogueria.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.drogueria.constant.DocumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.drogueria.constant.AuditState;
import com.drogueria.constant.RoleOperation;
import com.drogueria.constant.State;
import com.drogueria.dto.ProvisioningRequestDTO;
import com.drogueria.model.ProvisioningRequest;
import com.drogueria.query.ProvisioningQuery;
import com.drogueria.service.AffiliateService;
import com.drogueria.service.AgreementService;
import com.drogueria.service.AuditService;
import com.drogueria.service.ClientService;
import com.drogueria.service.DeliveryLocationService;
import com.drogueria.service.LogisticsOperatorService;
import com.drogueria.service.ProvisioningRequestService;

@Controller
public class ProvisioningRequestController {

	@Autowired
	private ClientService clientService;
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
	public @ResponseBody
	ProvisioningRequest saveProvisioningRequest(@RequestBody ProvisioningRequestDTO provisioningRequestDTO) throws Exception {
		ProvisioningRequest provisioningRequest = this.provisioningRequestService.save(provisioningRequestDTO);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			if (provisioningRequestDTO.getId() == null) {
				this.auditService.addAudit(auth.getName(), RoleOperation.PROVISIONING_REQUEST.getId(), AuditState.COMFIRMED, provisioningRequest.getId());
			} else {
				this.auditService.addAudit(auth.getName(), RoleOperation.PROVISIONING_REQUEST.getId(), AuditState.MODIFIED, provisioningRequest.getId());
			}
		}
		return provisioningRequest;
	}

	@RequestMapping(value = "/getProvisioningRequest", method = RequestMethod.GET)
	public @ResponseBody
	ProvisioningRequest getProvisioningRequest(@RequestParam Integer provisioningId) {
		return this.provisioningRequestService.get(provisioningId);
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

		Assert.notNull(provisioningRequest, "Provisioning Request with id " + provisioningRequestId + " not found!");
		Assert.isTrue(provisioningRequest.getState().getId().equals(State.ENTERED.getId()), "Provisioning request is not modifiable!");

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
		return this.provisioningRequestService.getFilterProvisionings(agreementId, clientId, State.ENTERED.getId());
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
		List<ProvisioningRequest> provisionings = this.provisioningRequestService.getAllByState(State.ENTERED.getId());
		provisionings.addAll(this.provisioningRequestService.getAllByState(State.AUTHORIZED.getId()));
		modelMap.put("provisionings", provisionings);
		return "provisioningRequestCancellation";
	}

	@RequestMapping(value = "/cancelProvisioningRequests", method = RequestMethod.POST)
	public @ResponseBody
	void cancelProvisioningRequests(@RequestBody List<Integer> provisioningIds) throws Exception {
		this.provisioningRequestService.cancelProvisioningRequests(provisioningIds);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			for (Integer provisioningId : provisioningIds) {
				this.auditService.addAudit(auth.getName(), RoleOperation.PROVISIONING_REQUEST_CANCELLATION.getId(), AuditState.CANCELLED, provisioningId);
			}
		}
	}

	@RequestMapping(value = "/provisionings", method = RequestMethod.POST)
	public ModelAndView provisionings(HttpServletRequest request) {
		ProvisioningQuery provisioningQuery = this.getProvisioningQuery(request);
		return new ModelAndView("provisionings", "provisionings", this.provisioningRequestService.getProvisioningForSearch(provisioningQuery));
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
}

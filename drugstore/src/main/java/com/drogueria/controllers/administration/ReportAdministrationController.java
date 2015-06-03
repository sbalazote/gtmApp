package com.drogueria.controllers.administration;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.drogueria.constant.AuditState;
import com.drogueria.constant.RoleOperation;
import com.drogueria.service.AffiliateService;
import com.drogueria.service.AgreementService;
import com.drogueria.service.AuditService;
import com.drogueria.service.ClientService;
import com.drogueria.service.ConceptService;
import com.drogueria.service.DeliveryLocationService;
import com.drogueria.service.LogisticsOperatorService;
import com.drogueria.service.ProviderService;
import com.drogueria.service.ProvisioningRequestService;
import com.drogueria.service.ProvisioningRequestStateService;
import com.drogueria.service.UserService;

@Controller
public class ReportAdministrationController {

	@Autowired
	private ConceptService conceptService;
	@Autowired
	private ProviderService providerService;
	@Autowired
	private AgreementService agreementService;
	@Autowired
	private LogisticsOperatorService logisticsOperatorService;
	@Autowired
	private ProvisioningRequestService provisioningRequestService;
	@Autowired
	private ProvisioningRequestStateService provisioningRequestStateService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private DeliveryLocationService deliveryLocationService;
	@Autowired
	private AffiliateService affiliateService;
	@Autowired
	private UserService userService;
	@Autowired
	private AuditService auditService;

	@RequestMapping(value = "/searchInput", method = RequestMethod.GET)
	public String searchInput(ModelMap modelMap) throws Exception {
		modelMap.put("concepts", this.conceptService.getAll());
		modelMap.put("providers", this.providerService.getAll());
		modelMap.put("agreements", this.agreementService.getAll());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAll());
		return "searchInput";
	}

	@RequestMapping(value = "/searchStock", method = RequestMethod.GET)
	public String searchStock(ModelMap modelMap) throws Exception {
		modelMap.put("agreements", this.agreementService.getAll());
		modelMap.put("concepts", this.conceptService.getAll());
		modelMap.put("providers", this.providerService.getAll());
		return "searchStock";
	}

	@RequestMapping(value = "/searchProvisioningRequest", method = RequestMethod.GET)
	public String searchProvisioningRequest(ModelMap modelMap) throws Exception {
		modelMap.put("provisionings", this.provisioningRequestService.getAll());
		modelMap.put("agreements", this.agreementService.getAll());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAll());
		modelMap.put("logisticsOperators", this.logisticsOperatorService.getAll());
		modelMap.put("clients", this.clientService.getAll());
		modelMap.put("states", this.provisioningRequestStateService.getAll());

		return "searchProvisioningRequest";
	}

	@RequestMapping(value = "/searchDeliveryNote", method = RequestMethod.GET)
	public String searchDeliveryNote(ModelMap modelMap) throws Exception {
		modelMap.put("providers", this.providerService.getAll());
		modelMap.put("agreements", this.agreementService.getAll());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAll());
		return "searchDeliveryNote";
	}

	@RequestMapping(value = "/searchAudit", method = RequestMethod.GET)
	public String searchAudit(ModelMap modelMap) throws Exception {
		List<AuditState> auditActionList = Arrays.asList(AuditState.values());
		List<RoleOperation> roleList = Arrays.asList(RoleOperation.values());
		modelMap.put("auditActions", auditActionList);
		modelMap.put("roles", roleList);
		modelMap.put("users", this.userService.getAll());
		this.auditService.getAudit(21892, "24028970");
		return "searchAudit";
	}

	@RequestMapping(value = "/searchOutput", method = RequestMethod.GET)
	public String searchOutput(ModelMap modelMap) throws Exception {
		modelMap.put("concepts", this.conceptService.getAll());
		modelMap.put("providers", this.providerService.getAll());
		modelMap.put("agreements", this.agreementService.getAll());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAll());
		return "searchOutput";
	}

	@RequestMapping(value = "/searchSerializedProduct", method = RequestMethod.GET)
	public String searchSerializedProduct(ModelMap modelMap) throws Exception {
		return "searchSerializedProduct";
	}

	@RequestMapping(value = "/searchBatchExpirateDateProduct", method = RequestMethod.GET)
	public String searchBatchExpirateDateProduct(ModelMap modelMap) throws Exception {
		return "searchBatchExpirateDateProduct";
	}

    @RequestMapping(value = "/searchSupplying", method = RequestMethod.GET)
    public String searchSupplying(ModelMap modelMap) throws Exception {
        modelMap.put("clients", this.clientService.getAll());
        modelMap.put("agreements", this.agreementService.getAll());
        return "searchSupplying";
    }

}

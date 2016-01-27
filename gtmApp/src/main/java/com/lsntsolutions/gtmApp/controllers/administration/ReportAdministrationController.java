package com.lsntsolutions.gtmApp.controllers.administration;

import java.util.Arrays;
import java.util.List;

import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.service.*;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	private ProvisioningRequestStateService provisioningRequestStateService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private DeliveryLocationService deliveryLocationService;
	@Autowired
	private UserService userService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private ProductMonodrugService productMonodrugService;

	@RequestMapping(value = "/searchInput", method = RequestMethod.GET)
	public String searchInput(ModelMap modelMap) throws Exception {
		modelMap.put("concepts", this.conceptService.getAll());
		modelMap.put("providers", this.providerService.getAll());
		modelMap.put("agreements", this.agreementService.getAll());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAll());
		modelMap.put("monodrugs", this.productMonodrugService.getAll());
		return "searchInput";
	}

	@RequestMapping(value = "/searchStock", method = RequestMethod.GET)
	public String searchStock(ModelMap modelMap) throws Exception {
		modelMap.put("agreements", this.agreementService.getAll());
		modelMap.put("concepts", this.conceptService.getAll());
		modelMap.put("providers", this.providerService.getAll());
		modelMap.put("monodrugs", this.productMonodrugService.getAll());
		return "searchStock";
	}

	@RequestMapping(value = "/searchProvisioningRequest", method = RequestMethod.GET)
	public String searchProvisioningRequest(ModelMap modelMap) throws Exception {
		modelMap.put("agreements", this.agreementService.getAll());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAll());
		modelMap.put("logisticsOperators", this.logisticsOperatorService.getAll());
		modelMap.put("clients", this.clientService.getAll());
		modelMap.put("states", this.provisioningRequestStateService.getAll());
		modelMap.put("monodrugs", this.productMonodrugService.getAll());
		return "searchProvisioningRequest";
	}

	@RequestMapping(value = "/searchDeliveryNote", method = RequestMethod.GET)
	public String searchDeliveryNote(ModelMap modelMap) throws Exception {
		modelMap.put("concepts", this.conceptService.getAll());
		modelMap.put("providers", this.providerService.getAll());
		modelMap.put("agreements", this.agreementService.getAll());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAll());
        modelMap.put("clients", this.clientService.getAllActives());
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
        modelMap.put("agreements", this.agreementService.getAll());
		modelMap.put("clients", this.clientService.getAll());
        return "searchSupplying";
    }

}

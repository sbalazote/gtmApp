package com.lsntsolutions.gtmApp.controllers.printing;

import java.util.List;

import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.service.AgreementService;
import com.lsntsolutions.gtmApp.service.AuditService;
import com.lsntsolutions.gtmApp.service.ClientService;
import com.lsntsolutions.gtmApp.service.ProvisioningRequestService;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.helper.PickingSheetPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PickingSheetController {

	@Autowired
	private ProvisioningRequestService provisioningRequestService;
	@Autowired
	private PickingSheetPrinter pickingSheetPrinter;
	@Autowired
	private AuditService auditService;
	@Autowired
	private AgreementService agreementService;
	@Autowired
	private ClientService clientService;

	@RequestMapping(value = "/pickingSheet", method = RequestMethod.GET)
	public String pickingSheet(ModelMap modelMap) throws Exception {
		modelMap.put("agreements", this.agreementService.getAllActives());
		modelMap.put("clients", this.clientService.getAllActives());
		return "pickingSheet";
	}

	@RequestMapping(value = "/printPickingSheets", method = RequestMethod.POST)
	@ResponseBody
	public void printPickingSheets(@RequestBody List<Integer> provisioningIds) throws Exception {
		this.pickingSheetPrinter.print(provisioningIds);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			for (Integer provisioningId : provisioningIds) {
				this.auditService.addAudit(auth.getName(), RoleOperation.PROVISIONING_REQUEST_PRINT.getId(), AuditState.COMFIRMED, provisioningId);
			}
		}
	}
}

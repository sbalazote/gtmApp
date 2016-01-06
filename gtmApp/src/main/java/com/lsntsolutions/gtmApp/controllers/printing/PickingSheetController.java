package com.lsntsolutions.gtmApp.controllers.printing;

import com.lsntsolutions.gtmApp.constant.AuditState;
import com.lsntsolutions.gtmApp.constant.RoleOperation;
import com.lsntsolutions.gtmApp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PickingSheetController {

	@Autowired
	private ProvisioningRequestService provisioningRequestService;
	@Autowired
	private ProvisioningRequestStateService provisioningRequestStateService;
	@Autowired
	private StockService stockService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private LogisticsOperatorService logisticsOperatorService;
	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/pickingSheet", method = RequestMethod.GET)
	public String pickingSheet(ModelMap modelMap) throws Exception {
		modelMap.put("agreements", this.orderService.getAgreementForOrderToPrint());
		modelMap.put("deliveryLocations", this.orderService.getDeliveryLocationsForOrderToPrint());
		modelMap.put("logisticsOperators", this.logisticsOperatorService.getAll());
		modelMap.put("clients", this.orderService.getClientForOrderToPrint());
		return "pickingSheet";
	}

	@RequestMapping(value = "/pickingSheets", method = RequestMethod.POST)
	public ModelAndView pickingSheets(HttpServletRequest request) throws Exception {
		String[] provisioningIds = request.getParameterValues("provisioningIds")[0].split(",");
		ModelAndView modelAndView = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			modelAndView = new ModelAndView("pickingSheets", "provisioningIds", provisioningIds);
			modelAndView.addObject("provisioningRequestService",this.provisioningRequestService);
			modelAndView.addObject("provisioningRequestStateService",this.provisioningRequestStateService);
			modelAndView.addObject("stockService",this.stockService);
			for (String provisioningId : provisioningIds) {
				this.auditService.addAudit(auth.getName(), RoleOperation.PROVISIONING_REQUEST_PRINT.getId(), AuditState.COMFIRMED, Integer.parseInt(provisioningId));
			}
		}
		return modelAndView;
	}
}
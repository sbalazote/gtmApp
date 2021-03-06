package com.lsntsolutions.gtmApp.controllers.search;

import com.lsntsolutions.gtmApp.dto.SearchAuditResultDTO;
import com.lsntsolutions.gtmApp.dto.SearchProductResultDTO;
import com.lsntsolutions.gtmApp.query.AuditQuery;
import com.lsntsolutions.gtmApp.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuditSearchController {

	@Autowired
	private AuditService auditService;

	@RequestMapping(value = "/getCountAuditSearch", method = RequestMethod.POST)
	public @ResponseBody
	boolean getCountAuditSearch(@RequestBody AuditQuery stockQuery) throws Exception {
		return this.auditService.getCountAuditSearch(stockQuery);
	}

	@RequestMapping(value = "/getAuditForSearch", method = RequestMethod.POST)
	public @ResponseBody
	SearchAuditResultDTO getAuditForSearch(@RequestBody AuditQuery auditQuery) throws Exception {
		return this.auditService.getAuditForSearch(auditQuery);
	}

	@RequestMapping(value = "/getSerializedProductAudit", method = RequestMethod.GET)
	public @ResponseBody
    SearchProductResultDTO getSerializedProductAudit(@RequestParam Integer productId, String serialNumber) throws Exception {
		return this.auditService.getAudit(productId, serialNumber, null, null);
	}

	@RequestMapping(value = "/getBatchExpirationDateProduct", method = RequestMethod.GET)
	public @ResponseBody
	SearchProductResultDTO getBatchExpirationDateProduct(@RequestParam Integer productId, String batch, String expirationDate) throws Exception {
		return this.auditService.getAudit(productId, null, batch, expirationDate.isEmpty() ? null : expirationDate);
	}

	@RequestMapping(value = "/audits", method = RequestMethod.POST)
	public ModelAndView audits(HttpServletRequest request) {
		AuditQuery auditQuery = this.getAuditQuery(request);
		return new ModelAndView("audits", "audits", this.auditService.getAuditForSearch(auditQuery));
	}

	private AuditQuery getAuditQuery(HttpServletRequest request) {
		Integer operationId = null;
		if (!(request.getParameterValues("operationId")[0]).equals("null")) {
			operationId = Integer.valueOf(request.getParameterValues("operationId")[0]);
		}
		Integer userId = null;
		if (!(request.getParameterValues("userId")[0]).equals("null")) {
			userId = Integer.valueOf(request.getParameterValues("userId")[0]);
		}
		Integer roleId = null;
		if (!(request.getParameterValues("roleId")[0]).equals("null")) {
			roleId = Integer.valueOf(request.getParameterValues("roleId")[0]);
		}
		Integer provisioningRequestId = null;
		if (!(request.getParameterValues("provisioningRequestId")[0]).equals("null")) {
			provisioningRequestId = Integer.valueOf(request.getParameterValues("provisioningRequestId")[0]);
		}
		AuditQuery auditQuery = AuditQuery.createFromParameters(request.getParameterValues("dateFrom")[0], request.getParameterValues("dateTo")[0], roleId,
				operationId, userId, request.getParameterValues("deliveryNoteNumber")[0], provisioningRequestId);

		return auditQuery;
	}

	@RequestMapping(value = "/batchExpirationDateProducts", method = RequestMethod.POST)
	public ModelAndView batchExpirationDateProducts(@RequestParam Integer productId, String batch, String expirationDate) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		SearchProductResultDTO searchProductResultDTO = this.auditService.getAudit(productId, null, batch, expirationDate.isEmpty() ? null : expirationDate);
		map.put("searchProductResultDTO", searchProductResultDTO);
		return new ModelAndView("productTrace", map);
	}

	@RequestMapping(value = "/serializedProducts", method = RequestMethod.POST)
	public ModelAndView serializedProducts(@RequestParam Integer productId, String serialNumber) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		SearchProductResultDTO searchProductResultDTO = this.auditService.getAudit(productId, serialNumber, null, null);
		map.put("searchProductResultDTO", searchProductResultDTO);
		return new ModelAndView("productTrace", map);
	}
}

package com.drogueria.controllers.search;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.drogueria.dto.AuditResultDTO;
import com.drogueria.model.Audit;
import com.drogueria.query.AuditQuery;
import com.drogueria.service.AuditService;

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
	List<Audit> getAuditForSearch(@RequestBody AuditQuery auditQuery) throws Exception {
		return this.auditService.getAuditForSearch(auditQuery);
	}

	@RequestMapping(value = "/getSerializedProductAudit", method = RequestMethod.GET)
	public @ResponseBody
	AuditResultDTO getSerializedProductAudit(@RequestParam Integer productId, String serialNumber) throws Exception {
		return this.auditService.getAudit(productId, serialNumber);
	}

	@RequestMapping(value = "/getBatchExpirateDateProduct", method = RequestMethod.GET)
	public @ResponseBody
	AuditResultDTO getBatchExpirateDateProduct(@RequestParam Integer productId, String batch, String expirateDate) throws Exception {
		return this.auditService.getAudit(productId, batch, expirateDate);
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
		Integer actionId = null;
		if (!(request.getParameterValues("actionId")[0]).equals("null")) {
			actionId = Integer.valueOf(request.getParameterValues("actionId")[0]);
		}
		Integer roleId = null;
		if (!(request.getParameterValues("roleId")[0]).equals("null")) {
			roleId = Integer.valueOf(request.getParameterValues("roleId")[0]);
		}
		AuditQuery auditQuery = AuditQuery.createFromParameters(request.getParameterValues("dateFrom")[0], request.getParameterValues("dateTo")[0], roleId,
				operationId, userId, actionId);

		return auditQuery;
	}
}

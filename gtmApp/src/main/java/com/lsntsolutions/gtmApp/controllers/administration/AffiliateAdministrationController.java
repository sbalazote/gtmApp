package com.lsntsolutions.gtmApp.controllers.administration;

import com.lsntsolutions.gtmApp.constant.DocumentType;
import com.lsntsolutions.gtmApp.dto.AffiliateDTO;
import com.lsntsolutions.gtmApp.model.Affiliate;
import com.lsntsolutions.gtmApp.model.Client;
import com.lsntsolutions.gtmApp.service.AffiliateService;
import com.lsntsolutions.gtmApp.service.ClientService;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AffiliateAdministrationController {

	@Autowired
	private ClientService clientService;

	@Autowired
	private AffiliateService affiliateService;

	@RequestMapping(value = "/affiliates", method = RequestMethod.POST)
	public ModelAndView affiliates(@RequestParam Map<String, String> parametersMap) {
		String searchPhrase = parametersMap.get("searchPhrase");
		String clientId = parametersMap.get("clientId");

		return new ModelAndView("affiliates", "affiliates", this.affiliateService.getForAutocomplete(searchPhrase, clientId.isEmpty() ? null : Integer.parseInt(clientId), null, null, null));
	}

	@RequestMapping(value = "/affiliateAdministration", method = RequestMethod.GET)
	public String affiliateAdministration(ModelMap modelMap) throws Exception {
		modelMap.put("clients", this.clientService.getAll());
		modelMap.put("documentTypes", DocumentType.types);
		return "affiliateAdministration";
	}

	@RequestMapping(value = "/saveAffiliate", method = RequestMethod.POST)
	public @ResponseBody
	Affiliate saveAffiliate(@RequestBody AffiliateDTO affiliateDTO) throws Exception {
		Affiliate affiliate = this.buildModel(affiliateDTO);
		this.affiliateService.save(affiliate);
		return affiliate;
	}

	@RequestMapping(value = "/addAffiliateToClient", method = RequestMethod.POST)
	public @ResponseBody
	Affiliate addAffiliateToClient(@RequestParam String code, @RequestParam Integer clientId) throws Exception {
		Affiliate affiliate = this.affiliateService.get(code);
		Client client = this.clientService.get(clientId);
		affiliate.getClients().add(client);
		this.affiliateService.save(affiliate);
		return affiliate;
	}


	private Affiliate buildModel(AffiliateDTO affiliateDTO) {
		Affiliate affiliate = new Affiliate();
		if (affiliateDTO.getId() != null) {
			affiliate.setId(affiliateDTO.getId());
		}

		affiliate.setCode(affiliateDTO.getCode());
		affiliate.setName(affiliateDTO.getName());
		affiliate.setSurname(affiliateDTO.getSurname());
		affiliate.setDocumentType(affiliateDTO.getDocumentType());
		affiliate.setDocument(affiliateDTO.getDocument());
		affiliate.setActive(affiliateDTO.isActive());

		List<Integer> clientsId = affiliateDTO.getClients();
		List<Client> clients = new ArrayList<>();
		for (Integer clientId : clientsId) {
			clients.add(this.clientService.get(clientId));
		}
		affiliate.setClients(clients);

		return affiliate;
	}

	@RequestMapping(value = "/readAffiliate", method = RequestMethod.GET)
	public @ResponseBody Affiliate readAffiliate(@RequestParam Integer affiliateId) throws Exception {
		return this.affiliateService.get(affiliateId);
	}

	@RequestMapping(value = "/deleteAffiliate", method = RequestMethod.POST)
	public @ResponseBody boolean deleteAffiliate(@RequestParam Integer affiliateId) throws Exception {
		return this.affiliateService.delete(affiliateId);
	}

	@RequestMapping(value = "/existsAffiliate", method = RequestMethod.GET)
	public @ResponseBody Boolean exists(@RequestParam String code) throws Exception {
		return this.affiliateService.exists(code);
	}

	@RequestMapping(value = "/getMatchedAffiliates", method = RequestMethod.POST)
	public @ResponseBody String getMatchedAffiliates(@RequestParam Map<String, String> parametersMap) throws JSONException {

		String searchPhrase = parametersMap.get("searchPhrase");
		Integer current = Integer.parseInt(parametersMap.get("current"));
		Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));
		String clientId = parametersMap.get("clientId");

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		String sortId = parametersMap.get("sort[id]");
		String sortCode = parametersMap.get("sort[code]");
		String sortName = parametersMap.get("sort[name]");
		String sortSurname = parametersMap.get("sort[surname]");
		String sortDocumentType = parametersMap.get("sort[documentType]");
		String sortDocument = parametersMap.get("sort[document]");
		String sortActive = parametersMap.get("sort[active]");

		Integer client = (clientId != null && clientId != "") ? Integer.valueOf(clientId) : null;
		List<Affiliate> listAffiliates = this.affiliateService.getForAutocomplete(searchPhrase, null, client, sortId, sortCode, sortName, sortSurname, sortDocumentType, sortDocument, sortActive);
		total = listAffiliates.size();
		if (total < start + length) {
			listAffiliates = listAffiliates.subList(start, (int) total);
		} else {
			if(length > 0) {
				listAffiliates = listAffiliates.subList(start, start + length);
			}
		}

		for (Affiliate affiliate : listAffiliates) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", affiliate.getId());
			dataJson.put("code", affiliate.getCode());
			dataJson.put("name", affiliate.getName());
			dataJson.put("surname", affiliate.getSurname());
			if(affiliate.getDocumentType() != null){
				dataJson.put("documentType", DocumentType.types.get(Integer.valueOf(affiliate.getDocumentType())));
			}else{
				dataJson.put("documentType", "No Informa");
			}

			dataJson.put("document", affiliate.getDocument());
			dataJson.put("isActive", affiliate.isActive() ? "Si" : "No");
			jsonArray.put(dataJson);
		}

		JSONObject responseJson = new JSONObject();
		responseJson.put("current", current);
		responseJson.put("rowCount", (total < (start + length)) ? (total - length) : length);
		responseJson.put("rows", jsonArray);
		responseJson.put("total", total);

		return responseJson.toString();
	}
}

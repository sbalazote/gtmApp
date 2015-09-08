package com.drogueria.controllers.administration;

import java.util.List;
import java.util.Map;

import com.drogueria.constant.DocumentType;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.drogueria.dto.AffiliateDTO;
import com.drogueria.model.Affiliate;
import com.drogueria.service.AffiliateService;
import com.drogueria.service.ClientService;

@Controller
public class AffiliateAdministrationController {

	@Autowired
	private ClientService clientService;

	@Autowired
	private AffiliateService affiliateService;

	@RequestMapping(value = "/affiliates", method = RequestMethod.POST)
	public ModelAndView affiliates() {
		return new ModelAndView("affiliates", "affiliates", this.affiliateService.getAll());
	}

	@RequestMapping(value = "/affiliateAdministration", method = RequestMethod.GET)
	public String affiliateAdministration(ModelMap modelMap) throws Exception {
		modelMap.put("clients", this.clientService.getAll());
		modelMap.put("documentTypes", DocumentType.types);
		return "affiliateAdministration";
	}

	@RequestMapping(value = "/saveAffiliate", method = RequestMethod.POST)
	public @ResponseBody Affiliate saveAffiliate(@RequestBody AffiliateDTO affiliateDTO) throws Exception {
		Affiliate affiliate = this.buildModel(affiliateDTO);
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
		affiliate.setClient(this.clientService.get(affiliateDTO.getClientId()));
		affiliate.setActive(affiliateDTO.isActive());

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

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		List<Affiliate> listAffiliates;
		if (searchPhrase.matches("")) {
			listAffiliates = this.affiliateService.getPaginated(start, length);
			total = this.affiliateService.getTotalNumber();
		} else {
			listAffiliates = this.affiliateService.getForAutocomplete(searchPhrase, null);
			total = listAffiliates.size();
			if (total < start + length) {
				listAffiliates = listAffiliates.subList(start, (int) total);
			} else {
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
			dataJson.put("client", affiliate.getClient().getCorporateName());
			dataJson.put("isActive", affiliate.isActive() == true ? "Si" : "No");
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

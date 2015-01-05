package com.drogueria.controllers.administration;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.drogueria.dto.AgreementDTO;
import com.drogueria.dto.AgreementTransferDTO;
import com.drogueria.model.Agreement;
import com.drogueria.service.AgreementService;
import com.drogueria.service.AgreementTransferService;

@Controller
public class AgreementAdministrationController {

	@Autowired
	private AgreementService agreementService;
	@Autowired
	private AgreementTransferService agreementTransferService;

	@RequestMapping(value = "/agreements", method = RequestMethod.POST)
	public ModelAndView agreements() {
		return new ModelAndView("agreements", "agreements", this.agreementService.getAll());
	}

	@RequestMapping(value = "/agreementAdministration", method = RequestMethod.GET)
	public String agreementAdministration(ModelMap modelMap) throws Exception {
		return "agreementAdministration";
	}

	@RequestMapping(value = "/saveAgreement", method = RequestMethod.POST)
	public @ResponseBody
	Agreement saveAgreement(@RequestBody AgreementDTO agreementDTO) throws Exception {
		Agreement agreement = this.buildModel(agreementDTO);
		this.agreementService.save(agreement);
		return agreement;
	}

	private Agreement buildModel(AgreementDTO agreementDTO) {
		Agreement agreement = new Agreement();
		if (agreementDTO.getId() != null) {
			agreement.setId(agreementDTO.getId());
		}
		agreement.setCode(agreementDTO.getCode());

		agreement.setDescription(agreementDTO.getDescription());
		agreement.setActive(agreementDTO.isActive());
		return agreement;
	}

	@RequestMapping(value = "/readAgreement", method = RequestMethod.GET)
	public @ResponseBody
	Agreement readAgreement(@RequestParam Integer agreementId) throws Exception {
		return this.agreementService.get(agreementId);
	}

	@RequestMapping(value = "/deleteAgreement", method = RequestMethod.POST)
	public @ResponseBody
	boolean deleteAgreement(@RequestParam Integer agreementId) throws Exception {
		return this.agreementService.delete(agreementId);
	}

	@RequestMapping(value = "/existsAgreement", method = RequestMethod.GET)
	public @ResponseBody
	Boolean existsAgreement(@RequestParam Integer code) throws Exception {
		return this.agreementService.exists(code);
	}

	@RequestMapping(value = "/getMatchedAgreements", method = RequestMethod.POST)
	public @ResponseBody
	String getMatchedAgreements(@RequestParam Map<String, String> parametersMap) throws JSONException {

		String searchPhrase = parametersMap.get("searchPhrase");
		Integer current = Integer.parseInt(parametersMap.get("current"));
		Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		List<Agreement> listAgreements = null;
		if (searchPhrase.matches("")) {
			listAgreements = this.agreementService.getPaginated(start, length);
			total = this.agreementService.getTotalNumber();
		} else {
			listAgreements = this.agreementService.getForAutocomplete(searchPhrase, null);
			total = listAgreements.size();
			if (total < start + length) {
				listAgreements = listAgreements.subList(start, (int) total);
			} else {
				listAgreements = listAgreements.subList(start, start + length);
			}
		}

		for (Agreement agreement : listAgreements) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", agreement.getId());
			dataJson.put("code", agreement.getCode());
			dataJson.put("description", agreement.getDescription());
			dataJson.put("isActive", agreement.isActive() == true ? "Si" : "No");
			jsonArray.put(dataJson);
		}

		JSONObject responseJson = new JSONObject();
		responseJson.put("current", current);
		responseJson.put("rowCount", (total < (start + length)) ? (total - length) : length);
		responseJson.put("rows", jsonArray);
		responseJson.put("total", total);

		return responseJson.toString();
	}

	@RequestMapping(value = "/agreementTransfer", method = RequestMethod.GET)
	public String agreementTransfer(ModelMap modelMap) throws Exception {
		modelMap.put("agreements", this.agreementService.getAll());
		return "agreementTransfer";
	}

	@RequestMapping(value = "/updateProductsAgreement", method = RequestMethod.POST)
	public @ResponseBody
	void saveInput(@RequestBody AgreementTransferDTO agreementTransferDTO, HttpServletRequest request) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			this.agreementTransferService.updateAgreementStock(agreementTransferDTO, auth.getName());
		}
	}

}

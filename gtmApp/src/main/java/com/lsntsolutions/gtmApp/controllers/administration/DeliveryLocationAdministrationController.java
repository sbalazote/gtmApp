package com.lsntsolutions.gtmApp.controllers.administration;

import com.lsntsolutions.gtmApp.dto.DeliveryLocationDTO;
import com.lsntsolutions.gtmApp.model.DeliveryLocation;
import com.lsntsolutions.gtmApp.model.Province;
import com.lsntsolutions.gtmApp.service.AgentService;
import com.lsntsolutions.gtmApp.service.DeliveryLocationService;
import com.lsntsolutions.gtmApp.service.ProvinceService;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class DeliveryLocationAdministrationController {

	@Autowired
	private DeliveryLocationService deliveryLocationService;

	@Autowired
	private ProvinceService provinceService;

	@Autowired
	private AgentService agentService;

	@Autowired
	private com.lsntsolutions.gtmApp.service.VATLiabilityService VATLiabilityService;

	@RequestMapping(value = "/deliveryLocations", method = RequestMethod.POST)
	public ModelAndView deliveryLocations(@RequestParam Map<String, String> parametersMap) {
		String searchPhrase = parametersMap.get("searchPhrase");
		if (searchPhrase.matches("")) {
			return new ModelAndView("deliveryLocations", "deliveryLocations", this.deliveryLocationService.getAll());
		} else {
			return new ModelAndView("deliveryLocations", "deliveryLocations", this.deliveryLocationService.getForAutocomplete(searchPhrase, null));
		}
	}

	@RequestMapping(value = "/deliveryLocationAdministration", method = RequestMethod.GET)
	public String deliveryLocationAdministration(ModelMap modelMap) throws Exception {
		modelMap.put("provinces", this.provinceService.getAll());
		modelMap.put("agents", this.agentService.getAll());
		modelMap.put("VATLiabilities", this.VATLiabilityService.getAll());
		return "deliveryLocationAdministration";
	}

	@RequestMapping(value = "/saveDeliveryLocation", method = RequestMethod.POST)
	public @ResponseBody DeliveryLocation saveDeliveryLocation(@RequestBody DeliveryLocationDTO deliveryLocationDTO) throws Exception {
		DeliveryLocation deliveryLocation = this.buildModel(deliveryLocationDTO);
		this.deliveryLocationService.save(deliveryLocation);
		return deliveryLocation;
	}

	private DeliveryLocation buildModel(DeliveryLocationDTO deliveryLocationDTO) {
		DeliveryLocation deliveryLocation = new DeliveryLocation();
		if (deliveryLocationDTO.getId() != null) {
			deliveryLocation.setId(deliveryLocationDTO.getId());
		}
		deliveryLocation.setCode(deliveryLocationDTO.getCode());
		deliveryLocation.setName(deliveryLocationDTO.getName());
		deliveryLocation.setTaxId(deliveryLocationDTO.getTaxId());
		deliveryLocation.setCorporateName(deliveryLocationDTO.getCorporateName());
		Province province = this.provinceService.get(deliveryLocationDTO.getProvinceId());
		deliveryLocation.setProvince(province);
		deliveryLocation.setLocality(deliveryLocationDTO.getLocality());
		deliveryLocation.setAddress(deliveryLocationDTO.getAddress());
		deliveryLocation.setZipCode(deliveryLocationDTO.getZipCode());
		deliveryLocation.setVATLiability(this.VATLiabilityService.get(deliveryLocationDTO.getVATLiabilityId()));
		deliveryLocation.setPhone(deliveryLocationDTO.getPhone());
		deliveryLocation.setMail(deliveryLocationDTO.getMail());
		deliveryLocation.setGln(deliveryLocationDTO.getGln());
		deliveryLocation.setActive(deliveryLocationDTO.isActive());
		deliveryLocation.setAgent(this.agentService.get(deliveryLocationDTO.getAgentId()));

		return deliveryLocation;
	}

	@RequestMapping(value = "/readDeliveryLocation", method = RequestMethod.GET)
	public @ResponseBody DeliveryLocation readDeliveryLocation(@RequestParam Integer deliveryLocationId) throws Exception {
		return this.deliveryLocationService.get(deliveryLocationId);
	}

	@RequestMapping(value = "/deleteDeliveryLocation", method = RequestMethod.POST)
	public @ResponseBody boolean deleteDeliveryLocation(@RequestParam Integer deliveryLocationId) throws Exception {
		return this.deliveryLocationService.delete(deliveryLocationId);
	}

	@RequestMapping(value = "/existsDeliveryLocation", method = RequestMethod.GET)
	public @ResponseBody Boolean existsDeliveryLocation(@RequestParam Integer code) throws Exception {
		return this.deliveryLocationService.exists(code);
	}

	@RequestMapping(value = "/getMatchedDeliveryLocations", method = RequestMethod.POST)
	public @ResponseBody String getMatchedDeliveryLocations(@RequestParam Map<String, String> parametersMap) throws JSONException {

		String searchPhrase = parametersMap.get("searchPhrase");
		Integer current = Integer.parseInt(parametersMap.get("current"));
		Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		List<DeliveryLocation> listDeliveryLocations;
		if (searchPhrase.matches("")) {
			listDeliveryLocations = this.deliveryLocationService.getPaginated(start, length);
			total = this.deliveryLocationService.getTotalNumber();
		} else {
			listDeliveryLocations = this.deliveryLocationService.getForAutocomplete(searchPhrase, null);
			total = listDeliveryLocations.size();
			if (total < start + length) {
				listDeliveryLocations = listDeliveryLocations.subList(start, (int) total);
			} else {
				listDeliveryLocations = listDeliveryLocations.subList(start, start + length);
			}
		}

		for (DeliveryLocation deliveryLocation : listDeliveryLocations) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", deliveryLocation.getId());
			dataJson.put("code", deliveryLocation.getCode());
			dataJson.put("name", deliveryLocation.getName());
			dataJson.put("taxId", deliveryLocation.getTaxId());
			dataJson.put("corporateName", deliveryLocation.getCorporateName());
			dataJson.put("province", deliveryLocation.getProvince().getName());
			dataJson.put("locality", deliveryLocation.getLocality());
			dataJson.put("address", deliveryLocation.getAddress());
			dataJson.put("zipCode", deliveryLocation.getZipCode());
			dataJson.put("vatLiability", deliveryLocation.getVATLiability().getAcronym());
			dataJson.put("phone", deliveryLocation.getPhone());
			dataJson.put("mail", deliveryLocation.getMail());
			dataJson.put("gln", deliveryLocation.getGln());
			dataJson.put("agent", deliveryLocation.getAgent().getDescription());
			dataJson.put("isActive", deliveryLocation.isActive() == true ? "Si" : "No");
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
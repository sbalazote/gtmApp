package com.lsntsolutions.gtmApp.controllers.administration;

import java.util.List;
import java.util.Map;

import com.lsntsolutions.gtmApp.service.DeliveryNoteEnumeratorService;
import com.lsntsolutions.gtmApp.dto.DeliveryNoteEnumeratorDTO;
import com.lsntsolutions.gtmApp.model.DeliveryNoteEnumerator;
import com.lsntsolutions.gtmApp.util.StringUtility;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DeliveryNoteEnumeratorController {

	@Autowired
	private DeliveryNoteEnumeratorService deliveryNoteEnumeratorService;

	@RequestMapping(value = "/deliveryNoteEnumeratorAdministration", method = RequestMethod.GET)
	public String deliveryNoteEnumeratorAdministration() throws Exception {
		return "deliveryNoteEnumeratorAdministration";
	}

	@RequestMapping(value = "/getDeliveryNoteEnumerators", method = RequestMethod.GET)
	public @ResponseBody
	List<DeliveryNoteEnumerator> getDeliveryNoteEnumerators(@RequestParam boolean fake) throws Exception {
		return this.deliveryNoteEnumeratorService.getReals(fake);
	}

	@RequestMapping(value = "/saveDeliveryNoteEnumerator", method = RequestMethod.POST)
	public @ResponseBody
	DeliveryNoteEnumerator saveDeliveryNoteEnumerator(@RequestBody DeliveryNoteEnumeratorDTO deliveryNoteEnumeratorDTO) throws Exception {
		DeliveryNoteEnumerator deliveryNoteEnumerator = this.buildModel(deliveryNoteEnumeratorDTO);
		this.deliveryNoteEnumeratorService.save(deliveryNoteEnumerator);
		return deliveryNoteEnumerator;
	}

	private DeliveryNoteEnumerator buildModel(DeliveryNoteEnumeratorDTO deliveryNoteEnumeratorDTO) {
		DeliveryNoteEnumerator deliveryNoteEnumerator = new DeliveryNoteEnumerator();
		if (deliveryNoteEnumeratorDTO.getId() != null) {
			deliveryNoteEnumerator.setId(deliveryNoteEnumeratorDTO.getId());
		}
		deliveryNoteEnumerator.setLastDeliveryNoteNumber(deliveryNoteEnumeratorDTO.getLastDeliveryNoteNumber());
		deliveryNoteEnumerator.setDeliveryNotePOS(deliveryNoteEnumeratorDTO.getDeliveryNotePOS());
		deliveryNoteEnumerator.setFake(deliveryNoteEnumeratorDTO.isFake());
		deliveryNoteEnumerator.setActive(deliveryNoteEnumeratorDTO.isActive());
		return deliveryNoteEnumerator;
	}

	@RequestMapping(value = "/readDeliveryNoteEnumerator", method = RequestMethod.GET)
	public @ResponseBody
	DeliveryNoteEnumerator readDeliveryNoteEnumerator(@RequestParam Integer deleteDeliveryNoteEnumeratorId) throws Exception {
		return this.deliveryNoteEnumeratorService.get(deleteDeliveryNoteEnumeratorId);
	}

	@RequestMapping(value = "/deleteDeliveryNoteEnumerator", method = RequestMethod.POST)
	public @ResponseBody
	boolean deleteDeliveryNoteEnumerator(@RequestParam Integer deleteDeliveryNoteEnumeratorId) throws Exception {
		return this.deliveryNoteEnumeratorService.delete(deleteDeliveryNoteEnumeratorId);
	}

	@RequestMapping(value = "/existsDeliveryNoteEnumerator", method = RequestMethod.GET)
	public @ResponseBody
	Boolean existsDeliveryNoteEnumerator(@RequestParam Integer deliveryNotePOS, Boolean fake) throws Exception {
		return this.deliveryNoteEnumeratorService.exists(deliveryNotePOS, fake);
	}

	@RequestMapping(value = "/checkNewDeliveryNoteNumber", method = RequestMethod.GET)
	public @ResponseBody
	Boolean checkNewDeliveryNoteNumber(@RequestParam Integer deliveryNotePOS, Integer lastDeliveryNoteNumberInput) throws Exception {
		return this.deliveryNoteEnumeratorService.checkNewDeliveryNoteNumber(deliveryNotePOS, lastDeliveryNoteNumberInput);
	}

	@RequestMapping(value = "/getMatchedDeliveryNoteEnumerators", method = RequestMethod.POST)
	public @ResponseBody
	String getMatchedDeliveryNoteEnumerators(@RequestParam Map<String, String> parametersMap) throws JSONException {

		String searchPhrase = parametersMap.get("searchPhrase");
		Integer current = Integer.parseInt(parametersMap.get("current"));
		Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		List<DeliveryNoteEnumerator> deliveryNoteEnumerators;
		if (searchPhrase.matches("")) {
			deliveryNoteEnumerators = this.deliveryNoteEnumeratorService.getPaginated(start, length);
			total = this.deliveryNoteEnumeratorService.getTotalNumber();
		} else {
			deliveryNoteEnumerators = this.deliveryNoteEnumeratorService.getForAutocomplete(searchPhrase, null, null);
			total = deliveryNoteEnumerators.size();
			if (total < start + length) {
				deliveryNoteEnumerators = deliveryNoteEnumerators.subList(start, (int) total);
			} else {
				deliveryNoteEnumerators = deliveryNoteEnumerators.subList(start, start + length);
			}
		}

		for (DeliveryNoteEnumerator enumerator : deliveryNoteEnumerators) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", enumerator.getId());
			String deliveryNoteEnumerator = StringUtility.addLeadingZeros(enumerator.getDeliveryNotePOS(), 4);
			dataJson.put("deliveryNotePOS", deliveryNoteEnumerator);
			String lastDeliveryNoteNumber = StringUtility.addLeadingZeros(enumerator.getLastDeliveryNoteNumber(), 8);
			dataJson.put("lastDeliveryNoteNumber",lastDeliveryNoteNumber);
			dataJson.put("isActive", enumerator.isActive() == true ? "Si" : "No");
			dataJson.put("isFake", enumerator.isFake() == true ? "Si" : "No");
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

package com.lsntsolutions.gtmApp.controllers.administration;

import com.lsntsolutions.gtmApp.dto.ClientDTO;
import com.lsntsolutions.gtmApp.dto.DeliveryLocationDTO;
import com.lsntsolutions.gtmApp.model.Client;
import com.lsntsolutions.gtmApp.model.DeliveryLocation;
import com.lsntsolutions.gtmApp.model.Province;
import com.lsntsolutions.gtmApp.service.ClientService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ClientAdministrationController {

	@Autowired
	private ClientService clientService;

	@Autowired
	private ProvinceService provinceService;

	@Autowired
	private DeliveryLocationService deliveryLocationService;

	@Autowired
	private com.lsntsolutions.gtmApp.service.VATLiabilityService VATLiabilityService;

	@RequestMapping(value = "/clients", method = RequestMethod.POST)
	public ModelAndView clients(@RequestParam Map<String, String> parametersMap) {
		String searchPhrase = parametersMap.get("searchPhrase");
		if (searchPhrase.matches("")) {
			return new ModelAndView("clients", "clients", this.clientService.getAll());
		} else {
			return new ModelAndView("clients", "clients", this.clientService.getForAutocomplete(searchPhrase, null, null, null, null, null, null, null));
		}
	}

	@RequestMapping(value = "/saveClient", method = RequestMethod.POST)
	public @ResponseBody
	Client saveClient(@RequestBody ClientDTO clientDTO) throws Exception {
		Client client = this.buildModel(clientDTO);
		this.clientService.save(client);
		return client;
	}

	@RequestMapping(value = "/clientAdministration", method = RequestMethod.GET)
	public String clientAdministration(ModelMap modelMap) throws Exception {
		modelMap.put("provinces", this.provinceService.getAll());
		modelMap.put("deliveryLocations", this.deliveryLocationService.getAllActives());
		modelMap.put("VATLiabilities", this.VATLiabilityService.getAll());
		return "clientAdministration";
	}

	private Client buildModel(ClientDTO clientDTO) {
		Client client = new Client();
		if (clientDTO.getId() != null) {
			client.setId(clientDTO.getId());
		}
		client.setCode(clientDTO.getCode());
		client.setAddress(clientDTO.getAddress());
		client.setCorporateName(clientDTO.getCorporateName());
		client.setLocality(clientDTO.getLocality());
		client.setName(clientDTO.getName());

		Province province = this.provinceService.get(clientDTO.getProvinceId());
		client.setProvince(province);

		client.setVATLiability(this.VATLiabilityService.get(clientDTO.getVATLiabilityId()));
		client.setTaxId(clientDTO.getTaxId());
		client.setZipCode(clientDTO.getZipCode());
		client.setPhone(clientDTO.getPhone());
		client.setActive(clientDTO.isActive());
		client.setMedicalInsuranceCode(clientDTO.getMedicalInsuranceCode());

		List<Integer> deliveryLocationsId = clientDTO.getDeliveryLocations();

		List<DeliveryLocation> deliveryLocations = new ArrayList<DeliveryLocation>();
		for (Integer deliveryLocationId : deliveryLocationsId) {
			deliveryLocations.add(this.deliveryLocationService.get(deliveryLocationId));
		}
		client.setDeliveryLocations(deliveryLocations);

		return client;
	}

	@RequestMapping(value = "/readClient", method = RequestMethod.GET)
	public @ResponseBody
	Client readClient(ModelMap modelMap, @RequestParam Integer clientId) throws Exception {
		Client client = this.clientService.get(clientId);

		modelMap.put("deliveryLocations", this.getSelectedDeliveryLocations(client));

		return client;
	}

	private List<DeliveryLocationDTO> getSelectedDeliveryLocations(Client client) {
		List<DeliveryLocation> selectedDeliveryLocations = client.getDeliveryLocations();
		List<DeliveryLocation> allDeliveryLocations = this.deliveryLocationService.getAll();

		List<DeliveryLocationDTO> deliveryLocationsDTO = new ArrayList<DeliveryLocationDTO>();

		for (DeliveryLocation deliveryLocation : allDeliveryLocations) {
			if (selectedDeliveryLocations.contains(deliveryLocation)) {
				deliveryLocationsDTO.add(this.newDeliveryLocationDTO(deliveryLocation, true));
			} else {
				deliveryLocationsDTO.add(this.newDeliveryLocationDTO(deliveryLocation, false));
			}
		}
		return deliveryLocationsDTO;
	}

	private DeliveryLocationDTO newDeliveryLocationDTO(DeliveryLocation deliveryLocation, boolean selected) {
		DeliveryLocationDTO deliveryLocationDTO = new DeliveryLocationDTO();

		deliveryLocationDTO.setId(deliveryLocation.getId());
		deliveryLocationDTO.setCode(deliveryLocation.getCode());
		deliveryLocationDTO.setName(deliveryLocation.getName());
		deliveryLocationDTO.setTaxId(deliveryLocation.getTaxId());
		deliveryLocationDTO.setCorporateName(deliveryLocation.getCorporateName());
		deliveryLocationDTO.setProvinceId(deliveryLocation.getProvince().getId());
		deliveryLocationDTO.setVATLiabilityId(deliveryLocation.getVATLiability().getId());
		deliveryLocationDTO.setLocality(deliveryLocation.getLocality());
		deliveryLocationDTO.setAddress(deliveryLocation.getAddress());
		deliveryLocationDTO.setZipCode(deliveryLocation.getZipCode());
		deliveryLocationDTO.setPhone(deliveryLocation.getPhone());
		deliveryLocationDTO.setMail(deliveryLocation.getMail());
		deliveryLocationDTO.setGln(deliveryLocation.getGln());
		deliveryLocationDTO.setAgentId(deliveryLocation.getAgent().getId());
		deliveryLocationDTO.setSelected(selected);

		return deliveryLocationDTO;
	}

	@RequestMapping(value = "/deleteClient", method = RequestMethod.POST)
	public @ResponseBody
	boolean deleteClient(@RequestParam Integer clientId) throws Exception {
		return this.clientService.delete(clientId);
	}

	@RequestMapping(value = "/existsClient", method = RequestMethod.GET)
	public @ResponseBody
	Boolean existsClient(@RequestParam Integer code) throws Exception {
		return this.clientService.exists(code);
	}

	@RequestMapping(value = "/getDeliveryLocationsByClient", method = RequestMethod.GET)
	public @ResponseBody
	List<DeliveryLocation> getDeliveryLocationsByClient(@RequestParam Integer clientId) throws Exception {
		Client client = this.clientService.get(clientId);
		List<DeliveryLocation> deliveryLocations = new ArrayList<>();
		if(client != null) {
			deliveryLocations = this.clientService.getDeliveriesLocations(clientId);
			//deliveryLocations = this.clientService.get(clientId).getDeliveryLocations();
		}
		return deliveryLocations;
	}

	@RequestMapping(value = "/getMatchedClients", method = RequestMethod.POST)
	public @ResponseBody
	String getMatchedClients(@RequestParam Map<String, String> parametersMap) throws JSONException {

		String searchPhrase = parametersMap.get("searchPhrase");
		Integer current = Integer.parseInt(parametersMap.get("current"));
		Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		String sortId = parametersMap.get("sort[id]");
		String sortCode = parametersMap.get("sort[code]");
		String sortName = parametersMap.get("sort[name]");
		String sortTaxId = parametersMap.get("sort[taxId]");
		String sortProvince = parametersMap.get("sort[province]");
		String sortIsActive = parametersMap.get("sort[isActive]");

		List<Client> listClients = this.clientService.getForAutocomplete(searchPhrase, null, sortId, sortCode, sortName, sortTaxId, sortProvince, sortIsActive);
		total = listClients.size();
		if (total < start + length) {
			listClients = listClients.subList(start, (int) total);
		} else {
			if(length > 0) {
				listClients = listClients.subList(start, start + length);
			}
		}

		for (Client client : listClients) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", client.getId());
			dataJson.put("code", client.getCode());
			dataJson.put("name", client.getName());
			dataJson.put("taxId", client.getTaxId());
			dataJson.put("corporateName", client.getCorporateName());
			dataJson.put("province", client.getProvince().getName());
			dataJson.put("locality", client.getLocality());
			dataJson.put("address", client.getAddress());
			dataJson.put("zipCode", client.getZipCode());
			dataJson.put("vatLiability", client.getVATLiability().getAcronym());
			dataJson.put("phone", client.getPhone());
			dataJson.put("isActive", client.isActive() ? "Si" : "No");
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
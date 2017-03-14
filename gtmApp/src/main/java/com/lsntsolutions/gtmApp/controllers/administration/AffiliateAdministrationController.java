package com.lsntsolutions.gtmApp.controllers.administration;

import com.lsntsolutions.gtmApp.constant.DocumentType;
import com.lsntsolutions.gtmApp.dto.AffiliateDTO;
import com.lsntsolutions.gtmApp.model.Affiliate;
import com.lsntsolutions.gtmApp.model.Client;
import com.lsntsolutions.gtmApp.model.ClientAffiliate;
import com.lsntsolutions.gtmApp.service.AffiliateService;
import com.lsntsolutions.gtmApp.service.ClientAffiliateService;
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

	@Autowired
	private ClientAffiliateService clientAffiliateService;

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

	@RequestMapping(value = "/saveClientAffiliate", method = RequestMethod.POST)
	public @ResponseBody
	ClientAffiliate saveClientAffiliate(@RequestParam Integer affiliateId, @RequestParam Integer clientId, @RequestParam String associateNumber) throws Exception {
		Affiliate affiliate = this.affiliateService.get(affiliateId);
		Client client = this.clientService.get(clientId);
		ClientAffiliate clientAffiliate = new ClientAffiliate();
		clientAffiliate.setAffiliate(affiliate);
		clientAffiliate.setClient(client);
		clientAffiliate.setAssociateNumber(associateNumber);
		this.clientAffiliateService.save(clientAffiliate);
		return clientAffiliate;
	}

	@RequestMapping(value = "/deleteClientAffiliate", method = RequestMethod.POST)
	public @ResponseBody boolean deleteClientAffiliate(@RequestParam Integer clientAffiliateId) throws Exception {
		return this.clientAffiliateService.delete(clientAffiliateId);
	}

	@RequestMapping(value = "/addAffiliateToClient", method = RequestMethod.POST)
	public @ResponseBody
	Affiliate addAffiliateToClient(@RequestParam String code, @RequestParam Integer clientId, @RequestParam String associateNumber) throws Exception {
		boolean clientFound = false;
		Affiliate affiliate = this.affiliateService.getByCode(code);
		Client client = this.clientService.get(clientId);
		List<ClientAffiliate> affiliateClients = affiliate.getClientAffiliates();
		for (ClientAffiliate affiliateClient : affiliateClients) {
			if (clientId.equals(affiliateClient.getClient().getId()))
				clientFound = true;
		}
		ClientAffiliate clientAffiliate = new ClientAffiliate();
		clientAffiliate.setAffiliate(affiliate);
		clientAffiliate.setClient(client);
		clientAffiliate.setAssociateNumber(associateNumber);
		if (!clientFound)
			affiliateClients.add(clientAffiliate);
		this.clientAffiliateService.save(clientAffiliate);
		return affiliate;
	}

	@RequestMapping(value = "/saveAffiliateAndClient", method = RequestMethod.POST)
	public @ResponseBody
	Affiliate saveAffiliateAndClient(@RequestBody AffiliateDTO affiliate) throws Exception {
		Affiliate newAffiliate = this.buildModel(affiliate);
		this.affiliateService.save(newAffiliate);
		Client client = this.clientService.get(affiliate.getClientId());

		ClientAffiliate clientAffiliate = new ClientAffiliate();
		clientAffiliate.setAffiliate(newAffiliate);
		clientAffiliate.setClient(client);
		clientAffiliate.setAssociateNumber(affiliate.getAssociateNumber());
		this.clientAffiliateService.save(clientAffiliate);
		return newAffiliate;
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
		affiliate.setSex(affiliateDTO.getSex());
		affiliate.setAddress(affiliateDTO.getAddress());
		affiliate.setLocality(affiliateDTO.getLocality());
		affiliate.setNumber(affiliateDTO.getNumber());
		affiliate.setFloor(affiliateDTO.getFloor());
		affiliate.setApartment(affiliateDTO.getApartment());
		affiliate.setZipCode(affiliateDTO.getZipCode());
		affiliate.setPhone(affiliateDTO.getPhone());

		return affiliate;
	}

	@RequestMapping(value = "/readAffiliate", method = RequestMethod.GET)
	public @ResponseBody Affiliate readAffiliate(@RequestParam Integer affiliateId) throws Exception {
		return this.affiliateService.get(affiliateId);
	}

	@RequestMapping(value = "/getClientAffiliates", method = RequestMethod.POST)
	public @ResponseBody String getClientAffiliates(@RequestParam Map<String, String> parametersMap) throws Exception {
		String id = parametersMap.get("affiliateId");
		Affiliate affiliate = this.affiliateService.get(Integer.valueOf(id));
		List<ClientAffiliate> clientAffiliates = affiliate.getClientAffiliates();
		Integer current = Integer.parseInt(parametersMap.get("current"));
		Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total = clientAffiliates.size();
		if (total < start + length) {
			clientAffiliates = clientAffiliates.subList(start, (int) total);
		} else {
			if(length > 0) {
				clientAffiliates = clientAffiliates.subList(start, start + length);
			}
		}

		for (ClientAffiliate clientAffiliate : clientAffiliates) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", clientAffiliate.getId());
			dataJson.put("code", clientAffiliate.getClient().getCode());
			dataJson.put("name", clientAffiliate.getClient().getName());
			dataJson.put("associateNumber", clientAffiliate.getAssociateNumber());
			jsonArray.put(dataJson);
		}

		JSONObject responseJson = new JSONObject();
		responseJson.put("current", current);
		responseJson.put("rowCount", (total < (start + length)) ? (total - length) : length);
		responseJson.put("rows", jsonArray);
		responseJson.put("total", total);
		return responseJson.toString();
	}

	@RequestMapping(value = "/getClientToAssociate", method = RequestMethod.GET)
	public @ResponseBody List<Client> getClientToAssociate(@RequestParam Integer affiliateId) throws Exception {
		List<Client> clients = this.clientService.getAll();
		List<Client> clientsToReturn = new ArrayList<>();
		if(affiliateId != null) {
			Affiliate affiliate = this.affiliateService.get(affiliateId);
			List<ClientAffiliate> clientAffiliates = affiliate.getClientAffiliates();
			for(Client client : clients){
				boolean found = false;
				for(ClientAffiliate clientAffiliate : clientAffiliates){
					if(clientAffiliate.getClient().getId() == client.getId()){
						found = true;
					}
				}
				if(!found){
					clientsToReturn.add(client);
				}
			}
			return clientsToReturn;
		}else{
			return clients;
		}
	}

	@RequestMapping(value = "/deleteAffiliate", method = RequestMethod.POST)
	public @ResponseBody boolean deleteAffiliate(@RequestParam Integer affiliateId) throws Exception {
		return this.affiliateService.delete(affiliateId);
	}

	@RequestMapping(value = "/existsAffiliate", method = RequestMethod.GET)
	public @ResponseBody Boolean existsAffiliate(@RequestParam String code) throws Exception {
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

		String sortId = (parametersMap.get("id") == null || parametersMap.get("id") == "") ? null : parametersMap.get("id");
		String sortCode = (parametersMap.get("code") == null || parametersMap.get("code") == "") ? null : parametersMap.get("code");
		String sortName = (parametersMap.get("name") == null || parametersMap.get("name") == "") ? null : parametersMap.get("name");
		String sortSurname = (parametersMap.get("surname") == null || parametersMap.get("surname") == "") ? null : parametersMap.get("surname");
		String sortDocumentType = (parametersMap.get("documentType") == null || parametersMap.get("documentType") == "") ? null : parametersMap.get("documentType");
		String sortDocument = (parametersMap.get("document") == null || parametersMap.get("document") == "") ? null : parametersMap.get("document");
		String sortActive = (parametersMap.get("active") == null || parametersMap.get("active") == "") ? null : parametersMap.get("active");

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

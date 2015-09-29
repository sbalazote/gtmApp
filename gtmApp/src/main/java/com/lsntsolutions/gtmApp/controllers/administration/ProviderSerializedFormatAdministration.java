package com.lsntsolutions.gtmApp.controllers.administration;

import java.util.List;

import com.lsntsolutions.gtmApp.model.ProviderSerializedFormat;
import com.lsntsolutions.gtmApp.service.ProviderSerializedFormatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lsntsolutions.gtmApp.dto.ProviderSerializedProductDTO;

@Controller
public class ProviderSerializedFormatAdministration {

	@Autowired
	private ProviderSerializedFormatService providerSerializedFormatService;

	@RequestMapping(value = "/providerSerializedFormatAdministration", method = RequestMethod.GET)
	public String providerSerializedFormatAdministration(ModelMap modelMap) throws Exception {
		return "providerSerializedFormatAdministration";
	}

	@RequestMapping(value = "/addProviderSerializedFormat", method = RequestMethod.GET)
	public String addProviderSerializedFormat(ModelMap modelMap) throws Exception {
		return "addProviderSerializedFormat";
	}

	@RequestMapping(value = "/saveProviderSerializedFormat", method = RequestMethod.POST)
	public @ResponseBody void saveProviderSerializedFormat(@RequestBody ProviderSerializedProductDTO providerSerializedProductDTO) throws Exception {
		this.providerSerializedFormatService.save(this.buildModel(providerSerializedProductDTO));
	}

	private ProviderSerializedFormat buildModel(ProviderSerializedProductDTO providerSerializedProductDTO) {
		ProviderSerializedFormat providerSerializedFormat = new ProviderSerializedFormat();
		if (providerSerializedProductDTO.getId() != null) {
			providerSerializedFormat.setId(providerSerializedProductDTO.getId());
		}

		if (providerSerializedProductDTO.getGtin() != null) {
			String gtinLength = providerSerializedProductDTO.getGtin();
			if (gtinLength.equals("")) {
				providerSerializedFormat.setGtinLength(null);
			} else {
				providerSerializedFormat.setGtinLength(Integer.parseInt(gtinLength));
			}
		}

		if (providerSerializedProductDTO.getExpirationDate() != null) {
			String expirationDateLength = providerSerializedProductDTO.getExpirationDate();
			if (expirationDateLength.equals("")) {
				providerSerializedFormat.setExpirationDateLength(null);
			} else {
				providerSerializedFormat.setExpirationDateLength(Integer.parseInt(expirationDateLength));
			}
		}

		if (providerSerializedProductDTO.getBatch() != null) {
			String batchLength = providerSerializedProductDTO.getBatch();
			if (batchLength.equals("")) {
				providerSerializedFormat.setBatchLength(null);
			} else {
				providerSerializedFormat.setBatchLength(Integer.parseInt(batchLength));
			}
		}

		if (providerSerializedProductDTO.getSerialNumber() != null) {
			String serialNumberLength = providerSerializedProductDTO.getSerialNumber();
			if (serialNumberLength.equals("")) {
				providerSerializedFormat.setSerialNumberLength(null);
			} else {
				providerSerializedFormat.setSerialNumberLength(Integer.parseInt(serialNumberLength));
			}
		}
		providerSerializedFormat.setSequence(providerSerializedProductDTO.getSequence());

		return providerSerializedFormat;
	}

	@RequestMapping(value = "/deleteProviderSerializedFormat", method = RequestMethod.POST)
	public @ResponseBody boolean deleteProviderSerializedFormat(@RequestParam Integer serializedFormatId) throws Exception {
		return this.providerSerializedFormatService.delete(serializedFormatId);
	}

	@RequestMapping(value = "/getProviderSerializedFormats", method = RequestMethod.POST)
	public @ResponseBody List<ProviderSerializedFormat> getProviderSerializedFormats() throws Exception {
		return this.providerSerializedFormatService.getAll();
	}
}

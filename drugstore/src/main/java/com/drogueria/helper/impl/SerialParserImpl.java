package com.drogueria.helper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.dto.ProviderSerializedProductDTO;
import com.drogueria.helper.SerialParser;
import com.drogueria.model.ProviderSerializedFormat;
import com.drogueria.model.ProviderSerializedFormatTokens;
import com.drogueria.service.ProviderSerializedFormatService;
import com.drogueria.service.ProviderSerializedFormatTokensService;

@Service
@Transactional
public class SerialParserImpl implements SerialParser {

	@Autowired
	private ProviderSerializedFormatService providerSerializedFormatService;
	@Autowired
	private ProviderSerializedFormatTokensService providerSerializedFormatTokensService;

	@Override
	public ProviderSerializedProductDTO parse(String serial) {
		List<ProviderSerializedFormatTokens> formatTokens = this.providerSerializedFormatTokensService.getAll();

		List<ProviderSerializedFormat> formats = this.providerSerializedFormatService.getAll();
		for (ProviderSerializedFormat format : formats) {
			String[] sequence = format.getSplittedSequence();
			String regexp = "";
			for (String fieldType : sequence) {
				regexp += this.getSeparators(fieldType, formatTokens) + "[a-zA-Z0-9]{" + format.getLength(fieldType) + "}";
			}
			if (serial.matches(regexp)) {
				return this.newProductItemDTO(serial, format, formatTokens);
			}
		}
		return null;
	}

	@Override
	public boolean parseSelfSerial(String serial) {
		String regexp = "414[0-9]{13}21[0-9]{7}";
		return serial.matches(regexp);
	}

	private String getSeparators(String fieldType, List<ProviderSerializedFormatTokens> formatTokens) {
		String separators = "";
		for (String separator : this.getSeparatorList(fieldType, formatTokens)) {
			separators += separator + "|";
		}
		separators = separators.substring(0, separators.length() - 1);
		return "(" + separators + ")";
	}

	private List<String> getSeparatorList(String fieldType, List<ProviderSerializedFormatTokens> formatTokens) {
		List<String> separators = new ArrayList<>();
		for (ProviderSerializedFormatTokens formatToken : formatTokens) {
			if (formatToken.getCode().equalsIgnoreCase(fieldType)) {
				separators.add(formatToken.getSeparatorToken());
			}
		}
		return separators;
	}

	private ProviderSerializedProductDTO newProductItemDTO(String serial, ProviderSerializedFormat format, List<ProviderSerializedFormatTokens> formatTokens) {
		ProviderSerializedProductDTO productItemDTO = new ProviderSerializedProductDTO();
		for (String fieldType : format.getSplittedSequence()) {
			List<String> separators = this.getSeparatorList(fieldType, formatTokens);
			for (String separator : separators) {
				if (serial.startsWith(separator)) {
					int beginIndex = separator.length();
					int endIndex = separator.length() + format.getLength(fieldType);
					String value = serial.substring(beginIndex, endIndex);
					productItemDTO.setValue(fieldType, value);
					serial = serial.substring(endIndex);
				}
			}
		}
		return productItemDTO;
	}

}
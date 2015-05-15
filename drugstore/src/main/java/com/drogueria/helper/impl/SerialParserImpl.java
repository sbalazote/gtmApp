package com.drogueria.helper.impl;

import java.util.ArrayList;
import java.util.List;

import com.sun.tools.jxc.apt.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drogueria.dto.ProviderSerializedProductDTO;
import com.drogueria.helper.SerialParser;
import com.drogueria.model.ProviderSerializedFormat;
import com.drogueria.model.ProviderSerializedFormatTokens;
import com.drogueria.service.ProviderSerializedFormatService;
import com.drogueria.service.ProviderSerializedFormatTokensService;
import com.drogueria.constant.Constants;

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
	public boolean isParseSelfSerial(String serial) {
		String regexp = "414[0-9]{13}21[0-9]{7}";
		return serial.matches(regexp);
	}

    @Override
    public ProviderSerializedProductDTO parseSelfSerial(String serial) {
        ProviderSerializedProductDTO providerSerializedProductDTO = null;
        if (serial.length() == Constants.SELF_SERIALIZED_LENGTH) {
            Integer toIndexSerial =  Constants.SELF_SERIALIZED_CODE_LENGTH + Constants.GLN_LENGTH;
            String gln = serial.substring(Constants.SELF_SERIALIZED_CODE_LENGTH, toIndexSerial);

            Integer sinceSerialIndex = toIndexSerial + Constants.SELF_SERIALIZED_SEPARATOR_SERIAL_LENGTH;
            Integer toSerialEndIndex = sinceSerialIndex + Constants.SELF_SERIALIZED_SERIAL_LENGTH;
            String serialNumber = serial.substring(sinceSerialIndex, toSerialEndIndex);
            providerSerializedProductDTO = new ProviderSerializedProductDTO();
            providerSerializedProductDTO.setSerialNumber(gln + serialNumber);
            providerSerializedProductDTO.setValue("S", gln + serialNumber);
        }
        return providerSerializedProductDTO;
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

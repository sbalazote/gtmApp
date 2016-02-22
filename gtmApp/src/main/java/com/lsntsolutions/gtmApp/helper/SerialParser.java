package com.lsntsolutions.gtmApp.helper;

import com.lsntsolutions.gtmApp.dto.ProviderSerializedFormatMatchedDTO;
import com.lsntsolutions.gtmApp.dto.ProviderSerializedProductDTO;

import java.util.List;

public interface SerialParser {

	ProviderSerializedProductDTO parse(String serial, Integer formatSerializedId);

	List<ProviderSerializedFormatMatchedDTO> getMatchParsers(String serial);

	boolean isParseSelfSerial(String serial);

    ProviderSerializedProductDTO parseSelfSerial(String serial);
}

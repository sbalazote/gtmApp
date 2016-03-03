package com.lsntsolutions.gtmApp.helper;

import com.lsntsolutions.gtmApp.dto.ProviderSerializedProductDTO;
import com.lsntsolutions.gtmApp.dto.ProviderSerializedSerialFormatDTO;

import java.util.List;

public interface SerialParser {

	ProviderSerializedProductDTO parse(String serial, Integer formatSerializedId);

	List<ProviderSerializedSerialFormatDTO> getMatchParsers(String serial);

	boolean isParseSelfSerial(String serial);

    ProviderSerializedProductDTO parseSelfSerial(String serial);
}

package com.lsntsolutions.gtmApp.helper;

import com.lsntsolutions.gtmApp.dto.ProviderSerializedProductDTO;

public interface SerialParser {

	ProviderSerializedProductDTO parse(String serial);

	boolean isParseSelfSerial(String serial);

    ProviderSerializedProductDTO parseSelfSerial(String serial);
}

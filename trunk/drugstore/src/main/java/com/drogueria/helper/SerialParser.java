package com.drogueria.helper;

import com.drogueria.dto.ProviderSerializedProductDTO;

public interface SerialParser {

	ProviderSerializedProductDTO parse(String serial);

	boolean parseSelfSerial(String serial);

}

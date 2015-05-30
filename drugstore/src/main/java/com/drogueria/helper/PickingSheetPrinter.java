package com.drogueria.helper;

import java.io.IOException;
import java.util.List;

import com.lowagie.text.DocumentException;

public interface PickingSheetPrinter {

	void print(List<Integer> provisioningIds) throws DocumentException, IOException;

}

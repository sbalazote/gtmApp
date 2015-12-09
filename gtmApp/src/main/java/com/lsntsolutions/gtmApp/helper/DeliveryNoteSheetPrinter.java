package com.lsntsolutions.gtmApp.helper;

import com.lsntsolutions.gtmApp.dto.PrinterResultDTO;

import java.util.List;

public interface DeliveryNoteSheetPrinter {

	void print(String userName, List<Integer> egressIds, PrinterResultDTO printerResultDTO, boolean printSupplyings, boolean printOutputs, boolean printOrders);
}

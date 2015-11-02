package com.lsntsolutions.gtmApp.helper;

import java.util.List;

public interface DeliveryNoteSheetPrinter {

	List<String> print(String userName, List<Integer> ordersIds);
}

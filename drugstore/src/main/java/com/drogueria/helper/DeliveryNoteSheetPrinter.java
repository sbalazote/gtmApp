package com.drogueria.helper;

import java.util.List;

public interface DeliveryNoteSheetPrinter {

	public static int NUMBER_OF_DELIVERY_NOTE_DETAILS_PER_PAGE = 10;

	List<Integer> print(List<Integer> ids);
}

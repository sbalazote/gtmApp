package com.lsntsolutions.gtmApp.helper;

import com.lsntsolutions.gtmApp.dto.StockDTO;

import java.util.Comparator;

public class StockDTOTotalAmountComparator implements Comparator<StockDTO> {
    public int compare(StockDTO stockDTO1, StockDTO stockDTO2) {
        return stockDTO1.getTotalAmount() - stockDTO2.getTotalAmount();
    }
}
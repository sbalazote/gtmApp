package com.lsntsolutions.gtmApp.helper;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.lsntsolutions.gtmApp.constant.DeliveryNoteConfigParam;
import com.lsntsolutions.gtmApp.dto.PrinterResultDTO;
import com.lsntsolutions.gtmApp.model.*;
import com.lsntsolutions.gtmApp.util.StringUtility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public interface DeliveryNoteSheetPrinter {

	void print(String userName, List<Integer> supplyingsIds, PrinterResultDTO printerResultDTO, boolean printSupplyings, boolean printOutputs, boolean printOrders);
}

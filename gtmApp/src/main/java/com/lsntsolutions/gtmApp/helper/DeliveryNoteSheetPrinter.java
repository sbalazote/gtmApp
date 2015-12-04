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

public abstract class DeliveryNoteSheetPrinter {

	public int offsetY;
	public PdfContentByte overContent;
	public BaseFont bf;
	public Map<String, Float> dnConfigMap;
	public boolean printHeader;
	int PRODUCT_DETAIL_HEADER_LINE_OFFSET_Y = 10;
	int SERIAL_DETAIL_LINE_OFFSET_Y = 10;
	int PRODUCT_BATCH_EXPIRATIONDATE_HEADER_LINE_OFFSET_Y = 10;

	public TreeMap<String, List<? extends Detail>> groupByProductAndBatch(List<?extends Detail> details) {
		TreeMap<String, List<? extends Detail>> detailsMap = new TreeMap<>();

		for(Detail detail : details){
			String id = Integer.toString(detail.getProduct().getId());
			String batch = detail.getBatch();
			String key = id + "," + batch;

			List<Detail> list = (List<Detail>) detailsMap.get(key);
			if(list == null) {
				list = new ArrayList<>();
			}
			list.add(detail);
			detailsMap.put(key, list);
		}

		return detailsMap;
	}

	public int numberOfLinesNeeded(TreeMap<String, List<? extends Detail>> orderMap) {
		int numberOfLines = 0;

		for(List<? extends Detail> outputDetail: orderMap.values()){
			String type = outputDetail.get(0).getProduct().getType();
			if (type.compareTo("BE") == 0) {
				numberOfLines += outputDetail.size();
			} else {
				numberOfLines += Math.ceil((double)outputDetail.size() / 4);
			}
		}

		return numberOfLines;
	}

	public int getProductTotalAmount(int productId, List<? extends Detail> details) {
		HashMap<Integer, Integer> detailsMap = new HashMap<>();

		for(Detail detail : details){
			Integer id = detail.getProduct().getId();

			Integer currentAmount = detailsMap.get(id);
			if(currentAmount == null) {
				currentAmount = new Integer(0);
			}
			currentAmount += detail.getAmount();
			detailsMap.put(id, currentAmount);
		}

		return detailsMap.get(productId);
	}

	public void printFooter(int amount) {
		overContent.saveState();

		// seteo el tipo de fuente.
		try {
			bf = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, false);
			overContent.setFontAndSize(bf, dnConfigMap.get(DeliveryNoteConfigParam.FONT_SIZE.name()));
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// imprimo Cantidad de Items para el remito.
		overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.NUMBEROFITEMS_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.NUMBEROFITEMS_Y.name()));
		overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.NUMBEROFITEMS_PRINT.name()) == 1 ? ("Items: " + amount) : "");

		overContent.restoreState();
	}

	public void printSerialDetails(List<? extends Detail> orderDetails) {

		// offset con respecto a la linea anterior.
		offsetY += SERIAL_DETAIL_LINE_OFFSET_Y;

		overContent.saveState();

		// seteo el tipo de fuente.
		try {
			bf = BaseFont.createFont(BaseFont.TIMES_BOLDITALIC, BaseFont.WINANSI, false);
			overContent.setFontAndSize(bf, dnConfigMap.get(DeliveryNoteConfigParam.FONT_SIZE.name()));
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		switch (orderDetails.size()) {
			case 1: {
				overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
				overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_PRINT.name()) == 1 ? orderDetails.get(0).getSerialNumber() : "");
				break;
			}
			case 2: {
				overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
				overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_PRINT.name()) == 1 ? orderDetails.get(0).getSerialNumber() : "");
				overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
				overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_PRINT.name()) == 1 ? orderDetails.get(1).getSerialNumber() : "");
				break;
			}
			case 3: {
				overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
				overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_PRINT.name()) == 1 ? orderDetails.get(0).getSerialNumber() : "");
				overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
				overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_PRINT.name()) == 1 ? orderDetails.get(1).getSerialNumber() : "");
				overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN3_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
				overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN3_PRINT.name()) == 1 ? orderDetails.get(2).getSerialNumber() : "");
				break;
			}
			case 4: {
				overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
				overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN1_PRINT.name()) == 1 ? orderDetails.get(0).getSerialNumber() : "");
				overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
				overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN2_PRINT.name()) == 1 ? orderDetails.get(1).getSerialNumber() : "");
				overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN3_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
				overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN3_PRINT.name()) == 1 ? orderDetails.get(2).getSerialNumber() : "");
				overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN4_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
				overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.SERIAL_COLUMN4_PRINT.name()) == 1 ? orderDetails.get(3).getSerialNumber() : "");
				break;
			}
			default: {
				break;
			}
		}

		overContent.restoreState();
	}

	public void printProductDetailHeader(String description, String monodrug, String brand, int totalAmount) {

		// offset con respecto a la linea anterior.
		offsetY += (printHeader ? 0 : PRODUCT_DETAIL_HEADER_LINE_OFFSET_Y);

		overContent.saveState();

		// seteo el tipo de fuente.
		try {
			bf = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, false);
			overContent.setFontAndSize(bf, dnConfigMap.get(DeliveryNoteConfigParam.FONT_SIZE.name()));
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// imprimo descripcion.
		overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DESCRIPTION_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
		overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DESCRIPTION_PRINT.name()) == 1 ? description : "");

		// imprimo monodraga.
		overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_MONODRUG_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
		overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_MONODRUG_PRINT.name()) == 1 ? monodrug : "");

		// imprimo marca.
		overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BRAND_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
		overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BRAND_PRINT.name()) == 1 ? brand : "");

		// imprimo cantidad total.
		overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_AMOUNT_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
		overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_AMOUNT_PRINT.name()) == 1 ? Integer.toString(totalAmount) : "");

		overContent.restoreState();

		printHeader = false;
	}

	public void printProductBatchExpirationDateHeader(String batch, String expirationDate, String batchAmount) {

		// offset con respecto a la linea anterior.
		offsetY += PRODUCT_BATCH_EXPIRATIONDATE_HEADER_LINE_OFFSET_Y;

		overContent.saveState();

		// seteo el tipo de fuente.
		try {
			bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, false);
			overContent.setFontAndSize(bf, dnConfigMap.get(DeliveryNoteConfigParam.FONT_SIZE.name()));
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// imprimo lote.
		overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
		overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_PRINT.name()) == 1 ? ("Lote: " + batch) : "");

		// imprimo vencimiento.
		overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_X.name()) + 120, dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
		overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_BATCHEXPIRATIONDATE_PRINT.name()) == 1 ? ("Vto.: " + expirationDate) : "");

		// imprimo cantidad total del lote.
		overContent.setTextMatrix(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_AMOUNT_X.name()), dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_DETAILS_Y.name()) - offsetY);
		overContent.showText(dnConfigMap.get(DeliveryNoteConfigParam.PRODUCT_AMOUNT_PRINT.name()) == 1 ? batchAmount : "");

		overContent.restoreState();
	}

	public abstract void print(String userName, List<Integer> supplyingsIds, PrinterResultDTO printerResultDTO, boolean printSupplyings, boolean printOutputs, boolean printOrders);
}

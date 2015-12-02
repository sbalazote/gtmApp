package com.lsntsolutions.gtmApp.helper.impl.excel;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lsntsolutions.gtmApp.helper.AbstractExcelView;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.lsntsolutions.gtmApp.model.Stock;

public class StockExcelView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Sheet sheet = workbook.createSheet("STOCK");

		@SuppressWarnings("unchecked")
		List<Stock> stockList = (List<Stock>) model.get("stocks");

		Row row = null;
		Cell cell = null;
		int r = 0;
		int c = 0;

		// Style for header cell
		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setAlignment(CellStyle.ALIGN_CENTER);

		// Create header cells
		row = sheet.createRow(r++);

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("CODIGO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("DESCRIPCION");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("GTIN");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("CONVENIO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("SERIE");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("LOTE");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("VTO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("CANTIDAD");

		// Create data cell
		for (Stock stock : stockList) {
			row = sheet.createRow(r++);
			c = 0;
			row.createCell(c++).setCellValue(stock.getProduct().getCode());
			row.createCell(c++).setCellValue(stock.getProduct().getDescription());
			row.createCell(c++).setCellValue(stock.getGtin() != null ? stock.getGtin().getNumber() : "");
			row.createCell(c++).setCellValue(stock.getAgreement().getDescription());
			row.createCell(c++).setCellValue(stock.getSerialNumber());
			row.createCell(c++).setCellValue(stock.getBatch());
			row.createCell(c++).setCellValue(stock.getExpirationDate());
			row.createCell(c++).setCellValue(stock.getAmount());

		}
		for (int i = 0; i < 7; i++) {
			sheet.autoSizeColumn(i, true);
		}

	}
}

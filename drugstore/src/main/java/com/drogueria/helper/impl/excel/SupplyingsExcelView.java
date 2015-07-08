package com.drogueria.helper.impl.excel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.drogueria.helper.AbstractExcelView;
import com.drogueria.model.Supplying;
import com.drogueria.model.SupplyingDetail;

public class SupplyingsExcelView extends AbstractExcelView {
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Sheet sheet = workbook.createSheet("DISPENSAS");

		@SuppressWarnings("unchecked")
		List<Supplying> supplyings = (List<Supplying>) model.get("supplyings");

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
		cell.setCellValue("ID");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("CONVENIO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("CLIENTE");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("FECHA");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("ANULADO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("PRODUCTO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("GTIN");

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
		for (Supplying supplying : supplyings) {
			c = 0;
			for (SupplyingDetail supplyingDetail : supplying.getSupplyingDetails()) {
				row = sheet.createRow(r++);
				c = 0;
				row.createCell(c++).setCellValue(supplying.getId());
				row.createCell(c++).setCellValue(supplying.getAgreement().getDescription());
				row.createCell(c++).setCellValue(supplying.getClient().getCorporateName());
				row.createCell(c++).setCellValue(dateFormatter.format(supplying.getDate()));
				row.createCell(c++).setCellValue(supplying.isCancelled() ? "SI" : "NO");
				row.createCell(c++).setCellValue(supplyingDetail.getProduct().getCode() + " - " + supplyingDetail.getProduct().getDescription());
				row.createCell(c++).setCellValue(supplyingDetail.getGtin() != null ? supplyingDetail.getGtin().getNumber() : "");
				row.createCell(c++).setCellValue(supplyingDetail.getSerialNumber());
				row.createCell(c++).setCellValue(supplyingDetail.getBatch());
				row.createCell(c++).setCellValue(dateFormatter.format(supplyingDetail.getExpirationDate()));
				row.createCell(c++).setCellValue(supplyingDetail.getAmount());
			}

		}
		for (int i = 0; i < 16; i++) {
			sheet.autoSizeColumn(i, true);
		}

	}
}

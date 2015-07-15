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
import com.drogueria.model.Output;
import com.drogueria.model.OutputDetail;

public class OutputExcelView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Sheet sheet = workbook.createSheet("EGRESOS");

		@SuppressWarnings("unchecked")
		List<Output> outputs = (List<Output>) model.get("outputs");

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
		cell.setCellValue("CONCEPTO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("CONVENIO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("CLIENTE/PROVEEDOR");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("FECHA");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("CODIGO TRANSC. ANMAT");

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
		for (Output output : outputs) {
			c = 0;
			for (OutputDetail outputDetail : output.getOutputDetails()) {
				row = sheet.createRow(r++);
				c = 0;
				row.createCell(c++).setCellValue(output.getId());
				row.createCell(c++).setCellValue(output.getConcept().getDescription());
				row.createCell(c++).setCellValue(output.getAgreement().getDescription());
				row.createCell(c++).setCellValue(output.getClientOrProviderDescription());
				row.createCell(c++).setCellValue(dateFormatter.format(output.getDate()));
				row.createCell(c++).setCellValue(output.isCancelled() ? "SI" : "NO");
				row.createCell(c++).setCellValue(outputDetail.getProduct().getCode() + " - " + outputDetail.getProduct().getDescription());
				row.createCell(c++).setCellValue(outputDetail.getGtin() != null ? outputDetail.getGtin().getNumber() : "");
				row.createCell(c++).setCellValue(outputDetail.getSerialNumber());
				row.createCell(c++).setCellValue(outputDetail.getBatch());
				row.createCell(c++).setCellValue(dateFormatter.format(outputDetail.getExpirationDate()));
				row.createCell(c++).setCellValue(outputDetail.getAmount());
			}

		}
		for (int i = 0; i < 16; i++) {
			sheet.autoSizeColumn(i, true);
		}

	}
}
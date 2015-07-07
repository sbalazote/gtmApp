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
import com.drogueria.model.Audit;

public class AuditExcelView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Sheet sheet = workbook.createSheet("AUDITORIAS");

		@SuppressWarnings("unchecked")
		List<Audit> audits = (List<Audit>) model.get("audits");

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
		cell.setCellValue("FECHA");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("ROL");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("OPERACION");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("ACCION");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("USUARIO");

		// Create data cell
		for (Audit audit : audits) {
			row = sheet.createRow(r++);
			c = 0;
			row.createCell(c++).setCellValue(dateFormatter.format(audit.getDate()));
			row.createCell(c++).setCellValue(audit.getRole().getDescription());
			row.createCell(c++).setCellValue(audit.getOperationId());
			row.createCell(c++).setCellValue(audit.getAuditAction().getDescription());
			row.createCell(c++).setCellValue(audit.getUser().getName());

		}
		for (int i = 0; i < 4; i++) {
			sheet.autoSizeColumn(i, true);
		}

	}
}
package com.lsntsolutions.gtmApp.helper.impl.excel;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lsntsolutions.gtmApp.model.Event;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.lsntsolutions.gtmApp.helper.AbstractExcelView;

public class EventsExcelView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Sheet sheet = workbook.createSheet("EVENTOS");

		@SuppressWarnings("unchecked")
		List<Event> events = (List<Event>) model.get("events");

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
		cell.setCellValue("CODIGO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("DESCRIPCION");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("AGENTE ORIGEN");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("AGENTE DESTINO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("ACTIVO");

		// Create data cell
		for (Event event : events) {
			row = sheet.createRow(r++);
			c = 0;
			row.createCell(c++).setCellValue(event.getId());
			row.createCell(c++).setCellValue(event.getCode());
			row.createCell(c++).setCellValue(event.getDescription());
			row.createCell(c++).setCellValue(event.getOriginAgent().getDescription());
			row.createCell(c++).setCellValue(event.getDestinationAgent().getDescription());
			row.createCell(c++).setCellValue(event.isActive());

		}
		for (int i = 0; i < 4; i++) {
			sheet.autoSizeColumn(i, true);
		}

	}
}
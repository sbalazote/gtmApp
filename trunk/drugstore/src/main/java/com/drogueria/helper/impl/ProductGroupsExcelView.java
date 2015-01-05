package com.drogueria.helper.impl;

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
import com.drogueria.model.ProductGroup;

public class ProductGroupsExcelView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Sheet sheet = workbook.createSheet("AGRUPACIONES");

		@SuppressWarnings("unchecked")
		List<ProductGroup> productGroups = (List<ProductGroup>) model.get("productGroups");

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
		cell.setCellValue("ACTIVO");

		// Create data cell
		for (ProductGroup productGroup : productGroups) {
			row = sheet.createRow(r++);
			c = 0;
			row.createCell(c++).setCellValue(productGroup.getId());
			row.createCell(c++).setCellValue(productGroup.getCode());
			row.createCell(c++).setCellValue(productGroup.getDescription());
			row.createCell(c++).setCellValue(productGroup.isActive());

		}
		for (int i = 0; i < 4; i++) {
			sheet.autoSizeColumn(i, true);
		}

	}
}
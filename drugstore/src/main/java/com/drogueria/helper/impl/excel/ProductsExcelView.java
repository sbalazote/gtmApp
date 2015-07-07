package com.drogueria.helper.impl.excel;

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
import com.drogueria.model.Product;

public class ProductsExcelView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Sheet sheet = workbook.createSheet("PRODUCTOS");

		@SuppressWarnings("unchecked")
		List<Product> products = (List<Product>) model.get("products");

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
		cell.setCellValue("MARCA");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("MONODROGA");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("AGRUPACION");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("ACCION FARMACOLOGICA");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("FRIO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("INFORMA ANMAT");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("TIPO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("ACTIVO");

		// Create data cell
		for (Product product : products) {
			row = sheet.createRow(r++);
			c = 0;
			row.createCell(c++).setCellValue(product.getId());
			row.createCell(c++).setCellValue(product.getCode());
			row.createCell(c++).setCellValue(product.getDescription());
			row.createCell(c++).setCellValue(product.getBrand().getDescription());
			row.createCell(c++).setCellValue(product.getMonodrug().getDescription());
			row.createCell(c++).setCellValue(product.getGroup().getDescription());
			row.createCell(c++).setCellValue(product.getDrugCategory().getDescription());
			row.createCell(c++).setCellValue(product.isCold() ? "SI" : "NO");
			row.createCell(c++).setCellValue(product.isInformAnmat() ? "SI" : "NO");
			row.createCell(c++).setCellValue(
					product.getType().equalsIgnoreCase("BE") ? "LOTE/VTO" : product.getType().equalsIgnoreCase("PS") ? "TRAZADO ORIGEN" : "TRAZADO PROPIO");
			row.createCell(c++).setCellValue(product.isActive() ? "SI" : "NO");

		}
		for (int i = 0; i < 4; i++) {
			sheet.autoSizeColumn(i, true);
		}

	}
}
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
import com.drogueria.model.DeliveryLocation;

public class DeliveryLocationsExcelView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Sheet sheet = workbook.createSheet("LUGARES DE ENTREGA");

		@SuppressWarnings("unchecked")
		List<DeliveryLocation> deliveryLocations = (List<DeliveryLocation>) model.get("deliveryLocations");

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
		cell.setCellValue("NOMBRE");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("CUIT");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("RAZON SOCIAL");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("PROVINCIA");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("LOCALIDAD");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("DIRECCION");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("COD. POSTAL");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("TIPO DE IVA");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("TELEFONO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("MAIL");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("GLN");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("AGENTE");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("ACTIVO");

		// Create data cell
		for (DeliveryLocation deliveryLocation : deliveryLocations) {
			row = sheet.createRow(r++);
			c = 0;
			row.createCell(c++).setCellValue(deliveryLocation.getId());
			row.createCell(c++).setCellValue(deliveryLocation.getCode());
			row.createCell(c++).setCellValue(deliveryLocation.getName());
			row.createCell(c++).setCellValue(deliveryLocation.getTaxId());
			row.createCell(c++).setCellValue(deliveryLocation.getCorporateName());
			row.createCell(c++).setCellValue(deliveryLocation.getProvince().getName());
			row.createCell(c++).setCellValue(deliveryLocation.getLocality());
			row.createCell(c++).setCellValue(deliveryLocation.getAddress());
			row.createCell(c++).setCellValue(deliveryLocation.getZipCode());
			row.createCell(c++).setCellValue(deliveryLocation.getVATLiability().getDescription());
			row.createCell(c++).setCellValue(deliveryLocation.getPhone());
			row.createCell(c++).setCellValue(deliveryLocation.getMail());
			row.createCell(c++).setCellValue(deliveryLocation.getGln());
			row.createCell(c++).setCellValue(deliveryLocation.getAgent().getDescription());
			row.createCell(c++).setCellValue(deliveryLocation.isActive() ? "SI" : "NO");

		}
		for (int i = 0; i < 4; i++) {
			sheet.autoSizeColumn(i, true);
		}

	}
}
package com.lsntsolutions.gtmApp.helper.impl.excel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lsntsolutions.gtmApp.model.ProvisioningRequest;
import com.lsntsolutions.gtmApp.model.ProvisioningRequestDetail;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.lsntsolutions.gtmApp.helper.AbstractExcelView;

public class ProvisioningExcelView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Sheet sheet = workbook.createSheet("PEDIDOS");

		@SuppressWarnings("unchecked")
		List<ProvisioningRequest> provisionings = (List<ProvisioningRequest>) model.get("provisionings");

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
		cell.setCellValue("AFILIADO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("LUGAR DE ENTREGA");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("FECHA DE ENTREGA");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("OPERADOR LOGISTICO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("COMENTARIOS");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("ESTADO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("PRODUCTO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("CANTIDAD");

		// Create data cell
		for (ProvisioningRequest provisioningRequest : provisionings) {
			c = 0;
			for (ProvisioningRequestDetail provisioningRequestDetail : provisioningRequest.getProvisioningRequestDetails()) {
				row = sheet.createRow(r++);
				c = 0;
				row.createCell(c++).setCellValue(provisioningRequest.getId());
				row.createCell(c++).setCellValue(provisioningRequest.getAgreement().getDescription());
				row.createCell(c++).setCellValue(provisioningRequest.getClient().getName());
				row.createCell(c++).setCellValue(provisioningRequest.getAffiliate().getSurname() + " " + provisioningRequest.getAffiliate().getName());
				row.createCell(c++).setCellValue(provisioningRequest.getDeliveryLocation().getName());
				row.createCell(c++).setCellValue(dateFormatter.format(provisioningRequest.getDeliveryDate()));
				row.createCell(c++)
						.setCellValue(provisioningRequest.getLogisticsOperator() != null ? provisioningRequest.getLogisticsOperator().getName() : "");
				row.createCell(c++).setCellValue(provisioningRequest.getComment() != null ? provisioningRequest.getComment() : "");
				row.createCell(c++).setCellValue(provisioningRequest.getState().getDescription());
				row.createCell(c++).setCellValue(
						provisioningRequestDetail.getProduct().getCode() + " - " + provisioningRequestDetail.getProduct().getDescription());
				row.createCell(c++).setCellValue(provisioningRequestDetail.getAmount());
			}

		}
		for (int i = 0; i < 16; i++) {
			sheet.autoSizeColumn(i, true);
		}

	}
}

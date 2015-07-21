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
import com.drogueria.model.Input;
import com.drogueria.model.InputDetail;

public class InputExcelView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Sheet sheet = workbook.createSheet("INGRESOS");

		@SuppressWarnings("unchecked")
		List<Input> inputs = (List<Input>) model.get("inputs");

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
		cell.setCellValue("CODIGO CONCEPTO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("CONCEPTO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("CODIGO CONVENIO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("CONVENIO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("CODIGO CLIENTE/PROVEEDOR");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("CLIENTE/PROVEEDOR");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("FECHA INGRESO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("CODIGO TRANSC. ANMAT");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("ANULADO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("NUMERO DE REMITO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("NUMERO DE ORDEN");

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
		for (Input input : inputs) {
			c = 0;
			for (InputDetail inputDetail : input.getInputDetails()) {
				row = sheet.createRow(r++);
				c = 0;
				row.createCell(c++).setCellValue(input.getFormatId());
				row.createCell(c++).setCellValue(input.getConcept().getFormatCode());
				row.createCell(c++).setCellValue(input.getConcept().getDescription());
                row.createCell(c++).setCellValue(input.getAgreement().getFormatCode());
				row.createCell(c++).setCellValue(input.getAgreement().getDescription());
                row.createCell(c++).setCellValue(input.getClientOrProviderCode());
				row.createCell(c++).setCellValue(input.getClientOrProviderDescription());
				row.createCell(c++).setCellValue(dateFormatter.format(input.getDate()));
                String transactionCode;
                if(input.isInformAnmat()){
                    if(input.getTransactionCodeANMAT() != null){
                        transactionCode = input.getTransactionCodeANMAT();
                    }else{
                        transactionCode = "Pendiente";
                    }
                }else{
                    transactionCode = "No informa";
                }
				row.createCell(c++).setCellValue(transactionCode);
				row.createCell(c++).setCellValue(input.isCancelled() ? "SI" : "NO");
				row.createCell(c++).setCellValue(input.getDeliveryNoteNumber());
				row.createCell(c++).setCellValue(input.getPurchaseOrderNumber());
				row.createCell(c++).setCellValue(inputDetail.getProduct().getCode() + " - " + inputDetail.getProduct().getDescription());
				row.createCell(c++).setCellValue(inputDetail.getGtin() != null ? inputDetail.getGtin().getNumber() : "");
				row.createCell(c++).setCellValue(inputDetail.getSerialNumber());
				row.createCell(c++).setCellValue(inputDetail.getBatch());
				row.createCell(c++).setCellValue(dateFormatter.format(inputDetail.getExpirationDate()));
				row.createCell(c++).setCellValue(inputDetail.getAmount());
			}

		}
		for (int i = 0; i < 16; i++) {
			sheet.autoSizeColumn(i, true);
		}

	}
}

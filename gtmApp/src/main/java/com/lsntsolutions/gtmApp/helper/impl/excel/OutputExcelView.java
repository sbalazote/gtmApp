package com.lsntsolutions.gtmApp.helper.impl.excel;

import com.lsntsolutions.gtmApp.helper.AbstractExcelView;
import com.lsntsolutions.gtmApp.model.DeliveryNote;
import com.lsntsolutions.gtmApp.model.Output;
import com.lsntsolutions.gtmApp.model.OutputDetail;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutputExcelView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Sheet sheet = workbook.createSheet("EGRESOS");

		@SuppressWarnings("unchecked")
		List<Output> outputs = (List<Output>) model.get("outputs");
		@SuppressWarnings("unchecked")
		Map<Integer, List<DeliveryNote>> associatedOutputs = (HashMap<Integer, List<DeliveryNote>>) model.get("associatedOutputs");

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

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("NUMERO DE REMITO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("FECHA DE REMITO");

		cell = row.createCell(c++);
		cell.setCellStyle(style);
		cell.setCellValue("CODIGO TRANSC. ANMAT");

		// Create data cell
		for (Output output : outputs) {
			c = 0;
			for (OutputDetail outputDetail : output.getOutputDetails()) {
				row = sheet.createRow(r++);
				c = 0;
				row.createCell(c++).setCellValue(output.getId());
				row.createCell(c++).setCellValue(output.getConcept().getFormatCode());
				row.createCell(c++).setCellValue(output.getConcept().getDescription());
				row.createCell(c++).setCellValue(output.getConcept().getFormatCode());
				row.createCell(c++).setCellValue(output.getAgreement().getDescription());
				row.createCell(c++).setCellValue(output.getClientOrProviderCode());
				row.createCell(c++).setCellValue(output.getClientOrProviderDescription());
				row.createCell(c++).setCellValue(dateFormatter.format(output.getDate()));
				row.createCell(c++).setCellValue(output.isCancelled() ? "SI" : "NO");
				row.createCell(c++).setCellValue(outputDetail.getProduct().getCode() + " - " + outputDetail.getProduct().getDescription());
				row.createCell(c++).setCellValue(outputDetail.getGtin() != null ? outputDetail.getGtin().getNumber() : "");
				row.createCell(c++).setCellValue(outputDetail.getSerialNumber());
				row.createCell(c++).setCellValue(outputDetail.getBatch());
				row.createCell(c++).setCellValue(dateFormatter.format(outputDetail.getExpirationDate()));
				row.createCell(c++).setCellValue(outputDetail.getAmount());

				// DOC. NRO
				List<DeliveryNote> outputDeliveryNotes = associatedOutputs.get(new Integer(output.getId()));
				String dnNumbers = "";
				if (outputDeliveryNotes != null) {
					for (DeliveryNote elem : outputDeliveryNotes) {
						String pre = elem.isFake() ? "X" : "R";
						dnNumbers = pre.concat(elem.getNumber());
						row.createCell(c++).setCellValue(dnNumbers);
						row.createCell(c++).setCellValue(dateFormatter.format(elem.getDate()));
						if(elem.getTransactionCodeANMAT() != null){
							row.createCell(c++).setCellValue(elem.getTransactionCodeANMAT());
						}else{
							if(elem.isInformAnmat()){
								row.createCell(c++).setCellValue("Pendiente");
							}else{
								row.createCell(c++).setCellValue("No informa");
							}
						}
					}
				} else {
					row.createCell(c++).setCellValue("Doc. Nro.: NO IMPRIME" );
					row.createCell(c++).setCellValue("No informa");
					row.createCell(c++).setCellValue("No informa");
				}
			}

		}
		for (int i = 0; i < 16; i++) {
			sheet.autoSizeColumn(i, true);
		}

	}
}

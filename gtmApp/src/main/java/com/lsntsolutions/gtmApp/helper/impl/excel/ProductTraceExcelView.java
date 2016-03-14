package com.lsntsolutions.gtmApp.helper.impl.excel;

import com.lsntsolutions.gtmApp.dto.SearchProductDTO;
import com.lsntsolutions.gtmApp.dto.SearchProductResultDTO;
import com.lsntsolutions.gtmApp.helper.AbstractExcelView;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ProductTraceExcelView extends AbstractExcelView {

    private void auditSheet(Workbook workbook, String sheetName, List<SearchProductDTO> searchProductDTOList) {
        Sheet sheet = workbook.createSheet(sheetName);

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
        cell.setCellValue("ROL");

        cell = row.createCell(c++);
        cell.setCellStyle(style);
        cell.setCellValue("ID de OPERACION");

        cell = row.createCell(c++);
        cell.setCellStyle(style);
        cell.setCellValue("FECHA");

        cell = row.createCell(c++);
        cell.setCellStyle(style);
        cell.setCellValue("USUARIO");

        // Create data cell
        for (SearchProductDTO searchProductDTO : searchProductDTOList) {
            row = sheet.createRow(r++);
            c = 0;
            row.createCell(c++).setCellValue(searchProductDTO.getId());
            row.createCell(c++).setCellValue(searchProductDTO.getRole());
            row.createCell(c++).setCellValue(searchProductDTO.getOperationId());
            row.createCell(c++).setCellValue(searchProductDTO.getDate());
            row.createCell(c++).setCellValue(searchProductDTO.getUsername());

        }
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i, true);
        }
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        @SuppressWarnings("unchecked")
        SearchProductResultDTO searchProductResultDTO = (SearchProductResultDTO) model.get("searchProductResultDTO");

        List<SearchProductDTO> inputs = searchProductResultDTO.getInputs();
        List<SearchProductDTO> outputs = searchProductResultDTO.getOutputs();
        List<SearchProductDTO> orders = searchProductResultDTO.getOrders();
        List<SearchProductDTO> deliveryNotes = searchProductResultDTO.getDeliveryNotes();
        List<SearchProductDTO> supplyings = searchProductResultDTO.getSupplyings();

        if (!inputs.isEmpty()) {
            auditSheet(workbook, "INGRESOS", inputs);
        }

        if (!outputs.isEmpty()) {
            auditSheet(workbook, "EGRESOS", outputs);
        }

        if (!orders.isEmpty()) {
            auditSheet(workbook, "ORDENES", orders);
        }

        if (!deliveryNotes.isEmpty()) {
            auditSheet(workbook, "REMITOS", deliveryNotes);
        }

        if (!supplyings.isEmpty()) {
            auditSheet(workbook, "DISPENSAS", supplyings);
        }
    }
}
package com.drogueria.helper.impl.excel;

import com.drogueria.dto.AuditDTO;
import com.drogueria.dto.AuditResultDTO;
import com.drogueria.helper.AbstractExcelView;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

public class ProductTraceExcelView extends AbstractExcelView {

    private void auditSheet(Workbook workbook, String sheetName, List<AuditDTO> auditDTOList) {
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
        cell.setCellValue("ACCION");

        cell = row.createCell(c++);
        cell.setCellStyle(style);
        cell.setCellValue("FECHA");

        cell = row.createCell(c++);
        cell.setCellStyle(style);
        cell.setCellValue("USUARIO");

        // Create data cell
        for (AuditDTO auditDTO : auditDTOList) {
            row = sheet.createRow(r++);
            c = 0;
            row.createCell(c++).setCellValue(auditDTO.getId());
            row.createCell(c++).setCellValue(auditDTO.getRole());
            row.createCell(c++).setCellValue(auditDTO.getOperationId());
            row.createCell(c++).setCellValue(auditDTO.getAuditAction());
            row.createCell(c++).setCellValue(auditDTO.getDate());
            row.createCell(c++).setCellValue(auditDTO.getUsername());

        }
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i, true);
        }
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        @SuppressWarnings("unchecked")
        AuditResultDTO auditResultDTO = (AuditResultDTO) model.get("auditResultDTO");

        List<AuditDTO> inputs = auditResultDTO.getInputs();
        List<AuditDTO> outputs = auditResultDTO.getOutputs();
        List<AuditDTO> orders = auditResultDTO.getOrders();
        List<AuditDTO> deliveryNotes = auditResultDTO.getDeliveryNotes();
        List<AuditDTO> supplyings = auditResultDTO.getSupplyings();

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
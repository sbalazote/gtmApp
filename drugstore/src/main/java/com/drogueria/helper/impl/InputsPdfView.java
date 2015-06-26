package com.drogueria.helper.impl;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drogueria.config.PropertyProvider;
import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Input;
import com.drogueria.model.InputDetail;
import com.drogueria.util.StringUtility;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;

public class InputsPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		@SuppressWarnings("unchecked")
		List<Input> inputs = (List<Input>) model.get("inputs");

		// Fonts
		Font fontHeader = new Font(Font.TIMES_ROMAN, 11f, Font.NORMAL, Color.BLACK);
		Font fontDetails = new Font(Font.TIMES_ROMAN, 8f, Font.NORMAL, Color.BLACK);

		String relativeWebPath = PropertyProvider.getInstance().getProp(PropertyProvider.LOGO);
		String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath.substring(1));
		Image logo = Image.getInstance(absoluteDiskPath);
		logo.scaleToFit(50f, 50f);
		logo.setAbsolutePosition(10f * 2.8346f, 200f * 2.8346f);

		for (Input input : inputs) {

			PdfPTable table = new PdfPTable(6); // 6 columna
			table.setWidthPercentage(95);
			table.setSpacingBefore(10f);

			table.setSpacingAfter(10f);
			float[] columnWidths = {2f, 4f, 3f, 2f, 3f, 1f};

			table.setWidths(columnWidths);

			//Encabezado

			PdfPCell productCodeHeader = new PdfPCell(new Paragraph("Cod."));
			PdfPCell productDescriptionHeader = new PdfPCell(new Paragraph("Descripcion"));
			PdfPCell productBatchHeader = new PdfPCell(new Paragraph("Lote"));
			PdfPCell productExpirationDateHeader = new PdfPCell(new Paragraph("Vto."));
			PdfPCell productSerialNumberHeader = new PdfPCell(new Paragraph("Serie"));
			PdfPCell productAmountHeader = new PdfPCell(new Paragraph("Cant."));

			productCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			productCodeHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			productDescriptionHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			productBatchHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			productExpirationDateHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			productSerialNumberHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
			productAmountHeader.setBorder(Rectangle.BOTTOM | Rectangle.TOP);

			table.addCell(productCodeHeader);
			table.addCell(productDescriptionHeader);
			table.addCell(productBatchHeader);
			table.addCell(productExpirationDateHeader);
			table.addCell(productSerialNumberHeader);
			table.addCell(productAmountHeader);

			// add text at an absolute position
			PdfContentByte cb = writer.getDirectContent();
			cb.beginText();
			BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);
			cb.setFontAndSize(bf_times, 11f);
			cb.setTextMatrix(230 * 2.8346f, 200 * 2.8346f);
			cb.showText("Text at position 100, 300.");
			cb.endText();

			document.add(logo);

			document.add(new Chunk("Fecha: "));
			Chunk date = new Chunk(dateFormatter.format(input.getDate()), fontHeader);
			document.add(date);
			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Ingreso Nro.: "));
			Chunk id = new Chunk(StringUtility.addLeadingZeros(input.getId().toString(), 8), fontHeader);
			document.add(id);
			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Doc. Nro.: "));
			String POS = input.getDeliveryNoteNumber().substring(0, 4);
			String number = input.getDeliveryNoteNumber().substring(4, 12);
			Chunk deliveryNoteNumber = new Chunk("R" + POS + "-" + number , fontHeader);
			document.add(deliveryNoteNumber);
			document.add(Chunk.NEWLINE);

			LineSeparator ls = new LineSeparator();
			document.add(new Chunk(ls));

			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Convenio: "));
			Chunk description = new Chunk(input.getAgreement().getCode() + " - " + input.getAgreement().getDescription(), fontHeader);
			document.add(description);
			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Cliente/Proveedor: "));
			Chunk active = new Chunk(input.getClientOrProviderDescription(), fontHeader);
			document.add(active);
			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Concepto: "));
			Chunk code = new Chunk(input.getConcept().getCode() + " - " + input.getConcept().getDescription(), fontHeader);
			document.add(code);
			document.add(Chunk.NEWLINE);

			document.add(new Chunk("Orden de Compra: "));
			Chunk purchaseNumber = new Chunk(input.getPurchaseOrderNumber(), fontHeader);
			document.add(purchaseNumber);
			document.add(Chunk.NEWLINE);

			for (InputDetail inputDetail : input.getInputDetails()) {
				PdfPCell productCodeDetail = new PdfPCell(new Paragraph(String.valueOf(inputDetail.getProduct().getCode()), fontDetails));
				PdfPCell productDescriptionDetail = new PdfPCell(new Paragraph(inputDetail.getProduct().getDescription(), fontDetails));
				PdfPCell productBatchDetail = new PdfPCell(new Paragraph(inputDetail.getBatch(), fontDetails));
				PdfPCell productExpirationDateDetail = (new PdfPCell(new Paragraph(dateFormatter.format(inputDetail.getExpirationDate()), fontDetails)));
				PdfPCell productSerialNumberDetail = new PdfPCell(new Paragraph(inputDetail.getSerialNumber(), fontDetails));
				PdfPCell productAmountDetail = new PdfPCell(new Paragraph(String.valueOf(inputDetail.getAmount()), fontDetails));

				productCodeDetail.setBorder(Rectangle.NO_BORDER);
				productDescriptionDetail.setBorder(Rectangle.NO_BORDER);
				productBatchDetail.setBorder(Rectangle.NO_BORDER);
				productExpirationDateDetail.setBorder(Rectangle.NO_BORDER);
				productSerialNumberDetail.setBorder(Rectangle.NO_BORDER);
				productAmountDetail.setBorder(Rectangle.NO_BORDER);

				table.addCell(productCodeDetail);
				table.addCell(productDescriptionDetail);
				table.addCell(productBatchDetail);
				table.addCell(productExpirationDateDetail);
				table.addCell(productSerialNumberDetail);
				table.addCell(productAmountDetail);
			}
			document.add(table);
			document.newPage();
		}
	}
}

package com.drogueria.helper.impl;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Input;
import com.drogueria.model.InputDetail;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

public class InputsPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		@SuppressWarnings("unchecked")
		List<Input> inputs = (List<Input>) model.get("inputs");

		// Fonts
		Font fontTitle = new Font(2, 14, Font.BOLD, Color.BLACK);
		Font fontTag = new Font(2, 10, Font.BOLD, Color.WHITE);

		for (Input input : inputs) {

			// 1.ID
			document.add(new Chunk("ID: "));
			Chunk id = new Chunk(input.getId().toString(), fontTitle);
			document.add(id);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 2.CONCEPTO
			document.add(new Chunk("CONCEPTO: "));
			Chunk code = new Chunk(input.getConcept().getDescription(), fontTitle);
			document.add(code);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 3.CONVENIO
			document.add(new Chunk("CONVENIO: "));
			Chunk description = new Chunk(input.getAgreement().getDescription(), fontTitle);
			document.add(description);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 4.CLIENTE/PROVEEDOR
			document.add(new Chunk("CLIENTE/PROVEEDOR: "));
			Chunk active = new Chunk(input.getClientOrProviderDescription(), fontTitle);
			document.add(active);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 5.FECHA
			document.add(new Chunk("FECHA: "));
			Chunk date = new Chunk(dateFormatter.format(input.getDate()), fontTitle);
			document.add(date);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// -- newline
			document.add(Chunk.NEWLINE);

			// 7.ANULADO
			document.add(new Chunk("ANULADO: "));
			Chunk cancelled = new Chunk(input.isCancelled() ? "SI" : "NO", fontTitle);
			document.add(cancelled);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 8.NUMERO DE REMITO
			document.add(new Chunk("NUMERO DE REMITO: "));
			Chunk deliveryNoteNumber = new Chunk(input.getDeliveryNoteNumber(), fontTitle);
			document.add(deliveryNoteNumber);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 9.NUMERO DE ORDEN
			document.add(new Chunk("NUMERO DE ORDEN: "));
			Chunk purchaseNumber = new Chunk(input.getPurchaseOrderNumber(), fontTitle);
			document.add(purchaseNumber);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);
			for (InputDetail inputDetail : input.getInputDetails()) {
				// 10.PRODUCTO
				document.add(new Chunk("PRODUCTO: "));
				Chunk product = new Chunk(inputDetail.getProduct().getCode() + " - " + inputDetail.getProduct().getDescription(), fontTitle);
				document.add(product);
				document.add(new Chunk(" "));

				// -- newline
				document.add(Chunk.NEWLINE);

				// 11.GTIN
				document.add(new Chunk("GTIN: "));
				Chunk gtin = new Chunk(inputDetail.getGtin() != null ? inputDetail.getGtin().getNumber() : "", fontTitle);
				document.add(gtin);
				document.add(new Chunk(" "));

				// -- newline
				document.add(Chunk.NEWLINE);

				// 12.NUMERO DE SERIE
				document.add(new Chunk("NUMERO DE SERIE: "));
				Chunk serialNumber = new Chunk(inputDetail.getSerialNumber() != null ? inputDetail.getSerialNumber() : "", fontTitle);
				document.add(serialNumber);
				document.add(new Chunk(" "));

				// -- newline
				document.add(Chunk.NEWLINE);

				// 13.LOTE
				document.add(new Chunk("NUMERO DE SERIE: "));
				Chunk batch = new Chunk(inputDetail.getBatch(), fontTitle);
				document.add(batch);
				document.add(new Chunk(" "));

				// -- newline
				document.add(Chunk.NEWLINE);

				// 14.VTO
				document.add(new Chunk("VENCIMIENTO: "));
				Chunk expirateDate = new Chunk(dateFormatter.format(inputDetail.getExpirationDate()), fontTitle);
				document.add(expirateDate);
				document.add(new Chunk(" "));

                // -- newline
                document.add(Chunk.NEWLINE);

                // 6.CODIGO TRANSC. ANMAT
                document.add(new Chunk("CODIGO TRANSC. ANMAT: "));
                Chunk anmatCode = new Chunk(inputDetail.getTransactionCodeANMAT() != null ? inputDetail.getTransactionCodeANMAT() : "", fontTitle);
                document.add(anmatCode);
                document.add(new Chunk(" "));


                // -- newline
				document.add(Chunk.NEWLINE);

				// 15.CANTIDAD
				document.add(new Chunk("CANTIDAD: "));
				Chunk amount = new Chunk(dateFormatter.format(inputDetail.getAmount()), fontTitle);
				document.add(amount);
				document.add(new Chunk(" "));

				// -- newline
				document.add(Chunk.NEWLINE);
			}

			// -- newline
			document.add(Chunk.NEWLINE);

		}

	}
}

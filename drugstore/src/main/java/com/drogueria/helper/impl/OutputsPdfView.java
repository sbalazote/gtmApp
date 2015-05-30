package com.drogueria.helper.impl;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Output;
import com.drogueria.model.OutputDetail;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

public class OutputsPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		@SuppressWarnings("unchecked")
		List<Output> outputs = (List<Output>) model.get("outputs");

		// Fonts
		Font fontTitle = new Font(2, 14, Font.BOLD, Color.BLACK);
		Font fontTag = new Font(2, 10, Font.BOLD, Color.WHITE);

		for (Output output : outputs) {

			// 1.ID
			document.add(new Chunk("ID: "));
			Chunk id = new Chunk(output.getId().toString(), fontTitle);
			document.add(id);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 2.CONCEPTO
			document.add(new Chunk("CONCEPTO: "));
			Chunk code = new Chunk(output.getConcept().getDescription(), fontTitle);
			document.add(code);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 3.CONVENIO
			document.add(new Chunk("CONVENIO: "));
			Chunk description = new Chunk(output.getAgreement().getDescription(), fontTitle);
			document.add(description);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 4.CLIENTE/PROVEEDOR
			document.add(new Chunk("CLIENTE/PROVEEDOR: "));
			Chunk active = new Chunk(output.getClientOrProviderDescription(), fontTitle);
			document.add(active);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 5.FECHA
			document.add(new Chunk("FECHA: "));
			Chunk date = new Chunk(dateFormatter.format(output.getDate()), fontTitle);
			document.add(date);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 6.ANULADO
			document.add(new Chunk("ANULADO: "));
			Chunk cancelled = new Chunk(output.isCancelled() ? "SI" : "NO", fontTitle);
			document.add(cancelled);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			for (OutputDetail outputDetail : output.getOutputDetails()) {
				// 10.PRODUCTO
				document.add(new Chunk("PRODUCTO: "));
				Chunk product = new Chunk(outputDetail.getProduct().getCode() + " - " + outputDetail.getProduct().getDescription(), fontTitle);
				document.add(product);
				document.add(new Chunk(" "));

				// -- newline
				document.add(Chunk.NEWLINE);

				// 11.GTIN
				document.add(new Chunk("GTIN: "));
				Chunk gtin = new Chunk(outputDetail.getGtin() != null ? outputDetail.getGtin().getNumber() : "", fontTitle);
				document.add(gtin);
				document.add(new Chunk(" "));

				// -- newline
				document.add(Chunk.NEWLINE);

				// 12.NUMERO DE SERIE
				document.add(new Chunk("NUMERO DE SERIE: "));
				Chunk serialNumber = new Chunk(outputDetail.getSerialNumber() != null ? outputDetail.getSerialNumber() : "", fontTitle);
				document.add(serialNumber);
				document.add(new Chunk(" "));

				// -- newline
				document.add(Chunk.NEWLINE);

				// 13.LOTE
				document.add(new Chunk("LOTE: "));
				Chunk batch = new Chunk(outputDetail.getBatch(), fontTitle);
				document.add(batch);
				document.add(new Chunk(" "));

				// -- newline
				document.add(Chunk.NEWLINE);

				// 14.VTO
				document.add(new Chunk("VTO: "));
				Chunk expirateDate = new Chunk(dateFormatter.format(outputDetail.getExpirationDate()), fontTitle);
				document.add(expirateDate);
				document.add(new Chunk(" "));

				// -- newline
				document.add(Chunk.NEWLINE);

				// 15.CANTIDAD
				document.add(new Chunk("CANTIDAD: "));
				Chunk amount = new Chunk(dateFormatter.format(outputDetail.getAmount()), fontTitle);
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

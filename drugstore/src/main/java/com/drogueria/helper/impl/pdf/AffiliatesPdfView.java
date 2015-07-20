package com.drogueria.helper.impl.pdf;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Affiliate;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

public class AffiliatesPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		document.open();
		@SuppressWarnings("unchecked")
		List<Affiliate> affilates = (List<Affiliate>) model.get("affiliates");

		// Fonts
		Font fontTitle = new Font(2, 14, Font.BOLD, Color.BLACK);
		Font fontTag = new Font(2, 10, Font.BOLD, Color.WHITE);

		for (Affiliate affilate : affilates) {

			// 1.ID
			document.add(new Chunk("ID: "));
			Chunk id = new Chunk(affilate.getId().toString(), fontTitle);
			document.add(id);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 2.CODIGO
			document.add(new Chunk("CODIGO: "));
			Chunk code = new Chunk(affilate.getCode().toString(), fontTitle);
			document.add(code);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 3.NOMBRE
			document.add(new Chunk("NOMBRE: "));
			Chunk description = new Chunk(affilate.getName(), fontTitle);
			document.add(description);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 4.APELLIDO
			document.add(new Chunk("APELLIDO: "));
			Chunk input = new Chunk(affilate.getSurname(), fontTitle);
			document.add(input);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 5.TIPO DE DOCUMENTO
			document.add(new Chunk("TIPO DE DOCUMENTO: "));
			Chunk printDeliveryNote = new Chunk(affilate.getDocumentType() == null ? "-" : affilate.getDocumentType(), fontTitle);
			document.add(printDeliveryNote);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 6.NRO. DE DOCUMENTO
			document.add(new Chunk("NRO. DE DOCUMENTO: "));
			Chunk deliveryNoteCopies = new Chunk(affilate.getDocument() == null ? "-" : affilate.getDocument(), fontTitle);
			document.add(deliveryNoteCopies);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 7.CLIENTE
			document.add(new Chunk("CLIENTE: "));
			Chunk refund = new Chunk(affilate.getClient().getName(), fontTitle);
			document.add(refund);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 8.ACTIVO
			document.add(new Chunk("ACTIVO: "));
			Chunk active = new Chunk(affilate.isActive() ? "SI" : "NO", fontTitle);
			document.add(active);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// -- newline
			document.add(Chunk.NEWLINE);

		}

	}

}
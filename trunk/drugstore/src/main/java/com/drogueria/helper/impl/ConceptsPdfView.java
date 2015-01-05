package com.drogueria.helper.impl;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Concept;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

public class ConceptsPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		@SuppressWarnings("unchecked")
		List<Concept> concepts = (List<Concept>) model.get("concepts");

		// Fonts
		Font fontTitle = new Font(2, 14, Font.BOLD, Color.BLACK);
		Font fontTag = new Font(2, 10, Font.BOLD, Color.WHITE);

		for (Concept concept : concepts) {

			// 1.ID
			document.add(new Chunk("ID: "));
			Chunk id = new Chunk(concept.getId().toString(), fontTitle);
			document.add(id);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 2.CODIGO
			document.add(new Chunk("CODIGO: "));
			Chunk code = new Chunk(concept.getCode().toString(), fontTitle);
			document.add(code);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 3.DESCRIPCION
			document.add(new Chunk("DESCRIPCION: "));
			Chunk description = new Chunk(concept.getDescription(), fontTitle);
			document.add(description);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 4.ES INGRESO
			document.add(new Chunk("ES INGRESO: "));
			Chunk input = new Chunk(concept.isInput() ? "SI" : "NO", fontTitle);
			document.add(input);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 5.IMPRIME REMITO
			document.add(new Chunk("IMPRIME REMITO: "));
			Chunk printDeliveryNote = new Chunk(concept.isPrintDeliveryNote() ? "SI" : "NO", fontTitle);
			document.add(printDeliveryNote);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 6.NRO. COPIAS REMITO
			document.add(new Chunk("NRO. COPIAS REMITO: "));
			Chunk deliveryNoteCopies = new Chunk(concept.getDeliveryNoteCopies().toString(), fontTitle);
			document.add(deliveryNoteCopies);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 7.ES DEVOLUCION
			document.add(new Chunk("ES DEVOLUCION: "));
			Chunk refund = new Chunk(concept.isRefund() ? "SI" : "NO", fontTitle);
			document.add(refund);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 8.INFORMA ANMAT
			document.add(new Chunk("INFORMA ANMAT: "));
			Chunk informAnmat = new Chunk(concept.isInformAnmat() ? "SI" : "NO", fontTitle);
			document.add(informAnmat);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 9.ACTIVO
			document.add(new Chunk("ACTIVO: "));
			Chunk active = new Chunk(concept.isActive() ? "SI" : "NO", fontTitle);
			document.add(active);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 10.ES CLIENTE
			document.add(new Chunk("ES CLIENTE: "));
			Chunk client = new Chunk(concept.isClient() ? "SI" : "NO", fontTitle);
			document.add(client);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// -- newline
			document.add(Chunk.NEWLINE);

		}

	}

}
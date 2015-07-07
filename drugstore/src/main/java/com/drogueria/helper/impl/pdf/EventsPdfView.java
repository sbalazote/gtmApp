package com.drogueria.helper.impl.pdf;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Event;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

public class EventsPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		@SuppressWarnings("unchecked")
		List<Event> events = (List<Event>) model.get("events");

		// Fonts
		Font fontTitle = new Font(2, 14, Font.BOLD, Color.BLACK);
		Font fontTag = new Font(2, 10, Font.BOLD, Color.WHITE);

		for (Event event : events) {

			// 1.ID
			document.add(new Chunk("ID: "));
			Chunk id = new Chunk(event.getId().toString(), fontTitle);
			document.add(id);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 2.CODIGO
			document.add(new Chunk("CODIGO: "));
			Chunk code = new Chunk(event.getCode().toString(), fontTitle);
			document.add(code);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 3.DESCRIPCION
			document.add(new Chunk("DESCRIPCION: "));
			Chunk description = new Chunk(event.getDescription(), fontTitle);
			document.add(description);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 4.AGENTE ORIGEN
			document.add(new Chunk("AGENTE ORIGEN: "));
			Chunk originAgent = new Chunk(event.getOriginAgent().getDescription(), fontTitle);
			document.add(originAgent);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 5.AGENTE DESTINO
			document.add(new Chunk("DESTINO: "));
			Chunk destinationAgent = new Chunk(event.getDestinationAgent().getDescription(), fontTitle);
			document.add(destinationAgent);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 6.ACTIVO
			document.add(new Chunk("ACTIVO: "));
			Chunk active = new Chunk(event.isActive() ? "SI" : "NO", fontTitle);
			document.add(active);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// -- newline
			document.add(Chunk.NEWLINE);

		}

	}

}
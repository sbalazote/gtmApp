package com.drogueria.helper.impl;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Audit;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

public class AuditPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		@SuppressWarnings("unchecked")
		List<Audit> audits = (List<Audit>) model.get("audits");

		// Fonts
		Font fontTitle = new Font(2, 14, Font.BOLD, Color.BLACK);
		Font fontTag = new Font(2, 10, Font.BOLD, Color.WHITE);

		for (Audit audit : audits) {

			// 1.FECHA
			document.add(new Chunk("FECHA: "));
			Chunk date = new Chunk(dateFormatter.format(audit.getDate()), fontTitle);
			document.add(date);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 2.ROL
			document.add(new Chunk("ROL: "));
			Chunk role = new Chunk(audit.getRole().getDescription(), fontTitle);
			document.add(role);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 3.OPERACION
			document.add(new Chunk("OPERACION: "));
			Chunk description = new Chunk(audit.getOperationId().toString(), fontTitle);
			document.add(description);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 4.ACCION
			document.add(new Chunk("ACCION: "));
			Chunk action = new Chunk(audit.getAuditAction().getDescription(), fontTitle);
			document.add(action);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 5.USUARIO
			document.add(new Chunk("USUARIO: "));
			Chunk user = new Chunk(audit.getUser().getName(), fontTitle);
			document.add(user);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// -- newline
			document.add(Chunk.NEWLINE);

		}

	}

}
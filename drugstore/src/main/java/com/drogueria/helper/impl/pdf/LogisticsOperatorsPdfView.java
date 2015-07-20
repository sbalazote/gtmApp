package com.drogueria.helper.impl.pdf;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.LogisticsOperator;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

public class LogisticsOperatorsPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		document.open();
		@SuppressWarnings("unchecked")
		List<LogisticsOperator> logisticsOperators = (List<LogisticsOperator>) model.get("logisticsOperators");

		// Fonts
		Font fontTitle = new Font(2, 14, Font.BOLD, Color.BLACK);
		Font fontTag = new Font(2, 10, Font.BOLD, Color.WHITE);

		for (LogisticsOperator logisticsOperator : logisticsOperators) {

			// 1.ID
			document.add(new Chunk("ID: "));
			Chunk id = new Chunk(logisticsOperator.getId().toString(), fontTitle);
			document.add(id);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 2.CODIGO
			document.add(new Chunk("CODIGO: "));
			Chunk code = new Chunk(logisticsOperator.getCode().toString(), fontTitle);
			document.add(code);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 3.NOMBRE
			document.add(new Chunk("NOMBRE: "));
			Chunk name = new Chunk(logisticsOperator.getName(), fontTitle);
			document.add(name);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 4.CUIT
			document.add(new Chunk("CUIT: "));
			Chunk taxId = new Chunk(logisticsOperator.getTaxId(), fontTitle);
			document.add(taxId);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 5.RAZON SOCIAL
			document.add(new Chunk("RAZON SOCIAL: "));
			Chunk corporateName = new Chunk(logisticsOperator.getCorporateName(), fontTitle);
			document.add(corporateName);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 6.PROVINCIA
			document.add(new Chunk("PROVINCIA: "));
			Chunk province = new Chunk(logisticsOperator.getProvince().getName(), fontTitle);
			document.add(province);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 7.LOCALIDAD
			document.add(new Chunk("LOCALIDAD: "));
			Chunk locality = new Chunk(logisticsOperator.getLocality(), fontTitle);
			document.add(locality);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 8.DIRECCION
			document.add(new Chunk("DIRECCION: "));
			Chunk address = new Chunk(logisticsOperator.getAddress(), fontTitle);
			document.add(address);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 9.COD. POSTAL
			document.add(new Chunk("COD. POSTAL: "));
			Chunk zipCode = new Chunk(logisticsOperator.getZipCode(), fontTitle);
			document.add(zipCode);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 10.TELEFONO
			document.add(new Chunk("TELEFONO: "));
			Chunk phone = new Chunk(logisticsOperator.getPhone() == null ? "-" : logisticsOperator.getPhone(), fontTitle);
			document.add(phone);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 11.ACTIVO
			document.add(new Chunk("ACTIVO: "));
			Chunk active = new Chunk(logisticsOperator.isActive() ? "SI" : "NO", fontTitle);
			document.add(active);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// -- newline
			document.add(Chunk.NEWLINE);

		}

	}

}
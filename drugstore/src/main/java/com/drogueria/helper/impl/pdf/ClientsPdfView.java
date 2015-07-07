package com.drogueria.helper.impl.pdf;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Client;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

public class ClientsPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		@SuppressWarnings("unchecked")
		List<Client> clients = (List<Client>) model.get("clients");

		// Fonts
		Font fontTitle = new Font(2, 14, Font.BOLD, Color.BLACK);
		Font fontTag = new Font(2, 10, Font.BOLD, Color.WHITE);

		for (Client client : clients) {

			// 1.ID
			document.add(new Chunk("ID: "));
			Chunk id = new Chunk(client.getId().toString(), fontTitle);
			document.add(id);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 2.CODIGO
			document.add(new Chunk("CODIGO: "));
			Chunk code = new Chunk(client.getCode().toString(), fontTitle);
			document.add(code);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 3.NOMBRE
			document.add(new Chunk("NOMBRE: "));
			Chunk name = new Chunk(client.getName(), fontTitle);
			document.add(name);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 4.CUIT
			document.add(new Chunk("CUIT: "));
			Chunk taxId = new Chunk(client.getTaxId(), fontTitle);
			document.add(taxId);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 5.RAZON SOCIAL
			document.add(new Chunk("RAZON SOCIAL: "));
			Chunk corporateName = new Chunk(client.getCorporateName(), fontTitle);
			document.add(corporateName);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 6.PROVINCIA
			document.add(new Chunk("PROVINCIA: "));
			Chunk province = new Chunk(client.getProvince().getName(), fontTitle);
			document.add(province);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 7.LOCALIDAD
			document.add(new Chunk("LOCALIDAD: "));
			Chunk locality = new Chunk(client.getLocality(), fontTitle);
			document.add(locality);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 8.DIRECCION
			document.add(new Chunk("DIRECCION: "));
			Chunk address = new Chunk(client.getAddress(), fontTitle);
			document.add(address);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 9.COD. POSTAL
			document.add(new Chunk("COD. POSTAL: "));
			Chunk zipCode = new Chunk(client.getZipCode(), fontTitle);
			document.add(zipCode);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 10.COD. POSTAL
			document.add(new Chunk("COD. POSTAL: "));
			Chunk VATLiability = new Chunk(client.getVATLiability().getDescription(), fontTitle);
			document.add(VATLiability);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 11.TELEFONO
			document.add(new Chunk("TELEFONO: "));
			Chunk phone = new Chunk(client.getPhone() == null ? "-" : client.getPhone(), fontTitle);
			document.add(phone);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 12.ACTIVO
			document.add(new Chunk("ACTIVO: "));
			Chunk active = new Chunk(client.isActive() ? "SI" : "NO", fontTitle);
			document.add(active);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// -- newline
			document.add(Chunk.NEWLINE);

		}

	}

}
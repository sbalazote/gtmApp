package com.drogueria.helper.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Provider;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

public class ProvidersPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		@SuppressWarnings("unchecked")
		ArrayList<Provider> providers = (ArrayList<Provider>) model.get("providers");

		// Fonts
		Font fontTitle = new Font(2, 14, Font.BOLD, Color.BLACK);
		Font fontTag = new Font(2, 10, Font.BOLD, Color.WHITE);

		for (Provider provider : providers) {

			// 1.ID
			document.add(new Chunk("ID: "));
			Chunk id = new Chunk(provider.getId().toString(), fontTitle);
			document.add(id);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 2.CODIGO
			document.add(new Chunk("CODIGO: "));
			Chunk code = new Chunk(provider.getCode().toString(), fontTitle);
			document.add(code);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 3.NOMBRE
			document.add(new Chunk("NOMBRE: "));
			Chunk name = new Chunk(provider.getName(), fontTitle);
			document.add(name);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 4.CUIT
			document.add(new Chunk("CUIT: "));
			Chunk taxId = new Chunk(provider.getTaxId(), fontTitle);
			document.add(taxId);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 5.RAZON SOCIAL
			document.add(new Chunk("RAZON SOCIAL: "));
			Chunk corporateName = new Chunk(provider.getCorporateName(), fontTitle);
			document.add(corporateName);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 6.PROVINCIA
			document.add(new Chunk("PROVINCIA: "));
			Chunk province = new Chunk(provider.getProvince().getName(), fontTitle);
			document.add(province);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 7.LOCALIDAD
			document.add(new Chunk("LOCALIDAD: "));
			Chunk locality = new Chunk(provider.getLocality(), fontTitle);
			document.add(locality);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 8.DIRECCION
			document.add(new Chunk("DIRECCION: "));
			Chunk address = new Chunk(provider.getAddress(), fontTitle);
			document.add(address);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 9.COD. POSTAL
			document.add(new Chunk("COD. POSTAL: "));
			Chunk zipCode = new Chunk(provider.getZipCode(), fontTitle);
			document.add(zipCode);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 10.TIPO DE IVA
			document.add(new Chunk("TIPO DE IVA: "));
			Chunk VATLiability = new Chunk(provider.getVATLiability().getDescription(), fontTitle);
			document.add(VATLiability);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 11.TELEFONO
			document.add(new Chunk("TELEFONO: "));
			Chunk phone = new Chunk(provider.getPhone() == null ? "-" : provider.getPhone(), fontTitle);
			document.add(phone);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 12.MAIL
			document.add(new Chunk("MAIL: "));
			Chunk mail = new Chunk(provider.getMail() == null ? "-" : provider.getMail(), fontTitle);
			document.add(mail);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 13.GLN
			document.add(new Chunk("GLN: "));
			Chunk gln = new Chunk(provider.getGln(), fontTitle);
			document.add(gln);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 14.AGENTE
			document.add(new Chunk("AGENTE: "));
			Chunk agent = new Chunk(provider.getAgent().getDescription(), fontTitle);
			document.add(agent);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 15.TIPO
			document.add(new Chunk("TIPO: "));
			Chunk type = new Chunk(provider.getType().getDescription(), fontTitle);
			document.add(type);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 16.ACTIVO
			document.add(new Chunk("ACTIVO: "));
			Chunk active = new Chunk(provider.isActive() ? "SI" : "NO", fontTitle);
			document.add(active);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// -- newline
			document.add(Chunk.NEWLINE);

		}

	}

}
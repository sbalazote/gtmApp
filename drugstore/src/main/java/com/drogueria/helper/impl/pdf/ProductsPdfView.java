package com.drogueria.helper.impl.pdf;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Product;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

public class ProductsPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		document.open();
		@SuppressWarnings("unchecked")
		ArrayList<Product> products = (ArrayList<Product>) model.get("products");

		// Fonts
		Font fontTitle = new Font(2, 14, Font.BOLD, Color.BLACK);
		Font fontTag = new Font(2, 10, Font.BOLD, Color.WHITE);

		for (Product product : products) {

			// 1.ID
			document.add(new Chunk("ID: "));
			Chunk id = new Chunk(product.getId().toString(), fontTitle);
			document.add(id);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 2.CODIGO
			document.add(new Chunk("CODIGO: "));
			Chunk code = new Chunk(product.getCode().toString(), fontTitle);
			document.add(code);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 3.DESCRIPCION
			document.add(new Chunk("DESCRIPCION: "));
			Chunk description = new Chunk(product.getDescription(), fontTitle);
			document.add(description);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 4.MARCA
			document.add(new Chunk("MARCA: "));
			Chunk brand = new Chunk(product.getBrand().getDescription(), fontTitle);
			document.add(brand);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 5.MONODROGA
			document.add(new Chunk("MONODROGA: "));
			Chunk monodrug = new Chunk(product.getMonodrug().getDescription(), fontTitle);
			document.add(monodrug);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 6.AGRUPACION
			document.add(new Chunk("AGRUPACION: "));
			Chunk group = new Chunk(product.getGroup().getDescription(), fontTitle);
			document.add(group);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 7.ACCION FARMACOLOGICA
			document.add(new Chunk("ACCION FARMACOLOGICA: "));
			Chunk drugCategory = new Chunk(product.getDrugCategory().getDescription(), fontTitle);
			document.add(drugCategory);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 8.FRIO
			document.add(new Chunk("FRIO: "));
			Chunk cold = new Chunk(product.isCold() ? "SI" : "NO", fontTitle);
			document.add(cold);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 9.INFORMA ANMAT
			document.add(new Chunk("INFORMA ANMAT: "));
			Chunk informAnmat = new Chunk(product.isInformAnmat() ? "SI" : "NO", fontTitle);
			document.add(informAnmat);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 10.TIPO
			document.add(new Chunk("TIPO: "));
			Chunk type = new Chunk(product.getType().equalsIgnoreCase("BE") ? "LOTE/VTO" : product.getType().equalsIgnoreCase("PS") ? "TRAZADO ORIGEN"
					: "TRAZADO PROPIO", fontTitle);
			document.add(type);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 11.ACTIVO
			document.add(new Chunk("ACTIVO: "));
			Chunk active = new Chunk(product.isActive() ? "SI" : "NO", fontTitle);
			document.add(active);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// -- newline
			document.add(Chunk.NEWLINE);

		}

	}

}
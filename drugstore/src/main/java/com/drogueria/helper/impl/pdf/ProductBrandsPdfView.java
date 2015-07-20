package com.drogueria.helper.impl.pdf;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.ProductBrand;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

public class ProductBrandsPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		document.open();
		@SuppressWarnings("unchecked")
		ArrayList<ProductBrand> productBrands = (ArrayList<ProductBrand>) model.get("productBrands");

		// Fonts
		Font fontTitle = new Font(2, 14, Font.BOLD, Color.BLACK);
		Font fontTag = new Font(2, 10, Font.BOLD, Color.WHITE);

		for (ProductBrand productBrand : productBrands) {

			// 1.ID
			document.add(new Chunk("ID: "));
			Chunk id = new Chunk(productBrand.getId().toString(), fontTitle);
			document.add(id);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 2.CODIGO
			document.add(new Chunk("CODIGO: "));
			Chunk code = new Chunk(productBrand.getCode().toString(), fontTitle);
			document.add(code);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 3.DESCRIPCION
			document.add(new Chunk("DESCRIPCION: "));
			Chunk description = new Chunk(productBrand.getDescription(), fontTitle);
			document.add(description);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 4.ACTIVO
			document.add(new Chunk("ACTIVO: "));
			Chunk active = new Chunk(productBrand.isActive() ? "SI" : "NO", fontTitle);
			document.add(active);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// -- newline
			document.add(Chunk.NEWLINE);

		}

	}

}
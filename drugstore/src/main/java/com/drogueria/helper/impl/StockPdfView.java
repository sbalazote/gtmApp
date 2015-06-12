package com.drogueria.helper.impl;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Stock;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

public class StockPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		@SuppressWarnings("unchecked")
		List<Stock> stockList = (List<Stock>) model.get("stocks");

		// Fonts
		Font fontTitle = new Font(2, 14, Font.BOLD, Color.BLACK);
		Font fontTag = new Font(2, 10, Font.BOLD, Color.WHITE);

		for (Stock stock : stockList) {

			// 1.PRODUCTO
			document.add(new Chunk("PRODUCTO: "));
			Chunk id = new Chunk(stock.getProduct().getCode() + " - " + stock.getProduct().getDescription(), fontTitle);
			document.add(id);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 2.GTIN:
			document.add(new Chunk("GTIN: "));
			Chunk code = new Chunk(stock.getGtin() != null ? stock.getGtin().getNumber() : "");
			document.add(code);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 3.CONVENIO
			document.add(new Chunk("CONVENIO: "));
			Chunk description = new Chunk(stock.getAgreement().getDescription(), fontTitle);
			document.add(description);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 4.SERIE
			document.add(new Chunk("SERIE: "));
			Chunk active = new Chunk(stock.getSerialNumber() != null ? stock.getSerialNumber() : "", fontTitle);
			document.add(active);
			document.add(new Chunk(" "));

			// 5.LOTE
			document.add(new Chunk("LOTE: "));
			Chunk batch = new Chunk(stock.getBatch(), fontTitle);
			document.add(batch);
			document.add(new Chunk(" "));

			// 6.VTO
			document.add(new Chunk("VTO: "));
			Chunk expirateDate = new Chunk(stock.getExpirationDate().toString(), fontTitle);
			document.add(expirateDate);
			document.add(new Chunk(" "));

			// 7.CANTIDAD
			document.add(new Chunk("CANTIDAD: "));
			Chunk amount = new Chunk(stock.getAmount().toString(), fontTitle);
			document.add(amount);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// -- newline
			document.add(Chunk.NEWLINE);

		}

	}
}

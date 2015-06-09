package com.drogueria.helper.impl;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.Supplying;
import com.drogueria.model.SupplyingDetail;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

public class SupplyingsPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		@SuppressWarnings("unchecked")
		List<Supplying> supplyings = (List<Supplying>) model.get("supplyings");

		// Fonts
		Font fontTitle = new Font(2, 14, Font.BOLD, Color.BLACK);
		Font fontTag = new Font(2, 10, Font.BOLD, Color.WHITE);

		for (Supplying supplying : supplyings) {

			// 1.ID
			document.add(new Chunk("ID: "));
			Chunk id = new Chunk(supplying.getId().toString(), fontTitle);
			document.add(id);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 2.CONVENIO
			document.add(new Chunk("CONVENIO: "));
			Chunk description = new Chunk(supplying.getAgreement().getDescription(), fontTitle);
			document.add(description);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 3.CLIENTE
			document.add(new Chunk("CLIENTE/PROVEEDOR: "));
			Chunk active = new Chunk(supplying.getClient().getCorporateName(), fontTitle);
			document.add(active);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 4.FECHA
			document.add(new Chunk("FECHA: "));
			Chunk date = new Chunk(dateFormatter.format(supplying.getDate()), fontTitle);
			document.add(date);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 5.ANULADO
			document.add(new Chunk("ANULADO: "));
			Chunk cancelled = new Chunk(supplying.isCancelled() ? "SI" : "NO", fontTitle);
			document.add(cancelled);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			for (SupplyingDetail supplyingDetail : supplying.getSupplyingDetails()) {
				// 6.PRODUCTO
				document.add(new Chunk("PRODUCTO: "));
				Chunk product = new Chunk(supplyingDetail.getProduct().getCode() + " - " + supplyingDetail.getProduct().getDescription(), fontTitle);
				document.add(product);
				document.add(new Chunk(" "));

				// -- newline
				document.add(Chunk.NEWLINE);

				// 7.GTIN
				document.add(new Chunk("GTIN: "));
				Chunk gtin = new Chunk(supplyingDetail.getGtin() != null ? supplyingDetail.getGtin().getNumber() : "", fontTitle);
				document.add(gtin);
				document.add(new Chunk(" "));

				// -- newline
				document.add(Chunk.NEWLINE);

				// 8.NUMERO DE SERIE
				document.add(new Chunk("NUMERO DE SERIE: "));
				Chunk serialNumber = new Chunk(supplyingDetail.getSerialNumber() != null ? supplyingDetail.getSerialNumber() : "", fontTitle);
				document.add(serialNumber);
				document.add(new Chunk(" "));

				// -- newline
				document.add(Chunk.NEWLINE);

				// 9.LOTE
				document.add(new Chunk("LOTE: "));
				Chunk batch = new Chunk(supplyingDetail.getBatch(), fontTitle);
				document.add(batch);
				document.add(new Chunk(" "));

				// -- newline
				document.add(Chunk.NEWLINE);

				// 10.VTO
				document.add(new Chunk("VTO: "));
				Chunk expirateDate = new Chunk(dateFormatter.format(supplyingDetail.getExpirationDate()), fontTitle);
				document.add(expirateDate);
				document.add(new Chunk(" "));

				// -- newline
				document.add(Chunk.NEWLINE);

				// 11.CANTIDAD
				document.add(new Chunk("CANTIDAD: "));
				Chunk amount = new Chunk(dateFormatter.format(supplyingDetail.getAmount()), fontTitle);
				document.add(amount);
				document.add(new Chunk(" "));

				// -- newline
				document.add(Chunk.NEWLINE);
			}

			// -- newline
			document.add(Chunk.NEWLINE);

		}

	}
}

package com.drogueria.helper.impl;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drogueria.helper.AbstractPdfView;
import com.drogueria.model.ProvisioningRequest;
import com.drogueria.model.ProvisioningRequestDetail;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

public class ProvisioningPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		@SuppressWarnings("unchecked")
		List<ProvisioningRequest> provisionings = (List<ProvisioningRequest>) model.get("provisionings");

		// Fonts
		Font fontTitle = new Font(2, 14, Font.BOLD, Color.BLACK);
		Font fontTag = new Font(2, 10, Font.BOLD, Color.WHITE);

		for (ProvisioningRequest provisioningRequest : provisionings) {

			// 1.ID
			document.add(new Chunk("ID: "));
			Chunk id = new Chunk(provisioningRequest.getId().toString(), fontTitle);
			document.add(id);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 3.CONVENIO
			document.add(new Chunk("CONVENIO: "));
			Chunk description = new Chunk(provisioningRequest.getAgreement().getDescription(), fontTitle);
			document.add(description);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 4.CLIENTE
			document.add(new Chunk("CLIENTE: "));
			Chunk active = new Chunk(provisioningRequest.getClient().getName(), fontTitle);
			document.add(active);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 5.AFILIADO
			document.add(new Chunk("AFILIADO: "));
			Chunk affiliate = new Chunk(provisioningRequest.getAffiliate().getSurname() + " " + provisioningRequest.getAffiliate().getName(), fontTitle);
			document.add(affiliate);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 6.LUGAR DE ENTREGA
			document.add(new Chunk("LUGAR DE ENTREGA: "));
			Chunk deliveryLocation = new Chunk(provisioningRequest.getDeliveryLocation().getName(), fontTitle);
			document.add(deliveryLocation);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 7.FECHA DE ENTREGA
			document.add(new Chunk("FECHA DE ENTREGA: "));
			Chunk date = new Chunk(dateFormatter.format(provisioningRequest.getDeliveryDate()), fontTitle);
			document.add(date);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 8.OPERADOR LOGISTICO
			document.add(new Chunk("OPERADOR LOGISTICO: "));
			Chunk deliveryNoteNumber = new Chunk(
					provisioningRequest.getLogisticsOperator() != null ? provisioningRequest.getLogisticsOperator().getName() : "", fontTitle);
			document.add(deliveryNoteNumber);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 9.COMENTARIOS
			document.add(new Chunk("COMENTARIOS: "));
			Chunk comment = new Chunk(provisioningRequest.getComment() != null ? provisioningRequest.getComment() : "", fontTitle);
			document.add(comment);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 10.ESTADO
			document.add(new Chunk("ESTADO: "));
			Chunk state = new Chunk(provisioningRequest.getState().getDescription(), fontTitle);
			document.add(state);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);
			for (ProvisioningRequestDetail provisioningRequestDetail : provisioningRequest.getProvisioningRequestDetails()) {
				// 11.PRODUCTO
				document.add(new Chunk("PRODUCTO: "));
				Chunk product = new Chunk(provisioningRequestDetail.getProduct().getCode() + " - " + provisioningRequestDetail.getProduct().getDescription(),
						fontTitle);
				document.add(product);
				document.add(new Chunk(" "));

				// -- newline
				document.add(Chunk.NEWLINE);

				// 12.CANTIDAD
				document.add(new Chunk("CANTIDAD: "));
				Chunk amount = new Chunk(dateFormatter.format(provisioningRequestDetail.getAmount()), fontTitle);
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

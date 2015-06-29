package com.drogueria.helper;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.PageSize;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;

public abstract class AbstractPdfView extends AbstractView {

	public AbstractPdfView() {
		this.setContentType("application/pdf");
	}

	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	@Override
	protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
		String filename = request.getPathInfo().substring(1);

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=" + filename);
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// IE workaround: write into byte array first.
		ByteArrayOutputStream baos = this.createTemporaryOutputStream();

		// Apply preferences and build metadata.
		Document document = new Document(PageSize.A4.rotate());
		PdfWriter writer = PdfWriter.getInstance(document, baos);
		this.prepareWriter(model, writer, request);
		this.buildPdfMetadata(model, document, request);

		// Build PDF document.
		writer.setInitialLeading(16);
		document.open();
		document.add(new Chunk(""));
		this.buildPdfDocument(model, document, writer, request, response);
		document.close();

		response.resetBuffer();
		Cookie cookie = new Cookie("fileDownloadToken", request.getParameterValues("fileDownloadToken")[0]);
		cookie.setPath(request.getContextPath());
		response.addCookie(cookie);

		// Flush to HTTP response.
		this.writeToResponse(response, baos);

	}

	protected void prepareWriter(Map<String, Object> model, PdfWriter writer, HttpServletRequest request) throws DocumentException {
		writer.setViewerPreferences(this.getViewerPreferences());
	}

	protected int getViewerPreferences() {
		return PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage;
	}

	protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request) {
	}

	protected abstract void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception;
}
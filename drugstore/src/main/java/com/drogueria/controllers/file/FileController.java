package com.drogueria.controllers.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.drogueria.dto.AlfabetaFileDTO;
import com.drogueria.helper.FileMeta;
import com.drogueria.service.ProductService;

@Controller
public class FileController {

	@Autowired
	private ProductService productService;

	private static final Logger logger = Logger.getLogger(FileController.class);

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody FileMeta upload(MultipartHttpServletRequest request) {

		MultipartFile mpf = request.getFile("alfabetaUpdateFile");
		FileMeta fileMeta = null;

		if (!mpf.isEmpty()) {

			logger.info(mpf.getOriginalFilename() + " subido! ");

			fileMeta = new FileMeta();
			fileMeta.setFileName(mpf.getOriginalFilename());
			fileMeta.setFileSize(mpf.getSize() + " bytes");

			try {
				fileMeta.setBytes(mpf.getBytes());

				String path = request.getSession().getServletContext().getRealPath("/alfabeta/");
				new File(path).mkdirs();
				String timestamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(path + "/" + timestamp + ".dat"));

			} catch (IOException e) {
				logger.error(e.getMessage());
				throw new RuntimeException("No se ha podido subir el archivo " + request.getFile("alfabetaUpdateFile").toString(), e);
			}

		} else {
			logger.error("No se ha podido subir el archivo porque éste estaba vacío");
		}
		return fileMeta;
	}

	@RequestMapping(value = "/updateProducts", method = RequestMethod.POST)
	public @ResponseBody void updateProducts(HttpServletRequest request, @RequestBody AlfabetaFileDTO alfabetaFileDTO) throws Exception {

		String path = request.getSession().getServletContext().getRealPath("/alfabeta/");
		String timestamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

		BufferedReader brManualDat = null;
		String currentLineManualDat;

		brManualDat = new BufferedReader(new FileReader(path + "/" + timestamp + ".dat"));

		boolean result;
		try {
			while ((currentLineManualDat = brManualDat.readLine()) != null) {
				Integer priceFieldByteOffset = alfabetaFileDTO.getPriceFieldByteOffset();
				Integer priceFieldLength = alfabetaFileDTO.getPriceFieldLength();

				Integer codeFieldByteOffset = alfabetaFileDTO.getCodeFieldByteOffset();
				Integer codeFieldLength = alfabetaFileDTO.getCodeFieldLength();

				Integer gtinFieldByteOffset = alfabetaFileDTO.getGtinFieldByteOffset();
				Integer gtinFieldLength = alfabetaFileDTO.getGtinFieldLength();

				BigDecimal updatedPrice = new BigDecimal(currentLineManualDat.substring(priceFieldByteOffset, priceFieldByteOffset + priceFieldLength));
				Integer updatedCode = Integer.parseInt(currentLineManualDat.substring(codeFieldByteOffset, codeFieldByteOffset + codeFieldLength));
				String updatedGtin = currentLineManualDat.substring(gtinFieldByteOffset, gtinFieldByteOffset + gtinFieldLength);

				result = this.productService.updateFromAlfabeta(updatedCode, updatedGtin, updatedPrice);

				if (!result) {
					logger.error("Error en actualizacion alfabeta - Codigo de Producto: " + updatedCode);
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException("Error al procesar el archivo de actualizacion Alfabeta", e);
		}
		brManualDat.close();
	}
}
package com.drogueria.controllers.file;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
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

	public byte[] convertToPNG(byte[] imageInByte) {
		try {
		// read a jpeg from a inputFileInputStream
		InputStream in = new ByteArrayInputStream(imageInByte);
		BufferedImage bufferedImage = ImageIO.read(in);

		// write the bufferedImage back to outputFile
		//ImageIO.write(bufferedImage, "png", new File(outputFile));

		// this writes the bufferedImage into a byte array called resultingBytes
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", byteArrayOut);

			return byteArrayOut.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/uploadLogo", method = RequestMethod.POST)
	public @ResponseBody FileMeta uploadLogo(MultipartHttpServletRequest request) {

		MultipartFile mpf = request.getFile("uploadLogo");
		FileMeta fileMeta = null;

		if (!mpf.isEmpty()) {
			fileMeta = new FileMeta();
			fileMeta.setFileName(mpf.getOriginalFilename());
			fileMeta.setFileSize(mpf.getSize() + " bytes");

			try {
				fileMeta.setBytes(mpf.getBytes());

				String path = request.getSession().getServletContext().getRealPath("/images/");
				String name = mpf.getOriginalFilename();
				String extension = name.substring(name.lastIndexOf('.'));

				new File(path).mkdirs();

				byte[] imageBytes;
				if (!extension.equals(".png")) {
					imageBytes = convertToPNG(mpf.getBytes());
				} else{
					imageBytes = mpf.getBytes();
				}

				FileCopyUtils.copy(imageBytes, new FileOutputStream(path + "/logo.png"));
				logger.info("Logo subido! ");

			} catch (IOException e) {
				logger.error(e.getMessage());
				throw new RuntimeException("No se ha podido subir el logo " + request.getFile("uploadLogo").toString(), e);
			}

		} else {
			logger.error("No se ha podido subir el logo porque éste estaba vacío");
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

		try {
			while ((currentLineManualDat = brManualDat.readLine()) != null) {
				Integer nameFieldByteOffset = alfabetaFileDTO.getNameFieldByteOffset();
				Integer nameFieldLength = alfabetaFileDTO.getNameFieldLength();

				Integer presentationFieldByteOffset = alfabetaFileDTO.getPresentationFieldByteOffset();
				Integer presentationFieldLength = alfabetaFileDTO.getPresentationFieldLength();

				Integer priceFieldByteOffset = alfabetaFileDTO.getPriceFieldByteOffset();
				Integer priceFieldLength = alfabetaFileDTO.getPriceFieldLength();

				Integer codeFieldByteOffset = alfabetaFileDTO.getCodeFieldByteOffset();
				Integer codeFieldLength = alfabetaFileDTO.getCodeFieldLength();

				Integer gtinFieldByteOffset = alfabetaFileDTO.getGtinFieldByteOffset();
				Integer gtinFieldLength = alfabetaFileDTO.getGtinFieldLength();

				Integer coldFieldByteOffset = alfabetaFileDTO.getColdFieldByteOffset();
				Integer coldFieldLength = alfabetaFileDTO.getColdFieldLength();

				String updatedName = currentLineManualDat.substring(nameFieldByteOffset, nameFieldByteOffset + nameFieldLength);
				String updatedPresentation = currentLineManualDat.substring(presentationFieldByteOffset, presentationFieldByteOffset + presentationFieldLength);
				String updatedDescription = updatedName.trim().concat(" ").concat(updatedPresentation).trim();
				BigDecimal updatedPrice = new BigDecimal(currentLineManualDat.substring(priceFieldByteOffset, priceFieldByteOffset + priceFieldLength));
				Integer updatedCode = Integer.parseInt(currentLineManualDat.substring(codeFieldByteOffset, codeFieldByteOffset + codeFieldLength));
				String updatedGtin = currentLineManualDat.substring(gtinFieldByteOffset, gtinFieldByteOffset + gtinFieldLength);
				Boolean updatedCold = (currentLineManualDat.substring(coldFieldByteOffset, coldFieldByteOffset + coldFieldLength) == "1") ? true : false;

				this.productService.updateFromAlfabeta(updatedDescription, updatedPrice, updatedCode, updatedGtin, updatedCold);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException("Error al procesar el archivo de actualizacion Alfabeta", e);
		}
		brManualDat.close();
	}
}
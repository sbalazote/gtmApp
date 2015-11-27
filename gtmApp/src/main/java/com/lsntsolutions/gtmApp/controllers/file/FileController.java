package com.lsntsolutions.gtmApp.controllers.file;

import com.lsntsolutions.gtmApp.dto.AlfabetaFileDTO;
import com.lsntsolutions.gtmApp.dto.ImportStockDTO;
import com.lsntsolutions.gtmApp.dto.ProviderSerializedProductDTO;
import com.lsntsolutions.gtmApp.helper.FileMeta;
import com.lsntsolutions.gtmApp.helper.SerialParser;
import com.lsntsolutions.gtmApp.model.InputDetail;
import com.lsntsolutions.gtmApp.model.Product;
import com.lsntsolutions.gtmApp.model.ProductGtin;
import com.lsntsolutions.gtmApp.service.InputService;
import com.lsntsolutions.gtmApp.service.ProductGtinService;
import com.lsntsolutions.gtmApp.service.ProductService;
import com.lsntsolutions.gtmApp.util.OperationResult;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class FileController {

	@Autowired
	private ProductService productService;
	@Autowired
	private InputService inputService;
	@Autowired
	private SerialParser serialParser;
	@Autowired
	private ProductGtinService productGtinService;

	private static final Logger logger = Logger.getLogger(FileController.class);

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody
	FileMeta upload(MultipartHttpServletRequest request) {

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

				//String path = request.getSession().getServletContext().getRealPath("/images/");
				String path = request.getServletContext().getInitParameter("upload.location");

				String name = mpf.getOriginalFilename();
				String extension = name.substring(name.lastIndexOf('.'));

				new File(path).mkdirs();

				byte[] imageBytes;
				if (!extension.equals(".png")) {
					imageBytes = convertToPNG(mpf.getBytes());
				} else{
					imageBytes = mpf.getBytes();
				}

				FileCopyUtils.copy(imageBytes, new FileOutputStream(path + "/uploadedLogo.png"));
				FileCopyUtils.copy(imageBytes, new FileOutputStream(request.getSession().getServletContext().getRealPath("/images/") + "/uploadedLogo.png"));
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

		BufferedReader brManualDat;
		String currentLineManualDat;

		brManualDat = new BufferedReader(new FileReader(path + "/" + timestamp + ".dat"));
		String updatedName;
		String updatedPresentation;
		String updatedDescription;
		BigDecimal updatedPrice;
		Integer updatedCode;
		String updatedGtin;
		Boolean updatedCold;
		Integer nameFieldByteOffset;
		Integer nameFieldLength;
		Integer presentationFieldByteOffset;
		Integer presentationFieldLength;
		Integer priceFieldByteOffset;
		Integer priceFieldLength;
		Integer codeFieldByteOffset;
		Integer codeFieldLength;
		Integer gtinFieldByteOffset;
		Integer gtinFieldLength;
		Integer coldFieldByteOffset;
		Integer coldFieldLength;
		try {
			nameFieldByteOffset = alfabetaFileDTO.getNameFieldByteOffset();
			nameFieldLength= alfabetaFileDTO.getNameFieldLength();

			presentationFieldByteOffset= alfabetaFileDTO.getPresentationFieldByteOffset();
			presentationFieldLength= alfabetaFileDTO.getPresentationFieldLength();

			priceFieldByteOffset = alfabetaFileDTO.getPriceFieldByteOffset();
			priceFieldLength = alfabetaFileDTO.getPriceFieldLength();

			codeFieldByteOffset = alfabetaFileDTO.getCodeFieldByteOffset();
			codeFieldLength = alfabetaFileDTO.getCodeFieldLength();

			gtinFieldByteOffset = alfabetaFileDTO.getGtinFieldByteOffset();
			gtinFieldLength = alfabetaFileDTO.getGtinFieldLength();

			coldFieldByteOffset = alfabetaFileDTO.getColdFieldByteOffset();
			coldFieldLength = alfabetaFileDTO.getColdFieldLength();

			while ((currentLineManualDat = brManualDat.readLine()) != null) {
				updatedName = currentLineManualDat.substring(nameFieldByteOffset, nameFieldByteOffset + nameFieldLength);
				updatedPresentation = currentLineManualDat.substring(presentationFieldByteOffset, presentationFieldByteOffset + presentationFieldLength);
				updatedDescription = updatedName.trim().concat(" ").concat(updatedPresentation).trim();
				updatedPrice = new BigDecimal(currentLineManualDat.substring(priceFieldByteOffset, priceFieldByteOffset + priceFieldLength));
				updatedCode = Integer.parseInt(currentLineManualDat.substring(codeFieldByteOffset, codeFieldByteOffset + codeFieldLength));
				updatedGtin = currentLineManualDat.substring(gtinFieldByteOffset, gtinFieldByteOffset + gtinFieldLength);
				updatedCold = (currentLineManualDat.substring(coldFieldByteOffset, coldFieldByteOffset + coldFieldLength) == "1") ? true : false;

				this.productService.updateFromAlfabeta(updatedDescription, updatedPrice, updatedCode, updatedGtin, updatedCold);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException("Error al procesar el archivo de actualizacion Alfabeta", e);
		}
		brManualDat.close();
	}


	@RequestMapping(value = "/uploadStock", method = RequestMethod.POST)
	public @ResponseBody
	FileMeta uploadStock(MultipartHttpServletRequest request) {

		MultipartFile mpf = request.getFile("importStock");
		FileMeta fileMeta = null;

		if (!mpf.isEmpty()) {

			logger.info(mpf.getOriginalFilename() + " subido! ");

			fileMeta = new FileMeta();
			fileMeta.setFileName(mpf.getOriginalFilename());
			fileMeta.setFileSize(mpf.getSize() + " bytes");

			try {
				fileMeta.setBytes(mpf.getBytes());

				String path = request.getSession().getServletContext().getRealPath("/importStock/");
				new File(path).mkdirs();
				String timestamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(path + "/" + timestamp + ".xls"));

			} catch (IOException e) {
				logger.error(e.getMessage());
				throw new RuntimeException("No se ha podido subir el archivo " + request.getFile("alfabetaUpdateFile").toString(), e);
			}

		} else {
			logger.error("No se ha podido subir el archivo porque éste estaba vacío");
		}
		return fileMeta;
	}

	@RequestMapping(value = "/updateImportStock", method = RequestMethod.POST)
	public @ResponseBody
	OperationResult updateImportStock(HttpServletRequest request, @RequestBody ImportStockDTO importStockDTO) throws Exception {
		OperationResult operationResult = new OperationResult();
		String path = request.getSession().getServletContext().getRealPath("/importStock/");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String timestamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		FileInputStream fileInputStream = new FileInputStream(path + "/" + timestamp + ".xls");
		POIFSFileSystem fs = new POIFSFileSystem(fileInputStream);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row;

		int rows; // No of rows
		rows = sheet.getPhysicalNumberOfRows();
		List<InputDetail> inputDetails = new ArrayList<>();
		List<String> errors = new ArrayList<>();
		List<Product> productsToActivate = new ArrayList<>();
		List<Product> serializedProducts = new ArrayList<>();
		String error;
		for(int i = importStockDTO.getFirstRow(); i < rows; i++) {
			row = sheet.getRow(i);
			if(row != null) {
				InputDetail inputDetail = new InputDetail();
				String gtin = row.getCell(importStockDTO.getGtinColumn() - 1).getStringCellValue();
				Product product = this.productService.getByGtin(gtin, null);
				if(product != null){
					if(!product.isActive()){
						productsToActivate.add(product);
					}
					ProductGtin productGtin = this.productGtinService.getByNumber(gtin);
					inputDetail.setGtin(productGtin);
					inputDetail.setProduct(product);
					if(HSSFCell.CELL_TYPE_STRING == row.getCell(importStockDTO.getAmountColumn() - 1).getCellType()) {
						inputDetail.setAmount(Integer.valueOf(row.getCell(importStockDTO.getAmountColumn() - 1).getStringCellValue()));
					}else if(HSSFCell.CELL_TYPE_NUMERIC == row.getCell(importStockDTO.getAmountColumn() - 1).getCellType()){
						Double amount = new Double(row.getCell(importStockDTO.getAmountColumn() - 1).getNumericCellValue());
						inputDetail.setAmount(Integer.valueOf(amount.intValue()));
					}
					if(HSSFCell.CELL_TYPE_STRING == row.getCell(importStockDTO.getBatchColumn() - 1).getCellType()) {
						inputDetail.setBatch(row.getCell(importStockDTO.getBatchColumn() - 1).getStringCellValue());
					}else if(HSSFCell.CELL_TYPE_NUMERIC == row.getCell(importStockDTO.getBatchColumn() - 1).getCellType()){
						Double batch = new Double(row.getCell(importStockDTO.getBatchColumn() - 1).getNumericCellValue());
						inputDetail.setBatch(String.valueOf(batch));
					}

					DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					Date date = format.parse(row.getCell(importStockDTO.getExpirationColumn() - 1).getStringCellValue());
					inputDetail.setExpirationDate(date);
					String type = row.getCell(importStockDTO.getTypeColumn() - 1).getStringCellValue();
					String serial = "";
					if(HSSFCell.CELL_TYPE_STRING == row.getCell(importStockDTO.getSerialColumn() - 1).getCellType()) {
						serial = row.getCell(importStockDTO.getSerialColumn() - 1).getStringCellValue();
					}else if(HSSFCell.CELL_TYPE_NUMERIC == row.getCell(importStockDTO.getSerialColumn() - 1).getCellType()){
						Double serialNumber = new Double(row.getCell(importStockDTO.getSerialColumn() - 1).getNumericCellValue());
						serial = String.valueOf(serialNumber);
					}
					ProviderSerializedProductDTO parse = null;
					if(type.indexOf("S") == 0){
						if(serial.length() > 0) {
							if (type.indexOf("S1") == 0) {
								parse = this.serialParser.parse(serial);
							}
							if (type.indexOf("S2") == 0) {
								parse = this.serialParser.parseSelfSerial(serial);
							}
							if (parse != null) {
								inputDetail.setSerialNumber(parse.getSerialNumber());
							}else{
								error = "No se pudo obtener el serie para el producto con gtin: " + gtin;
								errors.add(error);
							}
						}else{
							error = "Producto con gtin: " + gtin + " no registra Serie, ubicado en la fila: " + row + 1;
							errors.add(error);
						}
						serializedProducts.add(product);
					}
					inputDetails.add(inputDetail);
				}else{
					error = "El producto asociado al GTIN: "  + gtin;
					errors.add(error);
				}
			}
		}
		operationResult.setMyOwnErrors(errors);
		if(errors.size() == 0){
			this.inputService.importStock(inputDetails, importStockDTO.getAgreementId(), importStockDTO.getConceptId(), importStockDTO.getProviderId(), auth.getName());
			for(Product product : productsToActivate){
				product.setActive(true);
				this.productService.save(product);
			}
			for(Product product : serializedProducts){
				product.setType("PS");
				this.productService.save(product);
			}
			operationResult.setResultado(true);
		}
		return operationResult;
	}
}
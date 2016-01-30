package com.lsntsolutions.gtmApp.zxing.encoder;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Dimension;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;
import com.google.zxing.oned.Code128Writer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author Sebastian E. Balazote Oliver
 */
public class EncoderTest {
	public static void main(String[] args) {
		BitMatrix bitMatrix;
		try {
			// Write Barcode
			bitMatrix = new Code128Writer().encode("123456789", BarcodeFormat.CODE_128, 500, 300, null);
			MatrixToImageWriter.writeToStream(bitMatrix, "png", new FileOutputStream(new File("D://code128_123456789.png")));
			Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hints.put(EncodeHintType.DATA_MATRIX_SHAPE, SymbolShapeHint.FORCE_SQUARE);
			hints.put(EncodeHintType.MIN_SIZE, new Dimension(128, 128));
			bitMatrix = new DataMatrixWriter().encode("123456789", BarcodeFormat.DATA_MATRIX, 128, 128, hints);
			MatrixToImageWriter.writeToStream(bitMatrix, "png", new FileOutputStream(new File("D://datamatrixcode_123456789.png")));
		} catch (Exception e) {
		}

	}
}
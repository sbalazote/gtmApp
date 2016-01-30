package com.lsntsolutions.gtmApp.helper;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static com.lsntsolutions.gtmApp.constant.Constants.AES_ENCRYPTION_KEY;
import static com.lsntsolutions.gtmApp.constant.Constants.ENCRYPTION_SALT;

public class EncryptionHelper {

	private static final Logger logger = LoggerFactory.getLogger(EncryptionHelper.class);


	private EncryptionHelper() {
	}

	public static String generateHash(String str) {
		return DigestUtils.sha1Hex(ENCRYPTION_SALT + str);
	}

	public static String AESEncrypt(String strToEncrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			final SecretKeySpec secretKey = new SecretKeySpec(AES_ENCRYPTION_KEY.getBytes(), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			final String encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
			return encryptedString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String AESDecrypt(String strToDecrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			final SecretKeySpec secretKey = new SecretKeySpec(AES_ENCRYPTION_KEY.getBytes(), "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
			return decryptedString;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());

		}
		return null;
	}

}

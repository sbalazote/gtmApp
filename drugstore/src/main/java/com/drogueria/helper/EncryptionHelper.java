package com.drogueria.helper;

import static com.drogueria.constant.Constants.AES_ENCRYPTION_KEY;
import static com.drogueria.constant.Constants.ENCRYPTION_SALT;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class EncryptionHelper {

	private EncryptionHelper() {
	}

	public static String generateHash(String str) {
		System.out.println(str);
		System.out.println(DigestUtils.sha1Hex(ENCRYPTION_SALT + str));
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
			e.printStackTrace();

		}
		return null;
	}

}

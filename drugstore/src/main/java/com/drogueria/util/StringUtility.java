package com.drogueria.util;

public final class StringUtility {

	private StringUtility() {
	}

	public static String removeLeadingZero(String str) {
		return str.replaceAll("^0", "");
	}

	public static String addLeadingZeros(Integer number, Integer length) {
		return addLeadingZeros(String.valueOf(number), length);
	}

	public static String addLeadingZeros(String baseString, Integer length) {
		String str = "" + baseString;
		while (str.length() < length) {
			str = "0" + str;
		}
		return str;
	}

	public static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/* Este metodo convierte de formato corto de fecha "ddMMaa" a formato largo "dd/MM/aaaa" */
	public static String toLongDateFormat(String expirationDate) {
		return expirationDate.substring(0, 2) + "/" + expirationDate.substring(2, 4) + "/" + "20".concat(expirationDate.substring(4, 6));
	}

	/* Este metodo convierte de formato corto de fecha "dd/MM/aaaa" a formato corto "aaaa/MM/dd" muy util en SQL */
	public static String toUSDateFormat(String date) {
		return date.substring(6, 10) + "/" + date.substring(3, 5) + "/" + date.substring(0, 2);
	}

}

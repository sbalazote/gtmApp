package com.drogueria.constant;

public final class Constants {

	public static final String SELF_SERIALIZED_SERIAL_FORMAT = "414%s21%s";
	public static final String SELF_SERIALIZED_TAG_LEADING_REGEX = "414\\d{13}21";
	public static final Integer SERIAL_NUMBER_LENGTH = 7;
	public static final Integer SELF_SERIALIZED_CODE_LENGTH = 3;
	public static final Integer GLN_LENGTH = 13;
	public static final Integer SELF_SERIALIZED_SEPARATOR_SERIAL_LENGTH = 2;
	public static final Integer SELF_SERIALIZED_SERIAL_LENGTH = 7;
	public static final Integer SELF_SERIALIZED_LENGTH = 25;
	public static final Integer GTIN_LENGTH = 14;
	public static final String ENCRYPTION_SALT = "TPPROF7599";

	public static final String AES_ENCRYPTION_KEY = "DROGUERIAONCOMED";

	public static final Integer QUERY_MAX_RESULTS = 500;

	public static final String ERROR_ANMAT_ALREADY_CANCELLED = "4";

	public static final boolean TEST = true;
	public static final String TEST_ORIGIN_GLN = "9999999999918";
	public static final String TEST_DESTINATION_GLN = "glnws";
	public static final String TEST_ORIGIN_TAXID = "20259171289";
	public static final String TEST_DESTINATION_TAXID = "20259171289";
	public static final String TEST_GTIN = "07793081057133";
	public static final String TEST_EVENT = "133";

	private Constants() {

	}
}

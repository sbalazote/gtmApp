package com.drogueria.webservice;

public class ConfigReader {

	private static String isProduccion;
	private static String usr;
	private static String pass;

	public ConfigReader() {
		this.readFile();
	}

	private void readFile() {
		ConfigReader.isProduccion = "false";
		ConfigReader.usr = "testwservice";
		ConfigReader.pass = "testwservicepsw";
	}

	public static String getPass() {
		return ConfigReader.pass;
	}

	public static String getIsProduccion() {
		return ConfigReader.isProduccion;
	}

	public static String getUsr() {
		return ConfigReader.usr;
	}

}

package com.drogueria.config;

import java.io.IOException;
import java.util.Properties;

public class PropertyProvider {

	public static final String WEB_SERVICE_URL = "webservice.url";
	public static final String IS_PRODUCTION = "is.production";
    public static final String VERSION = "version";

	private static PropertyProvider instance = null;

	private final Properties prop = new Properties();

	private PropertyProvider() {
	}

	public static PropertyProvider getInstance() {
		if (instance == null) {
			instance = new PropertyProvider();
			try {
				instance.prop.load(PropertyProvider.class.getClassLoader().getResourceAsStream("config.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public String getProp(String propName) {
		return this.prop.getProperty(propName);
	}

}

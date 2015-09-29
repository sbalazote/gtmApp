package com.lsntsolutions.gtmApp.config;

import java.io.IOException;
import java.util.Properties;

public class PropertyProvider {

	public static final String WEB_SERVICE_URL = "webservice.url";
	public static final String IS_PRODUCTION = "is.production";
	public static final String ARTIFACT_ID = "artifactId";
	public static final String VERSION = "version";
	public static final String LOGO = "logo";
	public static final String NAME = "name";
	public static final String DATABASE_URL = "persistence.database.url";
	public static final String DATABASE_USERNAME = "persistence.database.username";
	public static final String DATABASE_PASSWORD = "persistence.database.password";
	public static final String LICENSE_EXPIRATION = "licenseExpiration";

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

	public void setProp(String propName, String propValue) {
		this.prop.setProperty(propName, propValue);
	}
}

package com.drogueria.webservice;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.cert.X509Certificate;

import javax.net.ssl.*;

public class MyProvider extends Provider {

	private static final long serialVersionUID = 1L;

	public MyProvider() {
		super("MyProvider", 1.0, "Trust certificates");
		this.put("TrustManagerFactory.TrustAllCertificates", MyTrustManagerFactory.class.getName());
    }

	private static class MyTrustManagerFactory extends TrustManagerFactorySpi {

		public MyTrustManagerFactory() {
		}

		@Override
		protected void engineInit(KeyStore keystore) {
		}

		@Override
		protected void engineInit(ManagerFactoryParameters mgrparams) {
		}

		@Override
		protected TrustManager[] engineGetTrustManagers() {
			return new TrustManager[] { new MyX509TrustManager() };
		}
	}

	protected static class MyX509TrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

}
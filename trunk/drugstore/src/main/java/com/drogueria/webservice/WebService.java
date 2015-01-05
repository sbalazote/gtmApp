package com.drogueria.webservice;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.ws.Service;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import com.inssjp.mywebservice.business.ConfirmacionTransaccionDTO;
import com.inssjp.mywebservice.business.IWebServicePortType;
import com.inssjp.mywebservice.business.MedicamentosDTO;
import com.inssjp.mywebservice.business.MedicamentosDTODHSerie;
import com.inssjp.mywebservice.business.TransaccionesNoConfirmadasWSResult;
import com.inssjp.mywebservice.business.WebServiceConfirmResult;
import com.inssjp.mywebservice.business.WebServiceResult;

public class WebService {

	private static final String PATH_PAMI_PRODUCCION = "https://trazabilidad.pami.org.ar:9050/trazamed.WebService";
	private static final String PATH_PAMI_ENTRENAMIENTO = "https://servicios.pami.org.ar/trazamed.WebService?wsdl";
	private static final String targetNamespace = "http://business.mywebservice.inssjp.com/";
	private static final String webServiceName = "IWebService";

	private String proxy;
	private String proxyPort;
	private boolean informProxy = false;

	public WebService() {
		new ConfigReader();
	}

	public WebServiceResult sendMedicamentos(MedicamentosDTO[] medicamentos, String usuario, String pass) throws Exception {
		IWebServicePortType webService = this.configurarWebService();
		WebServiceResult wsr = null;
		wsr = webService.sendMedicamentos(medicamentos, usuario, pass);
		return wsr;
	}

	public WebServiceResult sendMedicamentosDHSerie(MedicamentosDTODHSerie[] medicamentos, String usuario, String pass) throws Exception {
		IWebServicePortType webService = this.configurarWebService();
		WebServiceResult wsr = null;
		wsr = webService.sendMedicamentosDHSerie(medicamentos, usuario, pass);
		return wsr;
	}

	public WebServiceResult sendCancelacTransacc(Long codigo, String usuario, String pass) throws Exception {
		IWebServicePortType webService = this.configurarWebService();
		WebServiceResult wsr = null;
		wsr = webService.sendCancelacTransacc(codigo, usuario, pass);
		return wsr;
	}

	public WebServiceConfirmResult confirmarTransaccion(ConfirmacionTransaccionDTO[] transaccionesPendientes, String usuario, String pass) throws Exception {
		IWebServicePortType webService = this.configurarWebService();
		WebServiceConfirmResult wsr = null;
		wsr = webService.sendConfirmaTransacc(usuario, pass, transaccionesPendientes);
		return wsr;
	}

	public TransaccionesNoConfirmadasWSResult getTransaccionesNoConfirmadas(String usuario, String pass, long idTransaccion, String glnInformador,
			String glnOrigen, String glnDestino, String gtin, long idEvento, String fechaOpeDesde, String fechaOpeHasta, String fechaTransDesde,
			String fechaTransHasta, String fechaVencimientoDesde, String fechaVencimientoHasta, String nroRemito, String nroFactura, long estadoTransaccion,
			String otro, String otro1) throws Exception {
		IWebServicePortType webService = this.configurarWebService();
		TransaccionesNoConfirmadasWSResult wsr = null;
		wsr = webService.getTransaccionesNoConfirmadas(usuario, pass, idTransaccion, glnInformador, glnOrigen, glnDestino, gtin, idEvento, fechaOpeDesde,
				fechaOpeHasta, fechaTransDesde, fechaTransHasta, fechaVencimientoDesde, fechaVencimientoHasta, nroRemito, nroFactura, estadoTransaccion, otro,
				otro1);
		return wsr;
	}

	@SuppressWarnings("deprecation")
	private IWebServicePortType configurarWebService() throws ServiceException, MalformedURLException {
		this.setDefaultProxy();
		String wsdl = this.getPathWsdl(ConfigReader.getIsProduccion());
		URL wsdlURL = null;

		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] { "web-service.xml" });
		Resource resource = null;
		if (ConfigReader.getIsProduccion().equals("true")) {
			resource = appContext.getResource("classpath:trazaMedWebService.wsdl");
		} else {
			resource = appContext.getResource("classpath:trazaMedWebServiceTest.wsdl");
		}
		File wsdlFile = null;
		try {
			wsdlFile = resource.getFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (wsdlFile.exists()) {
			try {
				wsdlURL = wsdlFile.toURL();
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		} else {
			try {
				wsdlURL = new URL(wsdl);
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}
		QName qname = new QName(WebService.targetNamespace, WebService.webServiceName);
		Service service = Service.create(wsdlURL, qname);
		IWebServicePortType webService = service.getPort(IWebServicePortType.class);
		this.configureSSLTrusted(webService);
		this.configureHTTPSSoapHeaders(webService);
		Client client = ClientProxy.getClient(webService);
		client.getInInterceptors().add(new LoggingInInterceptor());
		client.getOutInterceptors().add(new LoggingOutInterceptor());
		return webService;
	}

	private void configureSSLTrusted(IWebServicePortType webService) {

		java.security.Security.addProvider(new MyProvider());
		java.security.Security.setProperty("ssl.TrustManagerFactory.algorithm", "TrustAllCertificates");
		Client proxy = ClientProxy.getClient(webService);
		HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
		TLSClientParameters tcp = new TLSClientParameters();
		tcp.setDisableCNCheck(Boolean.TRUE);
		conduit.setTlsClientParameters(tcp);

	}

	@SuppressWarnings("unchecked")
	private void configureHTTPSSoapHeaders(IWebServicePortType webService) {

		@SuppressWarnings("rawtypes")
		Map outProps = new HashMap();
		outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		outProps.put(WSHandlerConstants.USER, ConfigReader.getUsr());
		outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, ClientPasswordCallback.class.getName());
		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
		Client client = ClientProxy.getClient(webService);
		Endpoint cxfEndpoint = client.getEndpoint();
		cxfEndpoint.getOutInterceptors().add(wssOut);
	}

	private String getPathWsdl(String valor) {

		String resultado = null;
		if (valor != null) {
			if (valor.toUpperCase().equals("TRUE")) {
				resultado = WebService.PATH_PAMI_PRODUCCION;
			} else if (valor.toUpperCase().equals("FALSE")) {
				resultado = WebService.PATH_PAMI_ENTRENAMIENTO;
			}
		}
		return resultado;

	}

	public String getProxy() {
		return this.proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getProxyPort() {
		return this.proxyPort;
	}

	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}

	public boolean isInformProxy() {
		return this.informProxy;
	}

	public void setInformProxy(boolean informProxy) {
		this.informProxy = informProxy;
	}

	public void setDefaultProxy() {
		if (this.informProxy) {
			System.setProperty("https.proxyHost", this.proxy);
			System.setProperty("https.proxyPort", this.proxyPort);
		}
	}
}

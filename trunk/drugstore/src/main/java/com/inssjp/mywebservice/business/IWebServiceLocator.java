/**
 * IWebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.inssjp.mywebservice.business;

public class IWebServiceLocator extends org.apache.axis.client.Service implements com.inssjp.mywebservice.business.IWebService {

    public IWebServiceLocator() {
    }


    public IWebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IWebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for IWebServicePort
    private java.lang.String IWebServicePort_address = "http://localhost:9051/trazamed.WebService";

    public java.lang.String getIWebServicePortAddress() {
        return IWebServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IWebServicePortWSDDServiceName = "IWebServicePort";

    public java.lang.String getIWebServicePortWSDDServiceName() {
        return IWebServicePortWSDDServiceName;
    }

    public void setIWebServicePortWSDDServiceName(java.lang.String name) {
        IWebServicePortWSDDServiceName = name;
    }

    public com.inssjp.mywebservice.business.IWebServicePortType getIWebServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IWebServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIWebServicePort(endpoint);
    }

    public com.inssjp.mywebservice.business.IWebServicePortType getIWebServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.inssjp.mywebservice.business.IWebServiceSoapBindingStub _stub = new com.inssjp.mywebservice.business.IWebServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getIWebServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIWebServicePortEndpointAddress(java.lang.String address) {
        IWebServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.inssjp.mywebservice.business.IWebServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.inssjp.mywebservice.business.IWebServiceSoapBindingStub _stub = new com.inssjp.mywebservice.business.IWebServiceSoapBindingStub(new java.net.URL(IWebServicePort_address), this);
                _stub.setPortName(getIWebServicePortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("IWebServicePort".equals(inputPortName)) {
            return getIWebServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://business.mywebservice.inssjp.com/", "IWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://business.mywebservice.inssjp.com/", "IWebServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("IWebServicePort".equals(portName)) {
            setIWebServicePortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

/**
 * IWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.inssjp.mywebservice.business;

public interface IWebService extends javax.xml.rpc.Service {
    public java.lang.String getIWebServicePortAddress();

    public com.inssjp.mywebservice.business.IWebServicePortType getIWebServicePort() throws javax.xml.rpc.ServiceException;

    public com.inssjp.mywebservice.business.IWebServicePortType getIWebServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

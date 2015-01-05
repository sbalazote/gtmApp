/**
 * IWebServicePortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.inssjp.mywebservice.business;
import javax.jws.WebService;

@WebService
public interface IWebServicePortType extends java.rmi.Remote {
    public com.inssjp.mywebservice.business.WebServiceResult sendMedicamentosFraccion(com.inssjp.mywebservice.business.MedicamentosDTOFraccion[] arg0, java.lang.String arg1, java.lang.String arg2) throws java.rmi.RemoteException;
    public com.inssjp.mywebservice.business.WebServiceConfirmResult sendConfirmaTransacc(java.lang.String arg0, java.lang.String arg1, com.inssjp.mywebservice.business.ConfirmacionTransaccionDTO[] arg2) throws java.rmi.RemoteException;
    public com.inssjp.mywebservice.business.WebServiceResult sendCancelacTransacc(java.lang.Long arg0, java.lang.String arg1, java.lang.String arg2) throws java.rmi.RemoteException;
    public com.inssjp.mywebservice.business.TransaccionesNoConfirmadasWSResult getTransaccionesNoConfirmadas(java.lang.String arg0, java.lang.String arg1, java.lang.Long arg2, java.lang.String arg3, java.lang.String arg4, java.lang.String arg5, java.lang.String arg6, java.lang.Long arg7, java.lang.String arg8, java.lang.String arg9, java.lang.String arg10, java.lang.String arg11, java.lang.String arg12, java.lang.String arg13, java.lang.String arg14, java.lang.String arg15, java.lang.Long arg16, java.lang.String arg17, java.lang.String arg18) throws java.rmi.RemoteException;
    public com.inssjp.mywebservice.business.WebServiceResult sendAlertaTransacc(java.lang.String arg0, java.lang.String arg1, long[] arg2) throws java.rmi.RemoteException;
    public com.inssjp.mywebservice.business.WebServiceResult sendMedicamentos(com.inssjp.mywebservice.business.MedicamentosDTO[] arg0, java.lang.String arg1, java.lang.String arg2) throws java.rmi.RemoteException;
    public com.inssjp.mywebservice.business.TransaccionesNoConfirmadasWSResult getEnviosPropiosAlertados(java.lang.String arg0, java.lang.String arg1, java.lang.Long arg2, java.lang.String arg3, java.lang.String arg4, java.lang.String arg5, java.lang.String arg6, java.lang.Long arg7, java.lang.String arg8, java.lang.String arg9, java.lang.String arg10, java.lang.String arg11, java.lang.String arg12, java.lang.String arg13, java.lang.Long arg14, java.lang.Long arg15) throws java.rmi.RemoteException;
    public com.inssjp.mywebservice.business.WebServiceResult sendCancelacTransaccParcial(java.lang.Long arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.String arg4) throws java.rmi.RemoteException;
    public com.inssjp.mywebservice.business.TransaccionesWSResult getTransaccionesWS(java.lang.String arg0, java.lang.String arg1, java.lang.Long arg2, java.lang.String arg3, java.lang.String arg4, java.lang.String arg5, java.lang.Long arg6, java.lang.String arg7, java.lang.String arg8, java.lang.String arg9, java.lang.String arg10, java.lang.String arg11, java.lang.String arg12, java.lang.String arg13, java.lang.String arg14, java.lang.Long arg15, java.lang.Long arg16) throws java.rmi.RemoteException;
    public com.inssjp.mywebservice.business.CatalogoGTINResult getCatalogoElectronicoByGTIN(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.String arg4, java.lang.String arg5) throws java.rmi.RemoteException;
    public com.inssjp.mywebservice.business.CatalogoGLNResult getCatalogoElectronicoByGLN(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.String arg4, java.lang.Long arg5, java.lang.Long arg6) throws java.rmi.RemoteException;
    public com.inssjp.mywebservice.business.WebServiceResult sendMedicamentosDHSerie(com.inssjp.mywebservice.business.MedicamentosDTODHSerie[] arg0, java.lang.String arg1, java.lang.String arg2) throws java.rmi.RemoteException;
}

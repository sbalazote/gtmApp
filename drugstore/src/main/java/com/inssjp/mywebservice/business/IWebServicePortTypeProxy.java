package com.inssjp.mywebservice.business;

public class IWebServicePortTypeProxy implements com.inssjp.mywebservice.business.IWebServicePortType {
  private String _endpoint = null;
  private com.inssjp.mywebservice.business.IWebServicePortType iWebServicePortType = null;
  
  public IWebServicePortTypeProxy() {
    _initIWebServicePortTypeProxy();
  }
  
  public IWebServicePortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initIWebServicePortTypeProxy();
  }
  
  private void _initIWebServicePortTypeProxy() {
    try {
      iWebServicePortType = (new com.inssjp.mywebservice.business.IWebServiceLocator()).getIWebServicePort();
      if (iWebServicePortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iWebServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iWebServicePortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iWebServicePortType != null)
      ((javax.xml.rpc.Stub)iWebServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.inssjp.mywebservice.business.IWebServicePortType getIWebServicePortType() {
    if (iWebServicePortType == null)
      _initIWebServicePortTypeProxy();
    return iWebServicePortType;
  }
  
  public com.inssjp.mywebservice.business.WebServiceResult sendMedicamentosFraccion(com.inssjp.mywebservice.business.MedicamentosDTOFraccion[] arg0, java.lang.String arg1, java.lang.String arg2) throws java.rmi.RemoteException{
    if (iWebServicePortType == null)
      _initIWebServicePortTypeProxy();
    return iWebServicePortType.sendMedicamentosFraccion(arg0, arg1, arg2);
  }
  
  public com.inssjp.mywebservice.business.WebServiceConfirmResult sendConfirmaTransacc(java.lang.String arg0, java.lang.String arg1, com.inssjp.mywebservice.business.ConfirmacionTransaccionDTO[] arg2) throws java.rmi.RemoteException{
    if (iWebServicePortType == null)
      _initIWebServicePortTypeProxy();
    return iWebServicePortType.sendConfirmaTransacc(arg0, arg1, arg2);
  }
  
  public com.inssjp.mywebservice.business.WebServiceResult sendCancelacTransacc(java.lang.Long arg0, java.lang.String arg1, java.lang.String arg2) throws java.rmi.RemoteException{
    if (iWebServicePortType == null)
      _initIWebServicePortTypeProxy();
    return iWebServicePortType.sendCancelacTransacc(arg0, arg1, arg2);
  }
  
  public com.inssjp.mywebservice.business.TransaccionesNoConfirmadasWSResult getTransaccionesNoConfirmadas(java.lang.String arg0, java.lang.String arg1, java.lang.Long arg2, java.lang.String arg3, java.lang.String arg4, java.lang.String arg5, java.lang.String arg6, java.lang.Long arg7, java.lang.String arg8, java.lang.String arg9, java.lang.String arg10, java.lang.String arg11, java.lang.String arg12, java.lang.String arg13, java.lang.String arg14, java.lang.String arg15, java.lang.Long arg16, java.lang.String arg17, java.lang.String arg18, java.lang.Long arg19, java.lang.Long arg20) throws java.rmi.RemoteException{
    if (iWebServicePortType == null)
      _initIWebServicePortTypeProxy();
    return iWebServicePortType.getTransaccionesNoConfirmadas(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
  }
  
  public com.inssjp.mywebservice.business.WebServiceResult sendAlertaTransacc(java.lang.String arg0, java.lang.String arg1, long[] arg2) throws java.rmi.RemoteException{
    if (iWebServicePortType == null)
      _initIWebServicePortTypeProxy();
    return iWebServicePortType.sendAlertaTransacc(arg0, arg1, arg2);
  }
  
  public com.inssjp.mywebservice.business.WebServiceResult sendMedicamentos(com.inssjp.mywebservice.business.MedicamentosDTO[] arg0, java.lang.String arg1, java.lang.String arg2) throws java.rmi.RemoteException{
    if (iWebServicePortType == null)
      _initIWebServicePortTypeProxy();
    return iWebServicePortType.sendMedicamentos(arg0, arg1, arg2);
  }
  
  public com.inssjp.mywebservice.business.ConsultaStockWSResult getConsultaStock(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.String arg4, java.lang.Long arg5, java.lang.String arg6, java.lang.String arg7, java.lang.String arg8, java.lang.Long arg9, java.lang.Long arg10) throws java.rmi.RemoteException{
    if (iWebServicePortType == null)
      _initIWebServicePortTypeProxy();
    return iWebServicePortType.getConsultaStock(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
  }
  
  public com.inssjp.mywebservice.business.TransaccionesNoConfirmadasWSResult getEnviosPropiosAlertados(java.lang.String arg0, java.lang.String arg1, java.lang.Long arg2, java.lang.String arg3, java.lang.String arg4, java.lang.String arg5, java.lang.String arg6, java.lang.Long arg7, java.lang.String arg8, java.lang.String arg9, java.lang.String arg10, java.lang.String arg11, java.lang.String arg12, java.lang.String arg13, java.lang.Long arg14, java.lang.Long arg15) throws java.rmi.RemoteException{
    if (iWebServicePortType == null)
      _initIWebServicePortTypeProxy();
    return iWebServicePortType.getEnviosPropiosAlertados(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
  }
  
  public com.inssjp.mywebservice.business.WebServiceResult sendCancelacTransaccParcial(java.lang.Long arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.String arg4) throws java.rmi.RemoteException{
    if (iWebServicePortType == null)
      _initIWebServicePortTypeProxy();
    return iWebServicePortType.sendCancelacTransaccParcial(arg0, arg1, arg2, arg3, arg4);
  }
  
  public com.inssjp.mywebservice.business.TransaccionesWSResult getTransaccionesWS(java.lang.String arg0, java.lang.String arg1, java.lang.Long arg2, java.lang.String arg3, java.lang.String arg4, java.lang.String arg5, java.lang.Long arg6, java.lang.String arg7, java.lang.String arg8, java.lang.String arg9, java.lang.String arg10, java.lang.String arg11, java.lang.String arg12, java.lang.String arg13, java.lang.String arg14, java.lang.Long arg15, java.lang.Long arg16) throws java.rmi.RemoteException{
    if (iWebServicePortType == null)
      _initIWebServicePortTypeProxy();
    return iWebServicePortType.getTransaccionesWS(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
  }
  
  public com.inssjp.mywebservice.business.CatalogoGTINResult getCatalogoElectronicoByGTIN(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.String arg4, java.lang.String arg5, java.lang.Long arg6, java.lang.Long arg7) throws java.rmi.RemoteException{
    if (iWebServicePortType == null)
      _initIWebServicePortTypeProxy();
    return iWebServicePortType.getCatalogoElectronicoByGTIN(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
  }
  
  public com.inssjp.mywebservice.business.CatalogoGLNResult getCatalogoElectronicoByGLN(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.String arg4, java.lang.Long arg5, java.lang.Long arg6, java.lang.Long arg7, java.lang.Long arg8, java.lang.String arg9) throws java.rmi.RemoteException{
    if (iWebServicePortType == null)
      _initIWebServicePortTypeProxy();
    return iWebServicePortType.getCatalogoElectronicoByGLN(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
  }
  
  public com.inssjp.mywebservice.business.WebServiceResult sendMedicamentosDHSerie(com.inssjp.mywebservice.business.MedicamentosDTODHSerie[] arg0, java.lang.String arg1, java.lang.String arg2) throws java.rmi.RemoteException{
    if (iWebServicePortType == null)
      _initIWebServicePortTypeProxy();
    return iWebServicePortType.sendMedicamentosDHSerie(arg0, arg1, arg2);
  }
  
  
}
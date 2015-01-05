/**
 * WebServiceResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.inssjp.mywebservice.business;

public class WebServiceResult  implements java.io.Serializable {
    private java.lang.String codigoTransaccion;

    private com.inssjp.mywebservice.business.WebServiceError[] errores;

    private java.lang.Boolean resultado;

    public WebServiceResult() {
    }

    public WebServiceResult(
           java.lang.String codigoTransaccion,
           com.inssjp.mywebservice.business.WebServiceError[] errores,
           java.lang.Boolean resultado) {
           this.codigoTransaccion = codigoTransaccion;
           this.errores = errores;
           this.resultado = resultado;
    }


    /**
     * Gets the codigoTransaccion value for this WebServiceResult.
     * 
     * @return codigoTransaccion
     */
    public java.lang.String getCodigoTransaccion() {
        return codigoTransaccion;
    }


    /**
     * Sets the codigoTransaccion value for this WebServiceResult.
     * 
     * @param codigoTransaccion
     */
    public void setCodigoTransaccion(java.lang.String codigoTransaccion) {
        this.codigoTransaccion = codigoTransaccion;
    }


    /**
     * Gets the errores value for this WebServiceResult.
     * 
     * @return errores
     */
    public com.inssjp.mywebservice.business.WebServiceError[] getErrores() {
        return errores;
    }


    /**
     * Sets the errores value for this WebServiceResult.
     * 
     * @param errores
     */
    public void setErrores(com.inssjp.mywebservice.business.WebServiceError[] errores) {
        this.errores = errores;
    }

    public com.inssjp.mywebservice.business.WebServiceError getErrores(int i) {
        return this.errores[i];
    }

    public void setErrores(int i, com.inssjp.mywebservice.business.WebServiceError _value) {
        this.errores[i] = _value;
    }


    /**
     * Gets the resultado value for this WebServiceResult.
     * 
     * @return resultado
     */
    public java.lang.Boolean getResultado() {
        return resultado;
    }


    /**
     * Sets the resultado value for this WebServiceResult.
     * 
     * @param resultado
     */
    public void setResultado(java.lang.Boolean resultado) {
        this.resultado = resultado;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WebServiceResult)) return false;
        WebServiceResult other = (WebServiceResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigoTransaccion==null && other.getCodigoTransaccion()==null) || 
             (this.codigoTransaccion!=null &&
              this.codigoTransaccion.equals(other.getCodigoTransaccion()))) &&
            ((this.errores==null && other.getErrores()==null) || 
             (this.errores!=null &&
              java.util.Arrays.equals(this.errores, other.getErrores()))) &&
            ((this.resultado==null && other.getResultado()==null) || 
             (this.resultado!=null &&
              this.resultado.equals(other.getResultado())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getCodigoTransaccion() != null) {
            _hashCode += getCodigoTransaccion().hashCode();
        }
        if (getErrores() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getErrores());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getErrores(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getResultado() != null) {
            _hashCode += getResultado().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WebServiceResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://business.mywebservice.inssjp.com/", "webServiceResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoTransaccion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoTransaccion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errores");
        elemField.setXmlName(new javax.xml.namespace.QName("", "errores"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://business.mywebservice.inssjp.com/", "webServiceError"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resultado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

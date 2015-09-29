/**
 * TransaccionesWSResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.inssjp.mywebservice.business;

public class TransaccionesWSResult  implements java.io.Serializable {
    private java.lang.Long cantPaginas;

    private com.inssjp.mywebservice.business.WebServiceError[] errores;

    private java.lang.Boolean hay_error;

    private com.inssjp.mywebservice.business.TransaccionPlainWS[] list;

    public TransaccionesWSResult() {
    }

    public TransaccionesWSResult(
           java.lang.Long cantPaginas,
           com.inssjp.mywebservice.business.WebServiceError[] errores,
           java.lang.Boolean hay_error,
           com.inssjp.mywebservice.business.TransaccionPlainWS[] list) {
           this.cantPaginas = cantPaginas;
           this.errores = errores;
           this.hay_error = hay_error;
           this.list = list;
    }


    /**
     * Gets the cantPaginas value for this TransaccionesWSResult.
     * 
     * @return cantPaginas
     */
    public java.lang.Long getCantPaginas() {
        return cantPaginas;
    }


    /**
     * Sets the cantPaginas value for this TransaccionesWSResult.
     * 
     * @param cantPaginas
     */
    public void setCantPaginas(java.lang.Long cantPaginas) {
        this.cantPaginas = cantPaginas;
    }


    /**
     * Gets the errores value for this TransaccionesWSResult.
     * 
     * @return errores
     */
    public com.inssjp.mywebservice.business.WebServiceError[] getErrores() {
        return errores;
    }


    /**
     * Sets the errores value for this TransaccionesWSResult.
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
     * Gets the hay_error value for this TransaccionesWSResult.
     * 
     * @return hay_error
     */
    public java.lang.Boolean getHay_error() {
        return hay_error;
    }


    /**
     * Sets the hay_error value for this TransaccionesWSResult.
     * 
     * @param hay_error
     */
    public void setHay_error(java.lang.Boolean hay_error) {
        this.hay_error = hay_error;
    }


    /**
     * Gets the list value for this TransaccionesWSResult.
     * 
     * @return list
     */
    public com.inssjp.mywebservice.business.TransaccionPlainWS[] getList() {
        return list;
    }


    /**
     * Sets the list value for this TransaccionesWSResult.
     * 
     * @param list
     */
    public void setList(com.inssjp.mywebservice.business.TransaccionPlainWS[] list) {
        this.list = list;
    }

    public com.inssjp.mywebservice.business.TransaccionPlainWS getList(int i) {
        return this.list[i];
    }

    public void setList(int i, com.inssjp.mywebservice.business.TransaccionPlainWS _value) {
        this.list[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TransaccionesWSResult)) return false;
        TransaccionesWSResult other = (TransaccionesWSResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cantPaginas==null && other.getCantPaginas()==null) || 
             (this.cantPaginas!=null &&
              this.cantPaginas.equals(other.getCantPaginas()))) &&
            ((this.errores==null && other.getErrores()==null) || 
             (this.errores!=null &&
              java.util.Arrays.equals(this.errores, other.getErrores()))) &&
            ((this.hay_error==null && other.getHay_error()==null) || 
             (this.hay_error!=null &&
              this.hay_error.equals(other.getHay_error()))) &&
            ((this.list==null && other.getList()==null) || 
             (this.list!=null &&
              java.util.Arrays.equals(this.list, other.getList())));
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
        if (getCantPaginas() != null) {
            _hashCode += getCantPaginas().hashCode();
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
        if (getHay_error() != null) {
            _hashCode += getHay_error().hashCode();
        }
        if (getList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TransaccionesWSResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://business.mywebservice.inssjp.com/", "transaccionesWSResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cantPaginas");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cantPaginas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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
        elemField.setFieldName("hay_error");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hay_error"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("list");
        elemField.setXmlName(new javax.xml.namespace.QName("", "list"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://business.mywebservice.inssjp.com/", "transaccionPlainWS"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
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

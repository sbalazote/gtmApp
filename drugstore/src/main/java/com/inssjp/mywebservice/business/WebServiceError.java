/**
 * WebServiceError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.inssjp.mywebservice.business;

public class WebServiceError  implements java.io.Serializable {
    private java.lang.String _c_error;

    private java.lang.String _d_error;

    public WebServiceError() {
    }

    public WebServiceError(
           java.lang.String _c_error,
           java.lang.String _d_error) {
           this._c_error = _c_error;
           this._d_error = _d_error;
    }


    /**
     * Gets the _c_error value for this WebServiceError.
     * 
     * @return _c_error
     */
    public java.lang.String get_c_error() {
        return _c_error;
    }


    /**
     * Sets the _c_error value for this WebServiceError.
     * 
     * @param _c_error
     */
    public void set_c_error(java.lang.String _c_error) {
        this._c_error = _c_error;
    }


    /**
     * Gets the _d_error value for this WebServiceError.
     * 
     * @return _d_error
     */
    public java.lang.String get_d_error() {
        return _d_error;
    }


    /**
     * Sets the _d_error value for this WebServiceError.
     * 
     * @param _d_error
     */
    public void set_d_error(java.lang.String _d_error) {
        this._d_error = _d_error;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WebServiceError)) return false;
        WebServiceError other = (WebServiceError) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this._c_error==null && other.get_c_error()==null) || 
             (this._c_error!=null &&
              this._c_error.equals(other.get_c_error()))) &&
            ((this._d_error==null && other.get_d_error()==null) || 
             (this._d_error!=null &&
              this._d_error.equals(other.get_d_error())));
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
        if (get_c_error() != null) {
            _hashCode += get_c_error().hashCode();
        }
        if (get_d_error() != null) {
            _hashCode += get_d_error().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WebServiceError.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://business.mywebservice.inssjp.com/", "webServiceError"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_c_error");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_c_error"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_d_error");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_d_error"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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

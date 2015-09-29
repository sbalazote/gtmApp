/**
 * MonodrogaPlain.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.inssjp.mywebservice.business;

public class MonodrogaPlain  implements java.io.Serializable {
    private java.lang.String _c_monodroga;

    private java.lang.String _d_monodroga;

    public MonodrogaPlain() {
    }

    public MonodrogaPlain(
           java.lang.String _c_monodroga,
           java.lang.String _d_monodroga) {
           this._c_monodroga = _c_monodroga;
           this._d_monodroga = _d_monodroga;
    }


    /**
     * Gets the _c_monodroga value for this MonodrogaPlain.
     * 
     * @return _c_monodroga
     */
    public java.lang.String get_c_monodroga() {
        return _c_monodroga;
    }


    /**
     * Sets the _c_monodroga value for this MonodrogaPlain.
     * 
     * @param _c_monodroga
     */
    public void set_c_monodroga(java.lang.String _c_monodroga) {
        this._c_monodroga = _c_monodroga;
    }


    /**
     * Gets the _d_monodroga value for this MonodrogaPlain.
     * 
     * @return _d_monodroga
     */
    public java.lang.String get_d_monodroga() {
        return _d_monodroga;
    }


    /**
     * Sets the _d_monodroga value for this MonodrogaPlain.
     * 
     * @param _d_monodroga
     */
    public void set_d_monodroga(java.lang.String _d_monodroga) {
        this._d_monodroga = _d_monodroga;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MonodrogaPlain)) return false;
        MonodrogaPlain other = (MonodrogaPlain) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this._c_monodroga==null && other.get_c_monodroga()==null) || 
             (this._c_monodroga!=null &&
              this._c_monodroga.equals(other.get_c_monodroga()))) &&
            ((this._d_monodroga==null && other.get_d_monodroga()==null) || 
             (this._d_monodroga!=null &&
              this._d_monodroga.equals(other.get_d_monodroga())));
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
        if (get_c_monodroga() != null) {
            _hashCode += get_c_monodroga().hashCode();
        }
        if (get_d_monodroga() != null) {
            _hashCode += get_d_monodroga().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MonodrogaPlain.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://business.mywebservice.inssjp.com/", "monodrogaPlain"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_c_monodroga");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_c_monodroga"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_d_monodroga");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_d_monodroga"));
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

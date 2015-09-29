/**
 * ResultadoPersonalizado.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.inssjp.mywebservice.business;

public class ResultadoPersonalizado  implements java.io.Serializable {
    private java.lang.Long _id_generado;

    private java.lang.Boolean _is_modificado;

    public ResultadoPersonalizado() {
    }

    public ResultadoPersonalizado(
           java.lang.Long _id_generado,
           java.lang.Boolean _is_modificado) {
           this._id_generado = _id_generado;
           this._is_modificado = _is_modificado;
    }


    /**
     * Gets the _id_generado value for this ResultadoPersonalizado.
     * 
     * @return _id_generado
     */
    public java.lang.Long get_id_generado() {
        return _id_generado;
    }


    /**
     * Sets the _id_generado value for this ResultadoPersonalizado.
     * 
     * @param _id_generado
     */
    public void set_id_generado(java.lang.Long _id_generado) {
        this._id_generado = _id_generado;
    }


    /**
     * Gets the _is_modificado value for this ResultadoPersonalizado.
     * 
     * @return _is_modificado
     */
    public java.lang.Boolean get_is_modificado() {
        return _is_modificado;
    }


    /**
     * Sets the _is_modificado value for this ResultadoPersonalizado.
     * 
     * @param _is_modificado
     */
    public void set_is_modificado(java.lang.Boolean _is_modificado) {
        this._is_modificado = _is_modificado;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResultadoPersonalizado)) return false;
        ResultadoPersonalizado other = (ResultadoPersonalizado) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this._id_generado==null && other.get_id_generado()==null) || 
             (this._id_generado!=null &&
              this._id_generado.equals(other.get_id_generado()))) &&
            ((this._is_modificado==null && other.get_is_modificado()==null) || 
             (this._is_modificado!=null &&
              this._is_modificado.equals(other.get_is_modificado())));
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
        if (get_id_generado() != null) {
            _hashCode += get_id_generado().hashCode();
        }
        if (get_is_modificado() != null) {
            _hashCode += get_is_modificado().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResultadoPersonalizado.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://business.mywebservice.inssjp.com/", "resultadoPersonalizado"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_id_generado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_id_generado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_is_modificado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_is_modificado"));
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

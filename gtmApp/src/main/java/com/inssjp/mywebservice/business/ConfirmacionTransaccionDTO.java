/**
 * ConfirmacionTransaccionDTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.inssjp.mywebservice.business;

public class ConfirmacionTransaccionDTO  implements java.io.Serializable {
    private java.lang.String f_operacion;

    private java.lang.Long p_ids_transac;

    public ConfirmacionTransaccionDTO() {
    }

    public ConfirmacionTransaccionDTO(
           java.lang.String f_operacion,
           java.lang.Long p_ids_transac) {
           this.f_operacion = f_operacion;
           this.p_ids_transac = p_ids_transac;
    }


    /**
     * Gets the f_operacion value for this ConfirmacionTransaccionDTO.
     * 
     * @return f_operacion
     */
    public java.lang.String getF_operacion() {
        return f_operacion;
    }


    /**
     * Sets the f_operacion value for this ConfirmacionTransaccionDTO.
     * 
     * @param f_operacion
     */
    public void setF_operacion(java.lang.String f_operacion) {
        this.f_operacion = f_operacion;
    }


    /**
     * Gets the p_ids_transac value for this ConfirmacionTransaccionDTO.
     * 
     * @return p_ids_transac
     */
    public java.lang.Long getP_ids_transac() {
        return p_ids_transac;
    }


    /**
     * Sets the p_ids_transac value for this ConfirmacionTransaccionDTO.
     * 
     * @param p_ids_transac
     */
    public void setP_ids_transac(java.lang.Long p_ids_transac) {
        this.p_ids_transac = p_ids_transac;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConfirmacionTransaccionDTO)) return false;
        ConfirmacionTransaccionDTO other = (ConfirmacionTransaccionDTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.f_operacion==null && other.getF_operacion()==null) || 
             (this.f_operacion!=null &&
              this.f_operacion.equals(other.getF_operacion()))) &&
            ((this.p_ids_transac==null && other.getP_ids_transac()==null) || 
             (this.p_ids_transac!=null &&
              this.p_ids_transac.equals(other.getP_ids_transac())));
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
        if (getF_operacion() != null) {
            _hashCode += getF_operacion().hashCode();
        }
        if (getP_ids_transac() != null) {
            _hashCode += getP_ids_transac().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConfirmacionTransaccionDTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://business.mywebservice.inssjp.com/", "confirmacionTransaccionDTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("f_operacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "f_operacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ids_transac");
        elemField.setXmlName(new javax.xml.namespace.QName("", "p_ids_transac"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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

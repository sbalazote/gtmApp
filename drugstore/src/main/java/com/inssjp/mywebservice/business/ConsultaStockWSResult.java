/**
 * ConsultaStockWSResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.inssjp.mywebservice.business;

public class ConsultaStockWSResult  extends com.inssjp.mywebservice.business.WebServiceResult  implements java.io.Serializable {
    private java.lang.Long cantPaginas;

    private com.inssjp.mywebservice.business.ConsultaStockMedicamentoWS[] list;

    public ConsultaStockWSResult() {
    }

    public ConsultaStockWSResult(
           java.lang.String codigoTransaccion,
           com.inssjp.mywebservice.business.WebServiceError[] errores,
           java.lang.Boolean resultado,
           java.lang.Long cantPaginas,
           com.inssjp.mywebservice.business.ConsultaStockMedicamentoWS[] list) {
        super(
            codigoTransaccion,
            errores,
            resultado);
        this.cantPaginas = cantPaginas;
        this.list = list;
    }


    /**
     * Gets the cantPaginas value for this ConsultaStockWSResult.
     * 
     * @return cantPaginas
     */
    public java.lang.Long getCantPaginas() {
        return cantPaginas;
    }


    /**
     * Sets the cantPaginas value for this ConsultaStockWSResult.
     * 
     * @param cantPaginas
     */
    public void setCantPaginas(java.lang.Long cantPaginas) {
        this.cantPaginas = cantPaginas;
    }


    /**
     * Gets the list value for this ConsultaStockWSResult.
     * 
     * @return list
     */
    public com.inssjp.mywebservice.business.ConsultaStockMedicamentoWS[] getList() {
        return list;
    }


    /**
     * Sets the list value for this ConsultaStockWSResult.
     * 
     * @param list
     */
    public void setList(com.inssjp.mywebservice.business.ConsultaStockMedicamentoWS[] list) {
        this.list = list;
    }

    public com.inssjp.mywebservice.business.ConsultaStockMedicamentoWS getList(int i) {
        return this.list[i];
    }

    public void setList(int i, com.inssjp.mywebservice.business.ConsultaStockMedicamentoWS _value) {
        this.list[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultaStockWSResult)) return false;
        ConsultaStockWSResult other = (ConsultaStockWSResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.cantPaginas==null && other.getCantPaginas()==null) || 
             (this.cantPaginas!=null &&
              this.cantPaginas.equals(other.getCantPaginas()))) &&
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
        int _hashCode = super.hashCode();
        if (getCantPaginas() != null) {
            _hashCode += getCantPaginas().hashCode();
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
        new org.apache.axis.description.TypeDesc(ConsultaStockWSResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://business.mywebservice.inssjp.com/", "consultaStockWSResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cantPaginas");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cantPaginas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("list");
        elemField.setXmlName(new javax.xml.namespace.QName("", "list"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://business.mywebservice.inssjp.com/", "consultaStockMedicamentoWS"));
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

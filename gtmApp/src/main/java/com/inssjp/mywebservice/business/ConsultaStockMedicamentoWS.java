/**
 * ConsultaStockMedicamentoWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.inssjp.mywebservice.business;

public class ConsultaStockMedicamentoWS  implements java.io.Serializable {
    private java.lang.String forma;

    private java.lang.String gln;

    private java.lang.String gtin;

    private java.lang.String lote;

    private java.lang.String nombre;

    private java.lang.Long p_unidades;

    private java.lang.String presentacion;

    private java.lang.String serie;

    public ConsultaStockMedicamentoWS() {
    }

    public ConsultaStockMedicamentoWS(
           java.lang.String forma,
           java.lang.String gln,
           java.lang.String gtin,
           java.lang.String lote,
           java.lang.String nombre,
           java.lang.Long p_unidades,
           java.lang.String presentacion,
           java.lang.String serie) {
           this.forma = forma;
           this.gln = gln;
           this.gtin = gtin;
           this.lote = lote;
           this.nombre = nombre;
           this.p_unidades = p_unidades;
           this.presentacion = presentacion;
           this.serie = serie;
    }


    /**
     * Gets the forma value for this ConsultaStockMedicamentoWS.
     * 
     * @return forma
     */
    public java.lang.String getForma() {
        return forma;
    }


    /**
     * Sets the forma value for this ConsultaStockMedicamentoWS.
     * 
     * @param forma
     */
    public void setForma(java.lang.String forma) {
        this.forma = forma;
    }


    /**
     * Gets the gln value for this ConsultaStockMedicamentoWS.
     * 
     * @return gln
     */
    public java.lang.String getGln() {
        return gln;
    }


    /**
     * Sets the gln value for this ConsultaStockMedicamentoWS.
     * 
     * @param gln
     */
    public void setGln(java.lang.String gln) {
        this.gln = gln;
    }


    /**
     * Gets the gtin value for this ConsultaStockMedicamentoWS.
     * 
     * @return gtin
     */
    public java.lang.String getGtin() {
        return gtin;
    }


    /**
     * Sets the gtin value for this ConsultaStockMedicamentoWS.
     * 
     * @param gtin
     */
    public void setGtin(java.lang.String gtin) {
        this.gtin = gtin;
    }


    /**
     * Gets the lote value for this ConsultaStockMedicamentoWS.
     * 
     * @return lote
     */
    public java.lang.String getLote() {
        return lote;
    }


    /**
     * Sets the lote value for this ConsultaStockMedicamentoWS.
     * 
     * @param lote
     */
    public void setLote(java.lang.String lote) {
        this.lote = lote;
    }


    /**
     * Gets the nombre value for this ConsultaStockMedicamentoWS.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this ConsultaStockMedicamentoWS.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the p_unidades value for this ConsultaStockMedicamentoWS.
     * 
     * @return p_unidades
     */
    public java.lang.Long getP_unidades() {
        return p_unidades;
    }


    /**
     * Sets the p_unidades value for this ConsultaStockMedicamentoWS.
     * 
     * @param p_unidades
     */
    public void setP_unidades(java.lang.Long p_unidades) {
        this.p_unidades = p_unidades;
    }


    /**
     * Gets the presentacion value for this ConsultaStockMedicamentoWS.
     * 
     * @return presentacion
     */
    public java.lang.String getPresentacion() {
        return presentacion;
    }


    /**
     * Sets the presentacion value for this ConsultaStockMedicamentoWS.
     * 
     * @param presentacion
     */
    public void setPresentacion(java.lang.String presentacion) {
        this.presentacion = presentacion;
    }


    /**
     * Gets the serie value for this ConsultaStockMedicamentoWS.
     * 
     * @return serie
     */
    public java.lang.String getSerie() {
        return serie;
    }


    /**
     * Sets the serie value for this ConsultaStockMedicamentoWS.
     * 
     * @param serie
     */
    public void setSerie(java.lang.String serie) {
        this.serie = serie;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultaStockMedicamentoWS)) return false;
        ConsultaStockMedicamentoWS other = (ConsultaStockMedicamentoWS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.forma==null && other.getForma()==null) || 
             (this.forma!=null &&
              this.forma.equals(other.getForma()))) &&
            ((this.gln==null && other.getGln()==null) || 
             (this.gln!=null &&
              this.gln.equals(other.getGln()))) &&
            ((this.gtin==null && other.getGtin()==null) || 
             (this.gtin!=null &&
              this.gtin.equals(other.getGtin()))) &&
            ((this.lote==null && other.getLote()==null) || 
             (this.lote!=null &&
              this.lote.equals(other.getLote()))) &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre()))) &&
            ((this.p_unidades==null && other.getP_unidades()==null) || 
             (this.p_unidades!=null &&
              this.p_unidades.equals(other.getP_unidades()))) &&
            ((this.presentacion==null && other.getPresentacion()==null) || 
             (this.presentacion!=null &&
              this.presentacion.equals(other.getPresentacion()))) &&
            ((this.serie==null && other.getSerie()==null) || 
             (this.serie!=null &&
              this.serie.equals(other.getSerie())));
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
        if (getForma() != null) {
            _hashCode += getForma().hashCode();
        }
        if (getGln() != null) {
            _hashCode += getGln().hashCode();
        }
        if (getGtin() != null) {
            _hashCode += getGtin().hashCode();
        }
        if (getLote() != null) {
            _hashCode += getLote().hashCode();
        }
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        if (getP_unidades() != null) {
            _hashCode += getP_unidades().hashCode();
        }
        if (getPresentacion() != null) {
            _hashCode += getPresentacion().hashCode();
        }
        if (getSerie() != null) {
            _hashCode += getSerie().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultaStockMedicamentoWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://business.mywebservice.inssjp.com/", "consultaStockMedicamentoWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("forma");
        elemField.setXmlName(new javax.xml.namespace.QName("", "forma"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gln");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gln"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gtin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gtin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lote");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lote"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_unidades");
        elemField.setXmlName(new javax.xml.namespace.QName("", "p_unidades"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presentacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "presentacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serie");
        elemField.setXmlName(new javax.xml.namespace.QName("", "serie"));
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

/**
 * MedicamentoPlain.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.inssjp.mywebservice.business;

public class MedicamentoPlain  implements java.io.Serializable {
    private java.lang.String descripcion;

    private java.lang.String fecha_alta;

    private java.lang.String fecha_baja;

    private java.lang.String fecha_ult_modificacion;

    private java.lang.String forma;

    private java.lang.String gln;

    private java.lang.String gtin;

    private com.inssjp.mywebservice.business.MonodrogaPlain[] monodrogas;

    private java.lang.String presentacion;

    private java.lang.String unidades;

    public MedicamentoPlain() {
    }

    public MedicamentoPlain(
           java.lang.String descripcion,
           java.lang.String fecha_alta,
           java.lang.String fecha_baja,
           java.lang.String fecha_ult_modificacion,
           java.lang.String forma,
           java.lang.String gln,
           java.lang.String gtin,
           com.inssjp.mywebservice.business.MonodrogaPlain[] monodrogas,
           java.lang.String presentacion,
           java.lang.String unidades) {
           this.descripcion = descripcion;
           this.fecha_alta = fecha_alta;
           this.fecha_baja = fecha_baja;
           this.fecha_ult_modificacion = fecha_ult_modificacion;
           this.forma = forma;
           this.gln = gln;
           this.gtin = gtin;
           this.monodrogas = monodrogas;
           this.presentacion = presentacion;
           this.unidades = unidades;
    }


    /**
     * Gets the descripcion value for this MedicamentoPlain.
     * 
     * @return descripcion
     */
    public java.lang.String getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this MedicamentoPlain.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the fecha_alta value for this MedicamentoPlain.
     * 
     * @return fecha_alta
     */
    public java.lang.String getFecha_alta() {
        return fecha_alta;
    }


    /**
     * Sets the fecha_alta value for this MedicamentoPlain.
     * 
     * @param fecha_alta
     */
    public void setFecha_alta(java.lang.String fecha_alta) {
        this.fecha_alta = fecha_alta;
    }


    /**
     * Gets the fecha_baja value for this MedicamentoPlain.
     * 
     * @return fecha_baja
     */
    public java.lang.String getFecha_baja() {
        return fecha_baja;
    }


    /**
     * Sets the fecha_baja value for this MedicamentoPlain.
     * 
     * @param fecha_baja
     */
    public void setFecha_baja(java.lang.String fecha_baja) {
        this.fecha_baja = fecha_baja;
    }


    /**
     * Gets the fecha_ult_modificacion value for this MedicamentoPlain.
     * 
     * @return fecha_ult_modificacion
     */
    public java.lang.String getFecha_ult_modificacion() {
        return fecha_ult_modificacion;
    }


    /**
     * Sets the fecha_ult_modificacion value for this MedicamentoPlain.
     * 
     * @param fecha_ult_modificacion
     */
    public void setFecha_ult_modificacion(java.lang.String fecha_ult_modificacion) {
        this.fecha_ult_modificacion = fecha_ult_modificacion;
    }


    /**
     * Gets the forma value for this MedicamentoPlain.
     * 
     * @return forma
     */
    public java.lang.String getForma() {
        return forma;
    }


    /**
     * Sets the forma value for this MedicamentoPlain.
     * 
     * @param forma
     */
    public void setForma(java.lang.String forma) {
        this.forma = forma;
    }


    /**
     * Gets the gln value for this MedicamentoPlain.
     * 
     * @return gln
     */
    public java.lang.String getGln() {
        return gln;
    }


    /**
     * Sets the gln value for this MedicamentoPlain.
     * 
     * @param gln
     */
    public void setGln(java.lang.String gln) {
        this.gln = gln;
    }


    /**
     * Gets the gtin value for this MedicamentoPlain.
     * 
     * @return gtin
     */
    public java.lang.String getGtin() {
        return gtin;
    }


    /**
     * Sets the gtin value for this MedicamentoPlain.
     * 
     * @param gtin
     */
    public void setGtin(java.lang.String gtin) {
        this.gtin = gtin;
    }


    /**
     * Gets the monodrogas value for this MedicamentoPlain.
     * 
     * @return monodrogas
     */
    public com.inssjp.mywebservice.business.MonodrogaPlain[] getMonodrogas() {
        return monodrogas;
    }


    /**
     * Sets the monodrogas value for this MedicamentoPlain.
     * 
     * @param monodrogas
     */
    public void setMonodrogas(com.inssjp.mywebservice.business.MonodrogaPlain[] monodrogas) {
        this.monodrogas = monodrogas;
    }


    /**
     * Gets the presentacion value for this MedicamentoPlain.
     * 
     * @return presentacion
     */
    public java.lang.String getPresentacion() {
        return presentacion;
    }


    /**
     * Sets the presentacion value for this MedicamentoPlain.
     * 
     * @param presentacion
     */
    public void setPresentacion(java.lang.String presentacion) {
        this.presentacion = presentacion;
    }


    /**
     * Gets the unidades value for this MedicamentoPlain.
     * 
     * @return unidades
     */
    public java.lang.String getUnidades() {
        return unidades;
    }


    /**
     * Sets the unidades value for this MedicamentoPlain.
     * 
     * @param unidades
     */
    public void setUnidades(java.lang.String unidades) {
        this.unidades = unidades;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MedicamentoPlain)) return false;
        MedicamentoPlain other = (MedicamentoPlain) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.descripcion==null && other.getDescripcion()==null) || 
             (this.descripcion!=null &&
              this.descripcion.equals(other.getDescripcion()))) &&
            ((this.fecha_alta==null && other.getFecha_alta()==null) || 
             (this.fecha_alta!=null &&
              this.fecha_alta.equals(other.getFecha_alta()))) &&
            ((this.fecha_baja==null && other.getFecha_baja()==null) || 
             (this.fecha_baja!=null &&
              this.fecha_baja.equals(other.getFecha_baja()))) &&
            ((this.fecha_ult_modificacion==null && other.getFecha_ult_modificacion()==null) || 
             (this.fecha_ult_modificacion!=null &&
              this.fecha_ult_modificacion.equals(other.getFecha_ult_modificacion()))) &&
            ((this.forma==null && other.getForma()==null) || 
             (this.forma!=null &&
              this.forma.equals(other.getForma()))) &&
            ((this.gln==null && other.getGln()==null) || 
             (this.gln!=null &&
              this.gln.equals(other.getGln()))) &&
            ((this.gtin==null && other.getGtin()==null) || 
             (this.gtin!=null &&
              this.gtin.equals(other.getGtin()))) &&
            ((this.monodrogas==null && other.getMonodrogas()==null) || 
             (this.monodrogas!=null &&
              java.util.Arrays.equals(this.monodrogas, other.getMonodrogas()))) &&
            ((this.presentacion==null && other.getPresentacion()==null) || 
             (this.presentacion!=null &&
              this.presentacion.equals(other.getPresentacion()))) &&
            ((this.unidades==null && other.getUnidades()==null) || 
             (this.unidades!=null &&
              this.unidades.equals(other.getUnidades())));
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
        if (getDescripcion() != null) {
            _hashCode += getDescripcion().hashCode();
        }
        if (getFecha_alta() != null) {
            _hashCode += getFecha_alta().hashCode();
        }
        if (getFecha_baja() != null) {
            _hashCode += getFecha_baja().hashCode();
        }
        if (getFecha_ult_modificacion() != null) {
            _hashCode += getFecha_ult_modificacion().hashCode();
        }
        if (getForma() != null) {
            _hashCode += getForma().hashCode();
        }
        if (getGln() != null) {
            _hashCode += getGln().hashCode();
        }
        if (getGtin() != null) {
            _hashCode += getGtin().hashCode();
        }
        if (getMonodrogas() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMonodrogas());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMonodrogas(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPresentacion() != null) {
            _hashCode += getPresentacion().hashCode();
        }
        if (getUnidades() != null) {
            _hashCode += getUnidades().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MedicamentoPlain.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://business.mywebservice.inssjp.com/", "medicamentoPlain"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_alta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha_alta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_baja");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha_baja"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_ult_modificacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha_ult_modificacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("monodrogas");
        elemField.setXmlName(new javax.xml.namespace.QName("", "monodrogas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://business.mywebservice.inssjp.com/", "monodrogaPlain"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "monodroga"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presentacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "presentacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unidades");
        elemField.setXmlName(new javax.xml.namespace.QName("", "unidades"));
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

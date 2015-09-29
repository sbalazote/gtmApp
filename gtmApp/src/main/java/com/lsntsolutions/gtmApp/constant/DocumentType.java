package com.lsntsolutions.gtmApp.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DocumentType {
    public static Map<Integer, String> types = null;
    static {
        Map<Integer, String> aMap = new HashMap<>();
        aMap.put(93, "Acta nacimiento");
        aMap.put(87, "CDI");
        aMap.put(95, "CI Bs. As. RNP");
        aMap.put(1, "CI Buenos Aires");
        aMap.put(3, "CI C&oacute;rdoba");
        aMap.put(2, "CI Catamarca");
        aMap.put(16, "CI Chaco");
        aMap.put(17, "CI Chubut");
        aMap.put(4, "CI Corrientes");
        aMap.put(5, "CI Entre R&iacute;os");
        aMap.put(91, "CI extranjera");
        aMap.put(18, "CI Formosa");
        aMap.put(6, "CI Jujuy");
        aMap.put(21, "CI La Pampa");
        aMap.put(8, "CI La Rioja");
        aMap.put(7, "CI Mendoza");
        aMap.put(19, "CI Misiones");
        aMap.put(20, "CI Neuqu&eacute;n");
        aMap.put(0, "CI Polic&iacute;a Federal");
        aMap.put(22, "CI R&iacute;o Negro");
        aMap.put(9, "CI Salta");
        aMap.put(10, "CI San Juan");
        aMap.put(11, "CI San Luis");
        aMap.put(23, "CI Santa Cruz");
        aMap.put(12, "CI Santa Fe");
        aMap.put(13, "CI Santiago del Estero");
        aMap.put(24, "CI Tierra del Fuego");
        aMap.put(14, "CI Tucum&aacute;n");
        aMap.put(86, "CUIL");
        aMap.put(80, "CUIT");
        aMap.put(96, "DNI");
        aMap.put(92, "en tr&aacute;mite");
        aMap.put(90, "LC");
        aMap.put(89, "LE");
        aMap.put(94, "Pasaporte");
        aMap.put(99, "Sin identificar");

        types = Collections.unmodifiableMap(aMap);
    }
}


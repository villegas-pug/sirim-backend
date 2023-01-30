package com.microservicio.rrhh.constants;

import java.util.HashMap;
import java.util.Map;

public class TipoLicenciaMap {
   
   public static final Map<String, Integer> TipoLicencia = new HashMap<>(){
      { put("LICENCIA POR SALUD (CERTIFICADO DE INCAPACIDAD - CITT O CERTIF. MED.)", 10); }
      { put("LICENCIA POR ASUNTOS PARTICULARES", 12); }
      { put("LICENCIA POR ENFERMEDAD GRAVE DE FAMILIAR DIRECTO", 14); }
      { put("LICENCIA POR ONOMASTICO", 16); }
      { put("LICENCIA POR FALLECIMIENTO DE FAMILIAR DIRECTO", 18); }
      { put("LICENCIA POR COMISION DEL SERVICIO", 20); }
      { put("LICENCIA POR COMPENSACION DE HORAS EXTRAORDINARIAS NO REMUNERADAS", 22); }
      { put("PERMISO POR COMPENSACION DE HORAS EXTRAORDINARIAS NO REMUNERADAS", 24); }
      { put("PERMISO POR ASUNTOS PARTICULARES", 26); }
      { put("PERMISO PARA EJERCER DOCENCIA", 28); }
      { put("PERMISO POR CAPACITACION", 30); }
      { put("PERMISO POR CITACION EXPRESA (PNP, JUDICIAL etc)", 32); }
      { put("PERMISO POR COMISION DEL SERVICIO", 34); }
      { put("PERMISO POR ATENCION MEDICA ACREDITADA (PREVIA CITA)", 36); }
      { put("OTROS", 38); }
   };

}

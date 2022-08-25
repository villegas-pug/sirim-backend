package com.microservicio.rimextraccion.services;

import java.util.List;
import java.util.Map;
import com.microservicio.rimextraccion.models.dto.AsigGrupoCamposAnalisisDto;
import com.microservicio.rimextraccion.models.dto.RecordAssignedDto;

public interface RimctrlCalCamposAnalisisService {

   /* ► REPO - METHOD'S ... */
   void saveCtrlCalCamposAnalisis(Long idAsigGrupo);
   void validateRecordAssigned(RecordAssignedDto recordAssignedDto);

   /* ► CUSTOM - METHOD'S ...  */
   String generateIdsCsvForCtrlCal(String nombreTabla, int regIni, int regFIn);
   List<Map<String, Object>> findTablaDinamicaByNameAndIds(String nombreTabla, String idsCsv);
   void conformToCtrlCal(AsigGrupoCamposAnalisisDto asigGrupoCamposAnalisisDto);


   
}
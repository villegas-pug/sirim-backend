package com.microservicio.rimctrlcalidad.services;

import java.util.List;
import java.util.Map;
import com.commons.utils.models.dto.AsigGrupoCamposAnalisisDto;
import com.commons.utils.models.dto.RegistroTablaDinamicaDto;
import com.commons.utils.models.dto.TablaDinamicaDto;

public interface RimctrlCalidadService {

   // ► Repo method's ...
   void saveCtrlCalCamposAnalisis(Long idAsigGrupo);
   void validateRecordAssigned(RegistroTablaDinamicaDto recordAssignedDto);
   void conformToCtrlCal(AsigGrupoCamposAnalisisDto asigGrupoCamposAnalisisDto);
   void saveMetaFieldIdErrorCsv(RegistroTablaDinamicaDto registroTablaDinamica);
   void saveRectificadoRecordAssigned(Long idProdAnalisis);
   AsigGrupoCamposAnalisisDto findAsigGrupoCamposAnalisisById(Long idAsigGrupo);

   // ► Client-Rest method's ...
   List<TablaDinamicaDto> findAllTablaDinamica();
   List<Map<String, Object>> findTablaDinamicaByIdCtrlCalAndIds(Long idCtrlCal, String idsCsv);
   String generateIdsCsvForCtrlCal(String nombreTabla, int regIni, int regFIn);

   // ► Custom method's ...
   
}
package com.microservicio.rimcommon.services;

import java.util.List;
import java.util.Map;
import com.commons.utils.models.dto.AsigGrupoCamposAnalisisDto;
import com.commons.utils.models.dto.GrupoCamposAnalisisDto;
import com.commons.utils.models.dto.RegistroTablaDinamicaDto;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.models.entities.Usuario;

public interface RimcommonService {
   
   // ► Repository - Method's ...
   List<Map<String, Object>> findDynamicSelectStatement(String queryString);
   List<TablaDinamicaDto> findAllTablaDinamica();
   List<Map<String, Object>> findTablaDinamicaBySuffixOfField(String nombreTabla, String suffix);
   Long countTablaByNombre(String nombreTabla);
   void saveRecordAssigned(RegistroTablaDinamicaDto recordAssignedDto);
   List<TablaDinamicaDto> findTablaDinamicaByUsrCreador(Usuario usrCreador);
   TablaDinamicaDto findTablaDinamicaByNombre(String nombre);
   List<TablaDinamicaDto> findAllTablaDinamicaOnlyNombres();
   List<AsigGrupoCamposAnalisisDto> findAsigByUsrAnalista(Usuario usuario, boolean unfinished);
   AsigGrupoCamposAnalisisDto findAsigById(Long id);

   GrupoCamposAnalisisDto findGrupoCamposAnalisisById(Long idGrupo);
   void deleteGrupoCamposAnalisisbyId(Long idGrupo);
   List<Map<String, Object>> findTablaDinamicaByRangoFromIds(long idAsigGrupo, Long rIni, Long rFin);
   
   // ► Rest-Client method's ...
   // ► Custom - Method's ...

}

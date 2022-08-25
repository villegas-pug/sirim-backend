package com.microservicio.rimextraccion.services;

import java.util.List;
import java.util.Map;

import com.microservicio.rimextraccion.models.dto.AsigGrupoCamposAnalisisDto;
import com.microservicio.rimextraccion.models.dto.TablaDinamicaDto;

public interface RimcommonService {
   
   /*► Repository - Method's ...  */
   List<Map<String, Object>> findDynamicSelectStatement(String queryString);
   List<TablaDinamicaDto> findAllTablaDinamica();
   List<Map<String, Object>> findTablaDinamicaByRangoFromIds(String nombreTabla, Long rIni, Long rFin);
   
   /*► Custom - Method's ...  */
   AsigGrupoCamposAnalisisDto setTotalsPropsToAsigGrupoCamposAnalisis (AsigGrupoCamposAnalisisDto assig);

}

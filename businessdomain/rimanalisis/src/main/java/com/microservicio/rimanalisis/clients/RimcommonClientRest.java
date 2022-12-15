package com.microservicio.rimanalisis.clients;

import java.util.List;
import java.util.Map;

import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.utils.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "microservicio-rimcommon")
public interface RimcommonClientRest {
   
   @GetMapping( path = { "/findAllTablaDinamica" } )
   Response<List<TablaDinamicaDto>> findAllTablaDinamica();

   @GetMapping( path = { "/findTablaDinamicaByNombre" } )
   Response<TablaDinamicaDto> findTablaDinamicaByNombre(@RequestParam(name = "nombreTabla") String nombreTabla);

   @GetMapping(path = { "/findTablaDinamicaByRangoFromIds" })
   Response<List<Map<String, Object>>> findTablaDinamicaByRangoFromIds(
                                                         @RequestParam(name = "idAsigGrupo") long idAsigGrupo, 
                                                         @RequestParam(name = "regAnalisisIni") long rIni, 
                                                         @RequestParam(name = "regAnalisisFin") long rFin);

   @GetMapping( path = { "/findDynamicSelectStatement" } )
   Response<List<Map<String, Object>>> findDynamicSelectStatement(@RequestParam(name = "queryString") String queryString);

}

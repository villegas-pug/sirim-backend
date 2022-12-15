package com.microservicio.rimctrlcalidad.clients;

import java.util.List;
import java.util.Map;
import com.commons.utils.models.dto.RegistroTablaDinamicaDto;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.utils.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "microservicio-rimcommon")
public interface RimcommonClientRest {

   @GetMapping( path = { "/findAllTablaDinamica" } )
   Response<List<TablaDinamicaDto>> findAllTablaDinamica();

   @PutMapping( path = { "/saveRecordAssigned" } )
   public Response<List<Map<String, Object>>> saveRecordAssigned(@RequestBody RegistroTablaDinamicaDto registroTablaDinamicaDto);

   @GetMapping( path = { "/findDynamicSelectStatement" } )
   public Response<List<Map<String, Object>>> findDynamicSelectStatement(@RequestParam(name = "queryString") String queryString);

   @GetMapping( path = { "/findTablaDinamicaByNombre" } )
   public Response<TablaDinamicaDto> findTablaDinamicaByNombre(@RequestParam(name = "nombreTabla") String nombreTabla);

}
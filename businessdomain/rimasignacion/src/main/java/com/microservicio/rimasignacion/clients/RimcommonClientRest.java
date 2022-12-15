package com.microservicio.rimasignacion.clients;

import java.util.List;
import com.commons.utils.models.dto.GrupoCamposAnalisisDto;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.utils.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "microservicio-rimcommon", decode404 = true)
public interface RimcommonClientRest {

   @GetMapping( path = { "/findAllTablaDinamica" }, produces = { MediaType.APPLICATION_JSON_VALUE } )
   public Response<List<TablaDinamicaDto>> findAllTablaDinamica();

   @GetMapping( path = { "/findGrupoCamposAnalisisById" }, produces = { MediaType.APPLICATION_JSON_VALUE } )
   public Response<GrupoCamposAnalisisDto> findGrupoCamposAnalisisById(@RequestParam(name = "idGrupo") Long idGrupo);

   @GetMapping( path = { "/countTablaByNombre" } )
   public Response<Long> countTablaByNombre(@RequestParam(name = "nombreTabla") String nombreTabla);

}

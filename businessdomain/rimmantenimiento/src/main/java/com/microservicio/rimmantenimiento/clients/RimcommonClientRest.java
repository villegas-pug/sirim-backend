package com.microservicio.rimmantenimiento.clients;

import java.util.List;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.utils.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "microservicio-rimcommon")
public interface RimcommonClientRest {

   @GetMapping( path = { "/findAllTablaDinamica" } )
   public Response<List<TablaDinamicaDto>> findAllTablaDinamica();
   
}

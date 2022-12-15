package com.microservicio.rimextraccion.clients;

import java.util.List;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservicio-rimcommon")
public interface RimcommonClientRest {
   
   @GetMapping( path = { "/findAllTablaDinamica" } )
   public Response<List<TablaDinamicaDto>> findAllTablaDinamica();

   @PostMapping( path = { "/findTablaDinamicaByUsrCreador" }, produces = { MediaType.APPLICATION_JSON_VALUE })
   public Response<List<TablaDinamicaDto>> findTablaDinamicaByUsrCreador(@RequestBody Usuario usrCreador);

}

package com.microservicios.operativo.clients;

import java.util.Optional;
import com.commons.utils.models.entities.Pais;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservicio-pais")
public interface PaisClient {

   @GetMapping(path = "/findByNacionalidad/{nacionalidad}")
   public Optional<Pais> findByNacionalidad(@PathVariable String nacionalidad);

}

package com.microservicio.produccion.client;

import java.util.Optional;

import com.commons.utils.models.entities.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservicio-usuario")
public interface UsuarioClient {
   
   @GetMapping(path = "/findByUserAuth/{userAuth}")
   Optional<Usuario> findByUserAuth(@PathVariable String userAuth);
}

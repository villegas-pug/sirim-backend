package com.microservicio.rimextraccion.clients;

import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservicio-usuario")
public interface UsuarioClientRest {
   
   @GetMapping(path = "/findByLogin/{login}")
   public Response<Usuario> findByLogin(@PathVariable(name = "login") String login);

}

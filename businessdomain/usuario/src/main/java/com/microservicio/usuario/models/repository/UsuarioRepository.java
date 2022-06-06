package com.microservicio.usuario.models.repository;

import java.util.Optional;
import java.util.UUID;

import com.commons.utils.models.entities.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID>{

   Optional<Usuario> findByLogin(String login);
   
}

package com.microservicio.usuario.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.commons.utils.models.entities.Usuario;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsuarioService extends UserDetailsService {
   Optional<Usuario> findByLogin(String login);
   Optional<Usuario> findById(UUID id);
   List<Usuario> findAll();
   Usuario save(Usuario usuario);
   void deleteByLogin(String login);
}

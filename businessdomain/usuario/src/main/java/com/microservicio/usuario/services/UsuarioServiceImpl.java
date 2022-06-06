package com.microservicio.usuario.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.commons.utils.models.entities.Usuario;
import com.microservicio.usuario.models.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

   @Autowired
   private UsuarioRepository repository;
   
   @Override
   @Transactional(readOnly = true)
   public Optional<Usuario> findByLogin(String login) {
      return repository.findByLogin(login);
   }

   @Override
   @Transactional(readOnly = true)
   public List<Usuario> findAll() {
      return this.repository.findAll();
   }

   @Override
   @Transactional
   public Usuario save(Usuario usuario) {
      return repository.save(usuario);
   }

   @Override
   public void deleteByLogin(String login) {

   }

   @Override
   @Transactional(readOnly = true)
   public Optional<Usuario> findById(UUID id) {
      return this.repository.findById(id);
   }

   @Override
   public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
      Usuario usuario = repository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
      return new User(usuario.getLogin(), usuario.getPassword(), true, true, true, true, new ArrayList<>());
   }
}
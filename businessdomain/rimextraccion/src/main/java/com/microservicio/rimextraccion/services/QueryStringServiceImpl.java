package com.microservicio.rimextraccion.services;

import java.util.Optional;
import com.microservicio.rimextraccion.models.entities.QueryString;
import com.microservicio.rimextraccion.repository.QueryStringRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QueryStringServiceImpl implements QueryStringService {

   @Autowired
   private QueryStringRepository repository;

   @Override
   @Transactional
   public void save(QueryString queryString) {
      this.repository.save(queryString);
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<QueryString> findByNombre(String nombre) {
      return this.repository.findByNombre(nombre);
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<QueryString> findById(Long id) {
      return this.repository.findById(id);
   }

   @Override
   @Transactional
   public void deleteById(Long id) {
      this.repository.deleteById(id);
   }
}
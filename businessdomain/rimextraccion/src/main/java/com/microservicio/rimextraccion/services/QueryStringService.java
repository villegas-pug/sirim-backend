package com.microservicio.rimextraccion.services;

import java.util.Optional;

import com.microservicio.rimextraccion.models.entities.QueryString;

public interface QueryStringService {

   void save(QueryString queryString);

   Optional<QueryString> findByNombre(String nombre);

   Optional<QueryString> findById(Long id);

   void deleteById(Long id);
   
}
package com.microservicio.rimextraccion.repository;

import java.util.Optional;

import com.microservicio.rimextraccion.models.entities.QueryString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryStringRepository extends JpaRepository<QueryString, Long>  {

   Optional<QueryString> findByNombre(String nombre);
   
}
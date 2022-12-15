package com.microservicio.rimextraccion.repository;

import com.commons.utils.models.entities.BaseDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseDatosRepository extends JpaRepository<BaseDatos, Long> {

   
}
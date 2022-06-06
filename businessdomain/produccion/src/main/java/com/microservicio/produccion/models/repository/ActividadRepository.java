package com.microservicio.produccion.models.repository;

import com.microservicio.produccion.models.entities.Actividad;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {
   
}
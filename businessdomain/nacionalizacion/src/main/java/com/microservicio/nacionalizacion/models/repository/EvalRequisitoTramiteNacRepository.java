package com.microservicio.nacionalizacion.models.repository;

import com.microservicio.nacionalizacion.models.entities.EvalRequisitoTramiteNac;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvalRequisitoTramiteNacRepository extends JpaRepository<EvalRequisitoTramiteNac, Long> {

   
}
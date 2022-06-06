package com.microservicio.nacionalizacion.models.repository;

import com.microservicio.nacionalizacion.models.entities.EvaluarTramiteNac;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluarTramiteNacRepository extends JpaRepository<EvaluarTramiteNac, Long> {

   
}
package com.microservicio.nacionalizacion.models.repository;

import java.util.Optional;

import com.microservicio.nacionalizacion.models.entities.NuevoTramiteNac;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NuevoTramiteNacRepository extends JpaRepository<NuevoTramiteNac, Long> {

   Optional<NuevoTramiteNac> findByNumeroTramite(String numeroTramite);
   
}
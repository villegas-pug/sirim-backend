package com.microservicio.nacionalizacion.models.repository;

import com.microservicio.nacionalizacion.models.entities.UbicacionExpediente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionExpedienteRepository extends JpaRepository<UbicacionExpediente, Long> {
   
}

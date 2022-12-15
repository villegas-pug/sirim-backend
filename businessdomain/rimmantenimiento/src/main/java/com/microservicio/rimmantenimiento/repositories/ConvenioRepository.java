package com.microservicio.rimmantenimiento.repositories;

import com.microservicio.rimmantenimiento.models.entities.Convenio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConvenioRepository extends JpaRepository<Convenio, Long> {
  
}

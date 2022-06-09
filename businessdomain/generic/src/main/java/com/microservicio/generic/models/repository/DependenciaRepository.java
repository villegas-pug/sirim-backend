package com.microservicio.generic.models.repository;

import com.commons.utils.models.entities.Dependencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DependenciaRepository extends JpaRepository<Dependencia, Long> {
   
}

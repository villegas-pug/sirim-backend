package com.microservicio.rimreglanegocio.repositories;


import com.commons.utils.models.entities.RNProceso;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RNProcesoRepository extends JpaRepository<RNProceso, Long> {

}

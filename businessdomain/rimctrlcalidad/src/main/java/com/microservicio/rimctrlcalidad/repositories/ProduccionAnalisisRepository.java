package com.microservicio.rimctrlcalidad.repositories;

import com.commons.utils.models.entities.ProduccionAnalisis;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduccionAnalisisRepository extends JpaRepository<ProduccionAnalisis, Long> {
   
}

package com.microservicio.rimcommon.repositories;

import com.commons.utils.models.entities.GrupoCamposAnalisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoCamposAnalisisRepository extends JpaRepository<GrupoCamposAnalisis, Long>{

}
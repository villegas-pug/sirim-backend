package com.microservicio.rimreglanegocio.repositories;

import com.commons.utils.models.entities.RNDimension;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RNDimensionRepository extends JpaRepository<RNDimension, Long> {
   
}

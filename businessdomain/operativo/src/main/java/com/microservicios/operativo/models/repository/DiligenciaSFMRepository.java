package com.microservicios.operativo.models.repository;

import com.microservicios.operativo.models.entities.DiligenciaSFM;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiligenciaSFMRepository extends JpaRepository<DiligenciaSFM, Long> {
   
}

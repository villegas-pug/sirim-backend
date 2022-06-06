package com.microservicios.operativo.models.repository;

import com.microservicios.operativo.models.entities.ArchivoSFM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivoSFMRepository extends JpaRepository<ArchivoSFM, Long> {
   
}

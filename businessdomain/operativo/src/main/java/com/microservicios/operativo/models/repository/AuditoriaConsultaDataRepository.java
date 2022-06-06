package com.microservicios.operativo.models.repository;

import com.commons.utils.models.entities.AuditoriaConsultaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaConsultaDataRepository extends JpaRepository<AuditoriaConsultaData, Long> {
   
}

package com.microservicios.operativo.models.repository;

import com.microservicios.operativo.models.entities.BandejaDocSFM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BandejaDocSFMRepository extends JpaRepository<BandejaDocSFM, Long> {
   
}
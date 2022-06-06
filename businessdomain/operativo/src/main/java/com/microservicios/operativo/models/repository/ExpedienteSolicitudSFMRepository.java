package com.microservicios.operativo.models.repository;

import java.util.List;
import java.util.Optional;
import com.microservicios.operativo.models.dto.MetadataFilesExpedienteSolicitudDto;
import com.microservicios.operativo.models.entities.ExpedienteSolicitudSFM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpedienteSolicitudSFMRepository extends JpaRepository<ExpedienteSolicitudSFM, Long> {


   @Query( value = "{CALL dbo.spu_Sid_Rpt_MetadataFilesExpedienteSolicitud}", nativeQuery = true )
   List<MetadataFilesExpedienteSolicitudDto> getMetadataFilesExpedienteSolicitud();
   
   @Query( 
      value = "SELECT TOP 1 * FROM SidExpedienteSolicitudSFM ses WHERE ses.sNumeroExpediente = :numeroExpediente ORDER BY ses.dFechaRegistro DESC",
      nativeQuery = true)
   Optional<ExpedienteSolicitudSFM> findByNumeroExpediente(@Param("numeroExpediente") String numeroExpediente);

}
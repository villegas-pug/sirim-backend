package com.microservicios.operativo.models.repository;

import java.util.List;

import com.microservicios.operativo.models.entities.PreInscripcion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PreInscripcionRepository extends JpaRepository<PreInscripcion, Long> {

   @Query(value = "{CALL dbo.spu_Sid_util_BuscarPreinscripcion(:nombre, :apePat, :apeMat, :nroDoc)}", nativeQuery = true)
   List<PreInscripcion> findByNombresOrDocumento(
                                 @Param("nombre") String nombres, 
                                 @Param("apePat") String apePat,
                                 @Param("apeMat") String apeMat,
                                 @Param("nroDoc") String nroDoc);
}
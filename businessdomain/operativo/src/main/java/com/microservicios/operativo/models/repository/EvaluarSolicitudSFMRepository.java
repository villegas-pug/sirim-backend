package com.microservicios.operativo.models.repository;

import java.util.List;

import com.commons.utils.models.entities.Etapa;
import com.microservicios.operativo.models.entities.EvaluarSolicitudSFM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluarSolicitudSFMRepository extends JpaRepository<EvaluarSolicitudSFM, Long> {

   @Query("SELECT new Etapa(e.idEtapa, e.descripcion, e.activo, e.fechaHoraAud) FROM Etapa e")
   List<Etapa> findAllEtapa();
   
}
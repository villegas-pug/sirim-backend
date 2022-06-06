package com.microservicio.nacionalizacion.models.repository;

import java.util.Optional;

import com.commons.utils.models.entities.Etapa;
import com.microservicio.nacionalizacion.models.entities.EtapaTramiteNac;
import com.microservicio.nacionalizacion.models.entities.NuevoTramiteNac;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EtapaTramiteNacRepository extends JpaRepository<EtapaTramiteNac, Long> {

   Optional<EtapaTramiteNac> findByEtapa(Etapa etapa);
   Optional<EtapaTramiteNac> findByTramiteNac(NuevoTramiteNac tramiteNac);

   @Query(value = "SELECT * FROM SidEtapaTramiteNac WHERE nIdTramiteNac = ? AND nIdEtapa = ?", nativeQuery = true)
   Optional<EtapaTramiteNac> findByIdTramiteNacAndIdEtapa(Long idTramiteNac, Long idEtapa);
   
}
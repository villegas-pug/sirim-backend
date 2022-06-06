package com.microservicio.nacionalizacion.models.repository;

import java.util.List;

import com.commons.utils.models.entities.TipoTramite;
import com.microservicio.nacionalizacion.models.entities.RequisitoTipoTramite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequisitoTipoTramiteRepository extends JpaRepository<RequisitoTipoTramite, Long>{

   List<RequisitoTipoTramite> findByTipoTramite(TipoTramite tipoTramite);
   
}
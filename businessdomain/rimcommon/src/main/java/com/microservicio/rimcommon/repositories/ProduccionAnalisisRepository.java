package com.microservicio.rimcommon.repositories;

import java.util.Optional;
import com.commons.utils.models.entities.AsigGrupoCamposAnalisis;
import com.commons.utils.models.entities.ProduccionAnalisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduccionAnalisisRepository extends JpaRepository<ProduccionAnalisis, Long> {

   Optional<ProduccionAnalisis> findByAsigGrupoAndIdRegistroAnalisis(AsigGrupoCamposAnalisis asigGrupoCamposAnalisis, Long idRegistroAnalisis);
   
}
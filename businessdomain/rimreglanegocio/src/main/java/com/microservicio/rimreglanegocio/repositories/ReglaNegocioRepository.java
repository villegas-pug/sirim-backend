package com.microservicio.rimreglanegocio.repositories;

import com.commons.utils.models.entities.RNProceso;
import com.commons.utils.models.entities.ReglaNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ReglaNegocioRepository extends JpaRepository<ReglaNegocio, String> {

   List<ReglaNegocio> findByProceso(RNProceso proceso);

   @Query(value = "{CALL dbo.up_Rim_RN_InsertaRegistroEjecucionScript(?)}", nativeQuery = true)
   Long createOneRegistroEjecucionScript(Long idRNControlCambio);

}

package com.microservicio.rimextraccion.repository;

import java.util.List;
import javax.persistence.Tuple;
import com.commons.utils.models.entities.Usuario;
import com.microservicio.rimextraccion.models.entities.AsigGrupoCamposAnalisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RimasigGrupoCamposAnalisisRepository extends JpaRepository<AsigGrupoCamposAnalisis, Long> {

   List<AsigGrupoCamposAnalisis> findByUsrAnalista(Usuario usuario);

   @Query(value = "{CALL dbo.usp_Rim_Procedimiento_ListarTablaDinamicaPorRangos(?, ?, ?)}", nativeQuery = true)
   List<Tuple> findTablaDinamicaByRangoFromIds(String nombreTabla, Long rIni, Long rFin);

}

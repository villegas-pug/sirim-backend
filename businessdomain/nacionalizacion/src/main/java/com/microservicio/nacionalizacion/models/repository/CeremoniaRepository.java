package com.microservicio.nacionalizacion.models.repository;

import java.util.List;
import com.commons.utils.models.dto.IndicadoresCeremoniaDto;
import com.microservicio.nacionalizacion.models.entities.Ceremonia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CeremoniaRepository extends JpaRepository<Ceremonia, Long> {

   @Query(value = "{CALL dbo.usp_Sid_Proc_SumarizzeCeremonia}", nativeQuery = true)
   List<IndicadoresCeremoniaDto> getSumarizzeCeremonia();
}
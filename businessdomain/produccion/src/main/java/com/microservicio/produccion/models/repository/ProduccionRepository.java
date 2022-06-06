package com.microservicio.produccion.models.repository;

import java.util.Date;
import java.util.List;
import com.commons.utils.models.dto.ProduccionDiariaDto;
import com.commons.utils.models.dto.ProduccionSemanalDto;
import com.commons.utils.models.entities.Usuario;
import com.microservicio.produccion.models.entities.Produccion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProduccionRepository extends JpaRepository<Produccion, Long> {

   List<Produccion> findByUsuarioAndFechaRegistro(Usuario usuario, Date fechaRegistro);

   @Query(value = "{CALL dbo.spu_Sid_Produccion_ContarActividadSemanaActual(:loginName)}", nativeQuery = true)
   List<ProduccionDiariaDto> countActividadCurrentWeek(String loginName);

   @Query(value = "{CALL dbo.spu_Sid_Rpt_ActividadSemanal(:refDate)}", nativeQuery = true)
   List<ProduccionSemanalDto> countActividadSemanalByDate(Date refDate);
   
}

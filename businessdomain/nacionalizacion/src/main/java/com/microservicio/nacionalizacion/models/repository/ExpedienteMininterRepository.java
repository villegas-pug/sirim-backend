package com.microservicio.nacionalizacion.models.repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;
import com.commons.utils.models.entities.Usuario;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import com.microservicio.nacionalizacion.models.dto.DetExpedienteMininterDto;
import com.microservicio.nacionalizacion.models.entities.ExpedienteMininter;
import com.microservicio.nacionalizacion.models.entities.UbicacionExpediente;

@Repository
public interface ExpedienteMininterRepository extends JpaRepository<ExpedienteMininter, Long> {

   Optional<ExpedienteMininter> findByNumeroExpediente(String numeroExpediente);
   Optional<ExpedienteMininter> findByNumeroExpedienteAndUsuarioDigita(String numeroExpediente, Usuario usuarioDigita);
   List<ExpedienteMininter> findByUbicacion(UbicacionExpediente ubicacion);

   @Query(value = "{CALL dbo.usp_Sid_Rpt_ObtenerFueraPlazoOficio(:idUsuario)}", nativeQuery = true)
   List<DetExpedienteMininterDto> findAllFueraPlazoOficio(UUID idUsuario);
   
}
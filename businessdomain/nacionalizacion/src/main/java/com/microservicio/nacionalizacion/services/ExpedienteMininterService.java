package com.microservicio.nacionalizacion.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.commons.utils.models.entities.Usuario;
import com.microservicio.nacionalizacion.models.dto.DetExpedienteMininterDto;
import com.microservicio.nacionalizacion.models.entities.ExpedienteMininter;
import com.microservicio.nacionalizacion.models.entities.UbicacionExpediente;

public interface ExpedienteMininterService {
   ExpedienteMininter save(ExpedienteMininter expedienteMininter);
   Optional<ExpedienteMininter> findById(Long idExpMininter);
   Optional<ExpedienteMininter> findByNumeroExpediente(String numeroExpediente);
   Optional<ExpedienteMininter> findByNumeroExpedienteAndUsuarioDigita(String numeroExpediente, Usuario usuarioDigita);
   List<ExpedienteMininter> findAll();
   void deleteById(Long idExpMininter);
   List<ExpedienteMininter> findByUbicacion(UbicacionExpediente ubicacion);
   List<DetExpedienteMininterDto> findAllFueraPlazoOficio(UUID idUsuario);
}

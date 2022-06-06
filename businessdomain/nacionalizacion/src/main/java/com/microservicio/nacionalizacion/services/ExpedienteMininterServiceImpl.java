package com.microservicio.nacionalizacion.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.commons.utils.models.entities.Usuario;
import com.microservicio.nacionalizacion.models.dto.DetExpedienteMininterDto;
import com.microservicio.nacionalizacion.models.entities.ExpedienteMininter;
import com.microservicio.nacionalizacion.models.entities.UbicacionExpediente;
import com.microservicio.nacionalizacion.models.repository.ExpedienteMininterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpedienteMininterServiceImpl implements ExpedienteMininterService {

   @Autowired
   private ExpedienteMininterRepository repository;

   @Override
   @Transactional
   public ExpedienteMininter save(ExpedienteMininter expedienteMininter) {
      return this.repository.save(expedienteMininter);
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<ExpedienteMininter> findById(Long idExpMininter) {
      return this.repository.findById(idExpMininter);
   }

   @Override
   @Transactional(readOnly = true)
   public List<ExpedienteMininter> findAll() {
      return this.repository.findAll();
   }

   @Override
   @Transactional
   public void deleteById(Long idExpMininter) {
      this.repository.deleteById(idExpMininter);
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<ExpedienteMininter> findByNumeroExpediente(String numeroExpediente) {
      return this.repository.findByNumeroExpediente(numeroExpediente);
   }

   @Override
   @Transactional(readOnly = true)
   public List<ExpedienteMininter> findByUbicacion(UbicacionExpediente ubicacion) {
      return this.repository.findByUbicacion(ubicacion);
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<ExpedienteMininter> findByNumeroExpedienteAndUsuarioDigita(String numeroExpediente, Usuario usuarioDigita) {
      return this.repository.findByNumeroExpedienteAndUsuarioDigita(numeroExpediente, usuarioDigita);
   }

   @Override
   @Transactional(readOnly = true)
   public List<DetExpedienteMininterDto> findAllFueraPlazoOficio(UUID idUsuario) {
      return this.repository.findAllFueraPlazoOficio(idUsuario);
   }
   
}
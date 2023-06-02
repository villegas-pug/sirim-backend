package com.microservicio.rimmantenimiento.services;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import com.commons.utils.constants.LevelLog;
import com.commons.utils.constants.Messages;
import com.commons.utils.constants.RimHttpHeaders;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.models.entities.Evento;
import com.commons.utils.models.entities.TipoLogico;
import com.commons.utils.models.entities.Usuario;
import com.google.common.primitives.Ints;
import com.microservicio.rimmantenimiento.clients.RimcommonClientRest;
import com.microservicio.rimmantenimiento.errors.EventoWarningException;
import com.microservicio.rimmantenimiento.errors.RimmantenimientoWarningException;
import com.microservicio.rimmantenimiento.models.entities.Convenio;
import com.microservicio.rimmantenimiento.models.entities.DetConvenio;
import com.microservicio.rimmantenimiento.repositories.ConvenioRepository;
import com.microservicio.rimmantenimiento.repositories.EventoRepository;
import com.microservicio.rimmantenimiento.repositories.TipoLogicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class RimmantenimientoServiceImpl implements RimmantenimientoService {

   @Autowired
   private EventoRepository eventoRepository;

   @Autowired
   private TipoLogicoRepository tipoLogicoRepository;

   @Autowired
   private ConvenioRepository convenioRepository;

   @Autowired
   private RimcommonClientRest rimcommonClient;

   
   //#region Repo method's ...

   @Override
   @Transactional
   public void saveEvento(Evento evento) {
      if(Objects.isNull(evento.getEvent_id())){// ► Nuevo ...
         this.eventoRepository.save(evento);
      } else {// ► Actualizar ...
         Evento eventoUpdated = this.eventoRepository
                                          .findById(evento.getEvent_id())
                                          .orElseThrow(() ->  new EventoWarningException(Messages.WARNING_EVENTO_NOT_EXISTS));

         eventoUpdated.setTitle(evento.getTitle());
         eventoUpdated.setStart(evento.getStart());
         eventoUpdated.setEnd(evento.getEnd());

         this.eventoRepository.save(eventoUpdated);

      }

   }

   @Override
   @Transactional(readOnly = true)
   public List<Evento> findAllEvento() {
      List<Evento> eventos = this.eventoRepository.findAll();
      if(eventos.size() == 0) throw new DataAccessEmptyWarning();
      return eventos;
   }

   @Override
   public List<Evento> findEventoByUsuario(Usuario usuario) {
      List<Evento> eventos = this.eventoRepository.findByUsuario(usuario);
      if(eventos.size() == 0) throw new DataAccessEmptyWarning();
      return eventos;
   }

   @Override
   @Transactional
   public void deleteEventoById(Long idEvento) {
      this.eventoRepository.deleteById(idEvento);
   }
   
   @Override
   @Transactional
   public void saveTipoLogico(TipoLogico tipoLogico, BindingResult result) {

      // ► Dep's ...
      int idTipo = Objects.isNull(tipoLogico.getIdTipo()) || tipoLogico.getIdTipo() == 0 ? -1 : tipoLogico.getIdTipo();

      // ► Repo dep's ...

      // Validación: ...
      if (idTipo == -1) { // ► Si Nuevo, no suministran el `id` ...
         if(result.hasErrors())
            throw new RimmantenimientoWarningException(this.convertBindingResultToMessageCsv(result));

         tipoLogico.setLongitud(this.generateTipoLogicoLongitud());
      }

      // ► Save ...
      tipoLogicoRepository.save(tipoLogico);
   }

   @Override
   @Transactional(readOnly = true)
   public List<TipoLogico> findAllTipoLogico() {
      return this.tipoLogicoRepository.findAll();
   }

   @Override
   @Transactional(readOnly = true)
   public TipoLogico findTipoLogicoByLongitud(int longitud) {
      TipoLogico tipoLogico = this.tipoLogicoRepository
                                          .findByLongitud(longitud)
                                          .orElseThrow(DataAccessEmptyWarning::new);
      return tipoLogico;
   }

   @Override
   @Transactional(readOnly = true)
   public TipoLogico findTipoLogicoById(Integer idTipo) {
      TipoLogico tipoLogico = this.tipoLogicoRepository.findById(idTipo)
                                                       .orElseThrow(DataAccessEmptyWarning::new);
      return tipoLogico;
   }

   @Override
   @Transactional
   public void deleteTipoLogicoById(Integer idTipo) {
      this.tipoLogicoRepository.deleteById(idTipo);
   }

   //#region Client-Rest method's ...

   @Override
   public List<TablaDinamicaDto> findAllTablaDinamica() {
      List<TablaDinamicaDto> tablaDinamicaDb = this.rimcommonClient.findAllTablaDinamica().getData();
      if (tablaDinamicaDb.size() == 0) throw new DataAccessEmptyWarning();
      return tablaDinamicaDb;
   }

   //#endregion

   
   //#region Repo - Convenio ...

   @Override
   @Transactional
   public void saveConvenio(Convenio convenio) {

      // » Dep's ...
      boolean isNewConvenio = convenio.getIdConvenio() == null;

      // ► ...
      if (isNewConvenio) {
         this.convenioRepository.save(convenio);
      } else {

         // ► Repo dep's ...
         Convenio convenioOld = this.convenioRepository
                                          .findById(convenio.getIdConvenio())
                                          .orElseThrow(DataAccessEmptyWarning::new);
   
         convenioOld.setNombre(convenio.getNombre());
         convenioOld.setCompleto(convenio.isCompleto());
   
         this.convenioRepository.save(convenioOld);

      }

   }

   @Override
   @Transactional(readOnly = true)
   public List<Convenio> findAllConvenio() {
      List<Convenio> convenios = this.convenioRepository.findAll();
      if (convenios.size() == 0) throw new DataAccessEmptyWarning();
      return convenios;
   }

   @Override
   @Transactional
   public void deleteConvenioById(Long idConvenio) {
      this.convenioRepository.deleteById(idConvenio);
   }

   @Override
   @Transactional
   public void saveDetConvenio(DetConvenio detConvenio) {
      
      // Dep's ...
      boolean isNewDetConvenio = detConvenio.getIdDetConvenio() == null;

      // Repo dep's ...
      Convenio convenio = this.convenioRepository
                                    .findById(detConvenio.getConvenio().getIdConvenio())
                                    .orElseThrow(DataAccessEmptyWarning::new);
      
      if (isNewDetConvenio) {

         DetConvenio detConvenioNew = DetConvenio
                                          .of()
                                          .descripcion(detConvenio.getDescripcion())
                                          .fechaDocumento(detConvenio.getFechaDocumento())
                                          .get();
   
         // ► ...
         convenio.addDetConvenio(detConvenioNew);
         
      } else {

         convenio.getDetConvenio()
                     .stream()
                     .filter(det -> det.getIdDetConvenio() == detConvenio.getIdDetConvenio())
                     .forEach(det -> {
                        det.setDescripcion(detConvenio.getDescripcion());
                        det.setFechaDocumento(detConvenio.getFechaDocumento());
                     });

      }

      // ► ...
      this.convenioRepository.save(convenio);
      
   }

   @Override
   @Transactional
   public void saveDetConvenioAnexo(Long idConvenio, Long idDetConvenio, MultipartFile anexo){
      
      // Repo dep's ...
      Convenio convenio = this.convenioRepository
                                    .findById(idConvenio)
                                    .orElseThrow(DataAccessEmptyWarning::new);

      convenio.getDetConvenio()
                  .stream()
                  .filter(det -> det.getIdDetConvenio() == idDetConvenio)
                  .forEach(det -> {
                     try {
                        det.setAnexo(anexo.getBytes());
                        det.setNombreAnexo(anexo.getOriginalFilename());
                     } catch (IOException e) {
                        log.error(e.getMessage());
                     }
                  });
      
      // ► ...
      this.convenioRepository.save(convenio);
   }

   @Override
   @Transactional(readOnly = true)
   public ResponseEntity<?> downloadDetConvenioAnexo(Long idConvenio, Long idDetConvenio) {
      
      // ► Dep's ...
      Resource anexo = null;
      HttpHeaders headers = new HttpHeaders();

      try {
         
         DetConvenio detConvenio = this.convenioRepository
                                             .findById(idConvenio)
                                             .orElseThrow(DataAccessEmptyWarning::new)
                                             .getDetConvenio()
                                             .stream()
                                             .filter(det -> det.getIdDetConvenio() == idDetConvenio)
                                             .findFirst()
                                             .get();
   
         String nombreAnexo = detConvenio.getNombreAnexo();
         anexo = new ByteArrayResource(detConvenio.getAnexo());
         
         headers.add(RimHttpHeaders.RESPONSE_STATUS, LevelLog.SUCCESS);
         headers.add(RimHttpHeaders.MESSAGE, Messages.MESSAGE_SUCCESS_DOWNLOAD);
         headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"".concat(nombreAnexo).concat("\""));

      } catch (Exception e) {

         headers.add(RimHttpHeaders.MESSAGE, e.getMessage());
         headers.add(RimHttpHeaders.RESPONSE_STATUS, LevelLog.ERROR);

      }
      
      return ResponseEntity
                  .ok()
                  .headers(headers)
                  .contentType(MediaType.APPLICATION_OCTET_STREAM)
                  .body(anexo);
   }

   @Override
   @Transactional
   public void deleteDetConvenio(DetConvenio detConvenio) {
      
      // ► Dep's ...
      Convenio convenio = this.convenioRepository
                                       .findById(detConvenio.getConvenio().getIdConvenio())
                                       .orElseThrow(DataAccessEmptyWarning::new);

      convenio.removeDetConvenio(detConvenio);

      // ► ...
      this.convenioRepository.save(convenio);
      
   }

   //#endregion

   //#region Custom method's ...

   @Override
   public int generateTipoLogicoLongitud() {

      // ► Dep's ...
      int minLen = 100,
          maxLen = 500;

      Random rnd = new Random();
      int len = (rnd.nextInt(maxLen - minLen) + 1) + minLen;

      // ► Repo dep's ...
      int[] usedLens = this.tipoLogicoRepository
                                    .findAll()
                                    .stream()
                                    .mapToInt(t -> t.getLongitud())
                                    .toArray();

      // ► ...
      while (Ints.indexOf(usedLens, len) >= 0) {
         len = (rnd.nextInt(maxLen - minLen) + 1) + minLen;
      }

      return len;
   }

   protected String convertBindingResultToMessageCsv(BindingResult result){

      StringBuilder csv = new StringBuilder();

      for (FieldError fieldErr : result.getFieldErrors()) {
         csv.append(fieldErr.getDefaultMessage());
      }

      return csv.toString();
   }

   //#endregion

}

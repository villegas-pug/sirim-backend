package com.microservicio.rimmantenimiento.controllers;

import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.entities.Evento;
import com.commons.utils.models.entities.TipoLogico;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;
import com.microservicio.rimmantenimiento.models.dto.TipoLogicoDto;
import com.microservicio.rimmantenimiento.models.entities.Convenio;
import com.microservicio.rimmantenimiento.models.entities.DetConvenio;
import com.microservicio.rimmantenimiento.services.RimmantenimientoService;
import java.util.List;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PutMapping;


@CrossOrigin(origins = { "*" })
@RestController
public class RimmantenimientoController {

   @Autowired
   private RimmantenimientoService rimmantenimientoService;

   @Autowired
   private ModelMapper modelMapper;

   @PostMapping(path = { "/saveEvento" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<?>> saveEvento(@RequestBody Evento evento) {
      rimmantenimientoService.saveEvento(evento);
      return Response
               .<List<?>>builder()
               .message(Messages.SUCCESS_SAVE_EVENTO)
               .data(List.of())
               .build();
   }

   @PostMapping(path = { "/findEventoByUsuario" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<Evento>> findEventoByUsuario(@RequestBody Usuario usuario) {
      return Response
               .<List<Evento>>builder()
               .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
               .data(rimmantenimientoService.findEventoByUsuario(usuario))
               .build();
   }

   @DeleteMapping(path = { "/deleteEventoById/{idEvento}" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<?>> deleteEventoById(@PathVariable Long idEvento) {
      this.rimmantenimientoService.deleteEventoById(idEvento);
      return Response
               .<List<?>>builder()
               .message(Messages.SUCCESS_DELETE_EVENTO)
               .data(List.of())
               .build();
   }
   
   @PostMapping(path = { "/saveTipoLogico" })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<TipoLogico>> saveTipoLogico(@Valid @RequestBody TipoLogicoDto tipoLogicoDto, BindingResult result) {

      // ► Save ...
      TipoLogico tipoLogico = modelMapper.map(tipoLogicoDto, TipoLogico.class);
      this.rimmantenimientoService.saveTipoLogico(tipoLogico, result);

      return Response
               .<List<TipoLogico>>builder()
               .message(Messages.SUCCESS_SAVE_TIPO_LOGICO)
               .data(this.rimmantenimientoService.findAllTipoLogico())
               .build();
   }
   
   @GetMapping(path = { "/findAllTipoLogico" })
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<TipoLogico>> findAllTipoLogico() {
       return Response
                  .<List<TipoLogico>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(this.rimmantenimientoService.findAllTipoLogico())
                  .build();
   }
   
   @DeleteMapping(path = "/deleteTipoLogicoById/{idTipo}")
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<TipoLogico>> deleteTipoLogicoById(@PathVariable Integer idTipo) {

      this.rimmantenimientoService.deleteTipoLogicoById(idTipo);

      List<TipoLogico> tipoLogicoDb = this.rimmantenimientoService.findAllTipoLogico();
      if (tipoLogicoDb.size() == 0) throw new DataAccessEmptyWarning();

      return Response
                  .<List<TipoLogico>>builder()
                  .message(Messages.SUCCESS_DELETE_RECORD((long)idTipo))
                  .data(tipoLogicoDb)
                  .build();
   }

   @PostMapping(path = "/saveConvenio" )
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<?>> saveConvenio(@RequestBody Convenio convenio) {
      this.rimmantenimientoService.saveConvenio(convenio);
      return Response
               .<List<?>>builder()
               .message(Messages.MESSAGE_SUCCESS_SAVE("Convenio"))
               .data(List.of())
               .build();
   }

   @PostMapping(path = "/saveDetConvenio" )
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<?>> saveDetConvenio(@RequestBody DetConvenio detConvenio) {
      this.rimmantenimientoService.saveDetConvenio(detConvenio);
      return Response
               .<List<?>>builder()
               .message(Messages.MESSAGE_SUCCESS_SAVE("Detalle convenio"))
               .data(List.of())
               .build();
   }

   @PostMapping(path = "/saveDetConvenioAnexo" )
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<?>> saveDetConvenioAnexo(
                                    @RequestParam Long idConvenio, 
                                    @RequestParam Long idDetConvenio, 
                                    @RequestPart MultipartFile anexo){
                                       
      this.rimmantenimientoService.saveDetConvenioAnexo(idConvenio, idDetConvenio, anexo);
      return Response
               .<List<?>>builder()
               .message(Messages.SUCCESS_UPLOAD_FILE)
               .data(List.of())
               .build();
   }

   @GetMapping(path = "/findAllConvenio" )
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<Convenio>> findAllConvenio() {
      return Response
               .<List<Convenio>>builder()
               .message(Messages.SUCCESS_SAVE_DATA_MODEL)
               .data(this.rimmantenimientoService.findAllConvenio())
               .build();
   }

   @DeleteMapping(path = { "/deleteConvenioById" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<?>> deleteConvenioById(@RequestParam Long idConvenio) {
      this.rimmantenimientoService.deleteConvenioById(idConvenio);
       return Response
                  .<List<?>>builder()
                  .message(Messages.SUCCESS_DELETE_RECORD(idConvenio))
                  .data(List.of())
                  .build();
   }

   @GetMapping(path = { "/downloadDetConvenioAnexo" }, produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
   public ResponseEntity<?> downloadDetConvenioAnexo(@RequestParam Long idConvenio, @RequestParam Long idDetConvenio) {
       return this.rimmantenimientoService.downloadDetConvenioAnexo(idConvenio, idDetConvenio);
   }
     

   @DeleteMapping(path = { "/deleteDetConvenio" })
   @ResponseStatus(code = HttpStatus.OK)
   public Response<List<?>> deleteDetConvenio(@RequestBody DetConvenio detConvenio) {
      this.rimmantenimientoService.deleteDetConvenio(detConvenio);
       return Response
                  .<List<?>>builder()
                  .message(Messages.SUCCESS_DELETE_RECORD(detConvenio.getIdDetConvenio()))
                  .data(List.of())
                  .build();
   }

}
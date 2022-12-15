package com.microservicio.rimmantenimiento.services;

import java.io.IOException;
import java.util.List;

import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.models.entities.Evento;
import com.commons.utils.models.entities.TipoLogico;
import com.commons.utils.models.entities.Usuario;
import com.microservicio.rimmantenimiento.models.entities.Convenio;
import com.microservicio.rimmantenimiento.models.entities.DetConvenio;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MultipartFilter;

public interface RimmantenimientoService {

   // ► Repo method's ...
   void saveTipoLogico(TipoLogico tipoLogico, BindingResult result);
   void deleteTipoLogicoById(Integer idTipo);
   List<TipoLogico> findAllTipoLogico();
   TipoLogico findTipoLogicoById(Integer idTipo);
   TipoLogico findTipoLogicoByLongitud(int longitud);

   void saveEvento(Evento evento);
   List<Evento> findAllEvento();
   List<Evento> findEventoByUsuario(Usuario usuario);
   void deleteEventoById(Long idEvento);

   // ► Repo-Convenio ...
   void saveConvenio(Convenio convenio);
   void deleteConvenioById(Long idConvenio);
   void saveDetConvenio(DetConvenio detConvenio);
   void saveDetConvenioAnexo(Long idConvenio, Long idDetConvenio, MultipartFile anexo);
   void deleteDetConvenio(DetConvenio detConvenio);
   ResponseEntity<?> downloadDetConvenioAnexo(Long idConvenio, Long idDetConvenio);

   List<Convenio> findAllConvenio();

   // ► Client-Rest method's ...
   List<TablaDinamicaDto> findAllTablaDinamica();

   // ► Custom method's ...
   int generateTipoLogicoLongitud();

}

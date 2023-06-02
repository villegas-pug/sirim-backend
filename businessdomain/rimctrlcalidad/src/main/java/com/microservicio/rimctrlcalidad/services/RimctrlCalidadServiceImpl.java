package com.microservicio.rimctrlcalidad.services;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.models.dto.AsigGrupoCamposAnalisisDto;
import com.commons.utils.models.dto.RegistroTablaDinamicaDto;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.models.entities.AsigGrupoCamposAnalisis;
import com.commons.utils.models.entities.CtrlCalCamposAnalisis;
import com.commons.utils.models.entities.ProduccionAnalisis;
import com.google.common.base.Strings;
import com.microservicio.rimctrlcalidad.clients.RimcommonClientRest;
import com.microservicio.rimctrlcalidad.errors.RimctrlcalidadWarningException;
import com.microservicio.rimctrlcalidad.repositories.AsigGrupoCamposAnalisisRepository;
import com.microservicio.rimctrlcalidad.repositories.CtrlCalCamposAnalisisRepository;
import com.microservicio.rimctrlcalidad.repositories.ProduccionAnalisisRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RimctrlCalidadServiceImpl implements RimctrlCalidadService {

   @Autowired
   private CtrlCalCamposAnalisisRepository repository;

   @Autowired
   private AsigGrupoCamposAnalisisRepository asigRepository;

   @Autowired
   private ProduccionAnalisisRepository produccionRepository;

   @Autowired
   private RimcommonClientRest rimcommonClient;

   @Autowired
   private ModelMapper modelMapper;

   //#region: Repo Method's ...

   @Override
   @Transactional
   public void saveCtrlCalCamposAnalisis(Long idAsigGrupo) {

      // ► Repo - Dep's ...
      // -----------------------------------------------------------------------------------------------------------------
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisisDb = this.asigRepository
                                                                     .findById(idAsigGrupo)
                                                                     .orElseThrow(DataAccessEmptyWarning::new);

      String nombreTabla = asigGrupoCamposAnalisisDb.getGrupo()
                                                    .getTablaDinamica()
                                                    .getNombre();

      int regIni = asigGrupoCamposAnalisisDb.getRegAnalisisIni(),
          regFin = asigGrupoCamposAnalisisDb.getRegAnalisisFin();

      // ► Validación: Si registro se encuentra en Control-Calidad ...
      boolean isInCtrlCal = asigGrupoCamposAnalisisDb
                                    .getCtrlsCalCamposAnalisis()
                                    .stream()
                                    .anyMatch(ctrlCal -> !ctrlCal.isCompleto());
      if (isInCtrlCal)
         throw new RimctrlcalidadWarningException(Messages.WARNING_IS_IN_CTRLCAL);

      String idsCtrlCalCsv = this.generateIdsCsvForCtrlCal(nombreTabla, regIni, regFin);

      Long totalRevisados = asigGrupoCamposAnalisisDb
                                    .getProduccionAnalisis()
                                    .stream()
                                    .filter(prod -> prod.isRevisado())
                                    .count();
      // -----------------------------------------------------------------------------------------------------------------
      
      // ► Payload: ...
      CtrlCalCamposAnalisis ctrlCalCamposAnalisisNew = CtrlCalCamposAnalisis
                                                               .of()
                                                               .idsCtrlCalCsv(idsCtrlCalCsv)
                                                               .totalRegistros((long) idsCtrlCalCsv.split(",").length)
                                                               .totalRevisados(totalRevisados.intValue())
                                                               .get();

      asigGrupoCamposAnalisisDb.addCtrlsCalCamposAnalisis(ctrlCalCamposAnalisisNew);

      // ► Save ...
      this.asigRepository.save(asigGrupoCamposAnalisisDb);
   }

   @Override
   @Transactional
   public void validateRecordAssigned(RegistroTablaDinamicaDto recordAssignedDto) {

      // ► Dep's ...
      Long idRecord = recordAssignedDto.getId(),
           idAsig = recordAssignedDto.getAsigGrupo().getIdAsigGrupo(),
           idCtrlCal = recordAssignedDto.getIdCtrlCal();

      // ► Repo dep's ...
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisis = this.asigRepository
                                                                     .findById(idAsig)
                                                                     .orElseThrow(DataAccessEmptyWarning::new);

      boolean isRecordValidated = asigGrupoCamposAnalisis
                                          .getProduccionAnalisis()
                                          .stream()
                                          .anyMatch(prod -> prod.getIdRegistroAnalisis().equals(idRecord)
                                                            && prod.isRevisado());

      Long totalRevisados = asigGrupoCamposAnalisis
                                    .getProduccionAnalisis()
                                    .stream()
                                    .filter(ProduccionAnalisis::isRevisado)
                                    .count();

      // ► Save ...
      /*----------------------------------------------------------------------------------------------------*/
      // ► Update: Tabla física y Asignacion ...
      this.rimcommonClient.saveRecordAssigned(recordAssignedDto);

      // ► Update: Prop´s de Producción y Control de calidad ...
      asigGrupoCamposAnalisis
               .getProduccionAnalisis()
               .stream()
               .filter(prod -> prod.getIdRegistroAnalisis().equals(idRecord))
               .forEach(prod -> {
                  prod.setRevisado(true);
                  prod.setObservacionesCtrlCal(recordAssignedDto.getObservacionesCtrlCal());
               });

      // ► Update: Total registros revisados `CtrlCalCamposAnalisis` ...
      if(!isRecordValidated)
         asigGrupoCamposAnalisis
                  .getCtrlsCalCamposAnalisis()
                  .stream()
                  .filter(ctrlCal -> ctrlCal.getIdCtrlCal().equals(idCtrlCal))
                  .forEach(ctrlCal -> {
                     ctrlCal.setTotalRevisados(totalRevisados.intValue() + 1);
                  });

      this.asigRepository.save(asigGrupoCamposAnalisis);
      
      /* -------------------------------------------------------------------------------------------------- */

   }

   @Override
   @Transactional
   public void conformToCtrlCal(AsigGrupoCamposAnalisisDto asigGrupoCamposAnalisisDto) {
      
      // ► Dep's ...
      Long idAsigGrupo = asigGrupoCamposAnalisisDto.getIdAsigGrupo(),
           idCtrlCal = asigGrupoCamposAnalisisDto.getCtrlCalCamposAnalisis().getIdCtrlCal();
      boolean isCtrlCalConforme = asigGrupoCamposAnalisisDto.isCtrlCalConforme();

      // ► Repo dep´s ...
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisisDb = this.asigRepository
                                                                     .findById(idAsigGrupo)
                                                                     .orElseThrow(DataAccessEmptyWarning::new);


      // Save ...
      asigGrupoCamposAnalisisDb.setCtrlCalConforme(isCtrlCalConforme);

      asigGrupoCamposAnalisisDb
                  .getCtrlsCalCamposAnalisis()
                  .stream()
                  .filter(ctrlCal -> ctrlCal.getIdCtrlCal().equals(idCtrlCal))
                  .forEach(ctrlCal -> {
                     ctrlCal.setCompleto(true);
                  });

      this.asigRepository.save(asigGrupoCamposAnalisisDb);

   }

   @Override
   @Transactional
   public void saveMetaFieldIdErrorCsv(RegistroTablaDinamicaDto registroTablaDinamica) {

      // ► Dep's: idAsig, idRegistroAnalisis, metaFieldName, isError ...
      Long idAsig = registroTablaDinamica.getAsigGrupo().getIdAsigGrupo(),
           idRegistroAnalisis = registroTablaDinamica.getId();

      String metaFieldName = registroTablaDinamica.getMetaFieldIdErrorCsv();
      boolean hasFieldError = registroTablaDinamica.isHasFieldError();
      
      // ► Repo dep's ...
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisis = this.asigRepository
                                                                  .findById(idAsig)
                                                                  .orElseThrow(DataAccessEmptyWarning::new)         ;
      
      // ► ...
      asigGrupoCamposAnalisis
               .getProduccionAnalisis()
               .stream()
               .filter(p -> p.getIdRegistroAnalisis().equals(idRegistroAnalisis))
               .forEach(p -> {

                  String metaFieldsErrorCsv = "";

                  if (hasFieldError) {

                     metaFieldsErrorCsv = Strings.isNullOrEmpty(p.getMetaFieldIdErrorCsv())
                                                   ? metaFieldName
                                                   : p.getMetaFieldIdErrorCsv().concat(", ")
                                                                               .concat(metaFieldName);
                     
                  } else {

                     metaFieldsErrorCsv = Arrays.stream(p.getMetaFieldIdErrorCsv().split(","))
                                                .map(String::trim)
                                                .filter(f -> !f.equals(metaFieldName.trim()))
                                                .collect(Collectors.joining(", "));

                  }

                  p.setMetaFieldIdErrorCsv(metaFieldsErrorCsv);
               });

         // ► Save ...
         this.asigRepository.save(asigGrupoCamposAnalisis);

   }

   @Override
   @Transactional(readOnly = true)
   public AsigGrupoCamposAnalisisDto findAsigGrupoCamposAnalisisById(Long idAsigGrupo) {
      
      AsigGrupoCamposAnalisisDto asigGrupoCamposAnalisisDto = modelMapper.map(
                                                                           this.asigRepository
                                                                                    .findById(idAsigGrupo)
                                                                                    .orElseThrow(DataAccessEmptyWarning::new),
                                                                           AsigGrupoCamposAnalisisDto.class);

      return asigGrupoCamposAnalisisDto;

   }

   @Override
   @Transactional
   public void saveRectificadoRecordAssigned(Long idProdAnalisis) {
      
      // Repo dep's ...
      ProduccionAnalisis produccionAnalisis = this.produccionRepository.findById(idProdAnalisis)
                                                                       .orElseThrow(DataAccessEmptyWarning::new);

      // Save ...
      produccionAnalisis.setRectificado(!produccionAnalisis.isRectificado());
      this.produccionRepository.save(produccionAnalisis);

   }

   //#endregion

   //#region Client-Rest method's ...

   @Override
   public List<TablaDinamicaDto> findAllTablaDinamica() {
      List<TablaDinamicaDto> tablaDinamicaDb = this.rimcommonClient.findAllTablaDinamica().getData();
      if (tablaDinamicaDb.size() == 0) throw new DataAccessEmptyWarning();
      return tablaDinamicaDb;
   }

   @Override
   public List<Map<String, Object>> findTablaDinamicaByIdCtrlCalAndIds(Long idCtrlCal, String idsCsv) {

      // ► Dep's ...
      StringBuilder queryString = new StringBuilder();

      // ► Repo dep's ...
      String nombreTabla = this.repository
                                    .findById(idCtrlCal)
                                    .orElseThrow(DataAccessEmptyWarning::new)
                                    .getAsigGrupoCamposAnalisis()
                                    .getGrupo()
                                    .getTablaDinamica()
                                    .getNombre();

      // ► Initialization dep's ...
      queryString.append("SELECT * FROM ")
                 .append(nombreTabla)
                 .append(" WHERE nid IN(")
                 .append(idsCsv).append(")")
                 .append(" ORDER BY nId");
      
      // ► Data-Set ...
      List<Map<String, Object>> ds = this.rimcommonClient.findDynamicSelectStatement(queryString.toString()).getData();
      if(ds.size() == 0) throw new DataAccessEmptyWarning();

      return ds;
   }

   @Override
   @Transactional(readOnly = true)
   public String generateIdsCsvForCtrlCal(String nombreTabla, int regIni, int regFin) {
      
      // ►  Dep's ...
      StringBuilder queryString = new StringBuilder();
      TablaDinamicaDto tablaDinamica = this.rimcommonClient.findTablaDinamicaByNombre(nombreTabla).getData();
      int porcentajeQC = tablaDinamica.getPorcentajeQC();

      // ► ...
      queryString.append("SELECT TOP ").append(porcentajeQC).append(" PERCENT nId FROM ")
                 .append(nombreTabla)
                 .append(" WHERE nId BETWEEN ")
                 .append(regIni). append(" AND ").append(regFin)
                 .append(" ORDER BY NEWID() ");

      // ► Data-Set ...
      List<Map<String, Object>> ds = this.rimcommonClient.findDynamicSelectStatement(queryString.toString()).getData();
      if (ds.size() == 0) throw new DataAccessEmptyWarning();

      return ds.stream()
               .map(r -> r.get("nId").toString())
               .collect(Collectors.joining(","));
   }

   //#endregion

   //#region: CUSTOM - METHOD'S
   //#endregion
   
}
package com.microservicio.rimextraccion.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.microservicio.rimextraccion.errors.RimcommonWarningException;
import com.microservicio.rimextraccion.models.dto.AsigGrupoCamposAnalisisDto;
import com.microservicio.rimextraccion.models.dto.RecordAssignedDto;
import com.microservicio.rimextraccion.models.entities.AsigGrupoCamposAnalisis;
import com.microservicio.rimextraccion.models.entities.CtrlCalCamposAnalisis;
import com.microservicio.rimextraccion.models.entities.ProduccionAnalisis;
import com.microservicio.rimextraccion.repository.RimasigGrupoCamposAnalisisRepository;
import com.microservicio.rimextraccion.repository.RimctrlCalCamposAnalisisRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RimctrlCalCamposAnalisisServiceImpl implements RimctrlCalCamposAnalisisService {

   @Autowired
   private RimasigGrupoCamposAnalisisRepository rimasigAnalisisRepository;

   @Autowired 
   private RimcommonService rimcommonService;

   @Autowired
   private RimanalisisService rimanalisisService;


   //#region: REPO - METHOD'S ...

   @Override
   @Transactional
   public void saveCtrlCalCamposAnalisis(Long idAsigGrupo) {

      // ► Repo - Dep's ...
      // -----------------------------------------------------------------------------------------------------------------
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisisDb = this.rimasigAnalisisRepository
                                                                  .findById(idAsigGrupo)
                                                                  .orElseThrow(DataAccessEmptyWarning::new);

      String nombreTabla = asigGrupoCamposAnalisisDb.getGrupo().getTablaDinamica().getNombre();
      int regIni = asigGrupoCamposAnalisisDb.getRegAnalisisIni(),
          regFin = asigGrupoCamposAnalisisDb.getRegAnalisisFin();

      boolean isInCtrlCal = asigGrupoCamposAnalisisDb
                                    .getCtrlsCalCamposAnalisis()
                                    .stream()
                                    .anyMatch(ctrlCal -> !ctrlCal.isCompleto());
      // ► Validación: Si ...
      if (isInCtrlCal)
         throw new RimcommonWarningException(Messages.WARNING_IS_IN_CTRLCAL);

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
      this.rimasigAnalisisRepository.save(asigGrupoCamposAnalisisDb);
   }

   @Override
   @Transactional
   public void validateRecordAssigned(RecordAssignedDto recordAssignedDto) {

      // ► Dep's ...
      Long idRecord = recordAssignedDto.getId(),
           idAsig = recordAssignedDto.getAsigGrupo().getIdAsigGrupo(),
           idCtrlCal = recordAssignedDto.getIdCtrlCal();

      // ► Repo dep's ...
      // ► ...
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisis = this.rimasigAnalisisRepository
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
      this.rimanalisisService.saveRecordAssigned(recordAssignedDto);

      // ► Update: Propiedades de Control de calidad ...
      asigGrupoCamposAnalisis
               .getProduccionAnalisis()
               .stream()
               .filter(prod -> prod.getIdRegistroAnalisis().equals(idRecord))
               .forEach(prod -> {
                  prod.setRevisado(true);
                  prod.setObservacionesCtrlCal(recordAssignedDto.getObservacionesCtrlCal());
                  prod.setMetaFieldIdErrorCsv(recordAssignedDto.getMetaFieldIdErrorCsv());
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

      this.rimasigAnalisisRepository.save(asigGrupoCamposAnalisis);
      
      /* -------------------------------------------------------------------------------------------------- */

   }

   //#endregion

   //#endregion: CUSTOM - METHOD'S

   @Override
   @Transactional(readOnly = true)
   public String generateIdsCsvForCtrlCal(String nombreTabla, int regIni, int regFin) {
      
      // ►  Dep's ...
      StringBuilder queryString = new StringBuilder();

      // ► ...
      queryString.append("SELECT TOP 10 PERCENT nId FROM ")
                 .append(nombreTabla)
                 .append(" WHERE nId BETWEEN ")
                 .append(regIni). append(" AND ").append(regFin)
                 .append(" ORDER BY NEWID() ");

      // ► Data-Set ...
      List<Map<String, Object>> ds = this.rimcommonService.findDynamicSelectStatement(queryString.toString());
      if(ds.size() == 0) throw new DataAccessEmptyWarning();

      return ds.stream()
               .map(r -> r.get("nId").toString())
               .collect(Collectors.joining(","));
   }

   @Override
   @Transactional(readOnly = true)
   public List<Map<String, Object>> findTablaDinamicaByNameAndIds(String nombreTabla, String idsCsv) {

      // ► Dep's ...
      StringBuilder queryString = new StringBuilder();


      // ► Initialization dep's ...
      queryString.append("SELECT * FROM ")
                 .append(nombreTabla)
                 .append(" WHERE nid IN(")
                 .append(idsCsv).append(")")
                 .append(" ORDER BY nId");
      
      // ► Data-Set ...
      List<Map<String, Object>> ds = this.rimcommonService.findDynamicSelectStatement(queryString.toString());
      if(ds.size() == 0) throw new DataAccessEmptyWarning();

      return ds;
   }

   @Override
   @Transactional
   public void conformToCtrlCal(AsigGrupoCamposAnalisisDto asigGrupoCamposAnalisisDto) {
      
      // ► Dep's ...
      Long idAsigGrupoDto = asigGrupoCamposAnalisisDto.getIdAsigGrupo(),
           idCtrlCalDto = asigGrupoCamposAnalisisDto.getCtrlCalCamposAnalisis().getIdCtrlCal();
      boolean isCtrlCalConformeDto = asigGrupoCamposAnalisisDto.isCtrlCalConforme();

      // ► Repo dep´s ...
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisisDb = this.rimasigAnalisisRepository
                                                                           .findById(idAsigGrupoDto)
                                                                           .orElseThrow(DataAccessEmptyWarning::new);


      // Save ...
      asigGrupoCamposAnalisisDb.setCtrlCalConforme(isCtrlCalConformeDto);

      asigGrupoCamposAnalisisDb
                  .getCtrlsCalCamposAnalisis()
                  .stream()
                  .filter(ctrlCal -> ctrlCal.getIdCtrlCal().equals(idCtrlCalDto))
                  .forEach(ctrlCal -> {
                     ctrlCal.setCompleto(true);
                  });

      this.rimasigAnalisisRepository.save(asigGrupoCamposAnalisisDb);

   }

   //#endregion
   
}
package com.microservicio.rimcommon.services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Tuple;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.helpers.DataModelHelper;
import com.commons.utils.helpers.RimcommonHelper;
import com.commons.utils.models.dto.AsigGrupoCamposAnalisisDto;
import com.commons.utils.models.dto.GrupoCamposAnalisisDto;
import com.commons.utils.models.dto.RegistroTablaDinamicaDto;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.models.entities.AsigGrupoCamposAnalisis;
import com.commons.utils.models.entities.GrupoCamposAnalisis;
import com.commons.utils.models.entities.ProduccionAnalisis;
import com.commons.utils.models.entities.TablaDinamica;
import com.commons.utils.models.entities.Usuario;
import com.microservicio.rimcommon.repositories.AsigGrupoCamposAnalisisRepository;
import com.microservicio.rimcommon.repositories.GrupoCamposAnalisisRepository;
import com.microservicio.rimcommon.repositories.ProduccionAnalisisRepository;
import com.microservicio.rimcommon.repositories.RimcommonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RimcommonServiceImpl implements RimcommonService {

   @Autowired
   private RimcommonRepository repository;

   @Autowired
   private GrupoCamposAnalisisRepository grupoRepository;

   @Autowired
   private AsigGrupoCamposAnalisisRepository asigRepository;

   @Autowired
   private ProduccionAnalisisRepository produccionRepository;

   @Autowired
   private ModelMapper modelMapper;

   @Override
   @Transactional(readOnly = true)
   public List<TablaDinamicaDto> findAllTablaDinamica() {
      List<TablaDinamicaDto> tablaDinamicaDto = this.repository
                                                         .findAll()
                                                         .stream()
                                                         .map(t -> modelMapper.map(t, TablaDinamicaDto.class))
                                                         .collect(Collectors.toList());

      /*► Validación ...  */
      if(tablaDinamicaDto.size() == 0)
         throw new DataAccessEmptyWarning();

      tablaDinamicaDto
            .forEach(t -> {
               t.getLstGrupoCamposAnalisis()
                  .forEach(g -> {
                     List<AsigGrupoCamposAnalisisDto> assigsNew = g.getAsigGrupoCamposAnalisis()
                           .stream()
                           .map(RimcommonHelper::setTotalsPropsToAsigGrupoCamposAnalisis)
                           .collect(Collectors.toList());

                     g.setAsigGrupoCamposAnalisis(assigsNew);
                  });
            });
      return tablaDinamicaDto;
   }

   @Override
   @Transactional(readOnly = true)
   public List<Map<String, Object>> findTablaDinamicaByRangoFromIds(long idAsigGrupo, Long rIni, Long rFin) {
      List<Tuple> tablaDinamicaDb = this.repository.findTablaDinamicaByRangoFromIds(idAsigGrupo, rIni, rFin);
      return  DataModelHelper.convertTuplesToJson(tablaDinamicaDb, false);
   }

   @Override
   @Transactional
   public void saveRecordAssigned(RegistroTablaDinamicaDto recordAssignedDto) {
      
      // ► Dep's ...
      Long idRegistro = recordAssignedDto.getId();
      Long idAsigGrupo = recordAssignedDto.getAsigGrupo().getIdAsigGrupo();

      AsigGrupoCamposAnalisis asigGrupoCamposAnalisis = AsigGrupoCamposAnalisis
                                                            .of()
                                                            .idAsigGrupo(idAsigGrupo)
                                                            .get();

      String cleanJsonValues = recordAssignedDto.getValues().replaceAll("[{}]", ""),
             csvFields = Arrays.stream(cleanJsonValues.split(","))
                               .map(i -> { 
                                    String[] field = i.split(":");
                                    return 
                                       field[0].replaceAll("[\"\']", "")
                                                .concat("=")
                                                .concat(field[1].replaceAll("\"", "'"));
                               })
                               .collect(Collectors.joining(", "));

      // ► Save: Registro en tabla física ...
      StringBuilder queryString = new StringBuilder("UPDATE ");
      queryString
         .append(recordAssignedDto.getNombreTabla()).append(" SET ").append(csvFields)
         .append(" WHERE nId = ").append(idRegistro);

      this.repository.alterTablaDinamica(queryString.toString());

      // ► Save: Registro en tabla lógica ...
      /*-------------------------------------------------------------------------------------------------------*/
      
      ProduccionAnalisis produccionAnalisis = this.produccionRepository
                                                         .findByAsigGrupoAndIdRegistroAnalisis(asigGrupoCamposAnalisis, idRegistro)
                                                         .orElse(null);

      
      
      if (produccionAnalisis == null) {// ► Nuevo registro de producción ...
         ProduccionAnalisis produccionAnalisisNew = ProduccionAnalisis
                                                            .of()
                                                            .asigGrupo(asigGrupoCamposAnalisis)
                                                            .idRegistroAnalisis(idRegistro)
                                                            .get();
         
         // ► Save ...
         this.produccionRepository.save(produccionAnalisisNew);
      }
                                       
      /*-------------------------------------------------------------------------------------------------------*/

   }

   @Override
   @Transactional(readOnly = true)
   public List<Map<String, Object>> findDynamicSelectStatement(String queryString) {
      List<Map<String, Object>> ds = DataModelHelper.convertTuplesToJson(this.repository.findDynamicSelectStatement(queryString), false);
      if(ds.size() == 0) throw new DataAccessEmptyWarning();
      return ds;
   }

   @Override
   @Transactional(readOnly = true)
   public List<Map<String, Object>> findTablaDinamicaBySuffixOfField(String nombreTabla, String suffix) {
      List<Tuple> tablaDinamicaDb = this.repository.findTablaDinamicaBySuffixOfField(nombreTabla, suffix);
      return DataModelHelper.convertTuplesToJson(tablaDinamicaDb, false);
   }

   @Override
   @Transactional(readOnly = true)
   public Long countTablaByNombre(String nombreTabla) {
      return this.repository.countTablaByNombre(nombreTabla);
   }

   @Override
   @Transactional(readOnly = true)
   public GrupoCamposAnalisisDto findGrupoCamposAnalisisById(Long idGrupo) {
      GrupoCamposAnalisis grupoCamposAnalisis = this.grupoRepository
                                                            .findById(idGrupo)
                                                            .orElseThrow(DataAccessEmptyWarning::new);
      GrupoCamposAnalisisDto grupoCamposAnalisisDto = modelMapper.map(grupoCamposAnalisis, GrupoCamposAnalisisDto.class);
      return grupoCamposAnalisisDto;
   }

   @Override
   @Transactional
   public void deleteGrupoCamposAnalisisbyId(Long idGrupo) {
      this.grupoRepository.deleteById(idGrupo);
   }

   @Override
   public List<TablaDinamicaDto> findTablaDinamicaByUsrCreador(Usuario usrCreador) {
      List<TablaDinamicaDto> tablaDinamicaDto = this.repository
                                                         .findByUsrCreador(usrCreador)
                                                         .stream()
                                                         .map(t -> modelMapper.map(t, TablaDinamicaDto.class))
                                                         .collect(Collectors.toList());

      // ► Validación ...
      if(tablaDinamicaDto.size() == 0)
         throw new DataAccessEmptyWarning();

      tablaDinamicaDto
            .forEach(t -> {
               t.getLstGrupoCamposAnalisis()
                  .forEach(g -> {
                     List<AsigGrupoCamposAnalisisDto> assigsNew = g.getAsigGrupoCamposAnalisis()
                                                                              .stream()
                                                                              .map(RimcommonHelper::setTotalsPropsToAsigGrupoCamposAnalisis)
                                                                              .collect(Collectors.toList());

                     g.setAsigGrupoCamposAnalisis(assigsNew);
                  });
            });
      return tablaDinamicaDto;
   }

   @Override
   @Transactional(readOnly = true)
   public TablaDinamicaDto findTablaDinamicaByNombre(String nombre) {
      TablaDinamicaDto tablaDinamicaDb = modelMapper.map(
                                                      this.repository
                                                            .findByNombre(nombre)
                                                            .orElseThrow(DataAccessEmptyWarning::new),
                                                      TablaDinamicaDto.class);

      return tablaDinamicaDb;
   }

   @Override
   @Transactional(readOnly = true)
   public List<TablaDinamicaDto> findAllTablaDinamicaOnlyNombres() {
      List<TablaDinamicaDto> tablaDinamicaOnlyNombres = this.repository
                                                                  .findAll()
                                                                  .stream()
                                                                  .map(t -> {

                                                                     return TablaDinamica
                                                                                 .of()
                                                                                 .idTabla(t.getIdTabla())
                                                                                 .nombre(t.getNombre())
                                                                                 .usrCreador(t.getUsrCreador())
                                                                                 .fechaCreacion(t.getFechaCreacion())
                                                                                 .get();

                                                                  }).map(t -> modelMapper.map(t, TablaDinamicaDto.class))
                                                                  .collect(Collectors.toList());
      
      return tablaDinamicaOnlyNombres;
   }

   @Override
   @Transactional(readOnly = true)
   public List<AsigGrupoCamposAnalisisDto> findAsigByUsrAnalista(Usuario usuario, boolean unfinished) {
   
      List<AsigGrupoCamposAnalisis> asigGrupoCamposAnalisisDb = this.asigRepository.findByUsrAnalista(usuario);
      if (asigGrupoCamposAnalisisDb.size() == 0) throw new DataAccessEmptyWarning();

      List<AsigGrupoCamposAnalisisDto> asigGrupoCamposAnalisisDto;

      if (unfinished) { // ► Asignaciones pendientes ...
         asigGrupoCamposAnalisisDb = asigGrupoCamposAnalisisDb
                                                .stream()
                                                .filter(RimcommonServiceImpl::isAsigUnfinished)// Asignaciones `PENDIENTES` ...
                                                .filter(asig -> asig.getFechaAsignacion().compareTo(new Date()) <= 0)// Asignaciones anteriores a la fecha de asignación ...
                                                .collect(Collectors.toList());

            if (asigGrupoCamposAnalisisDb.size() == 0) throw new DataAccessEmptyWarning();
      }

      asigGrupoCamposAnalisisDto = asigGrupoCamposAnalisisDb
                                          .stream()
                                          .map(assig -> modelMapper.map(assig, AsigGrupoCamposAnalisisDto.class))
                                          .map(RimcommonHelper::setTotalsPropsToAsigGrupoCamposAnalisis)
                                          .collect(Collectors.toList());

      return asigGrupoCamposAnalisisDto;
   }

   @Override
   @Transactional(readOnly = true)
   public AsigGrupoCamposAnalisisDto findAsigById(Long id) {
      
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisis = this.asigRepository
                                                                     .findById(id)
                                                                     .orElseThrow(DataAccessEmptyWarning::new);

      AsigGrupoCamposAnalisisDto asigGrupoCamposAnalisisDto = modelMapper.map(asigGrupoCamposAnalisis, AsigGrupoCamposAnalisisDto.class);
      RimcommonHelper.setTotalsPropsToAsigGrupoCamposAnalisis(asigGrupoCamposAnalisisDto);

      return asigGrupoCamposAnalisisDto;
   }

   //#region Private method's ...

   private static boolean isAsigUnfinished(AsigGrupoCamposAnalisis asig){
      Long totalAsignados = (long) ((asig.getRegAnalisisFin()  - asig.getRegAnalisisIni()) + 1);
      Long totalAnalizados = (long) asig.getProduccionAnalisis().size();
      return (totalAsignados - totalAnalizados) > 0;
   }

   //#endregion

}

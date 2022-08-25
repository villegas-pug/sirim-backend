package com.microservicio.rimextraccion.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Tuple;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.helpers.DataModelHelper;
import com.microservicio.rimextraccion.models.dto.AsigGrupoCamposAnalisisDto;
import com.microservicio.rimextraccion.models.dto.TablaDinamicaDto;
import com.microservicio.rimextraccion.repository.RimasigGrupoCamposAnalisisRepository;
import com.microservicio.rimextraccion.repository.RimcommonRepository;
import com.microservicio.rimextraccion.repository.RimextraccionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RimcommonServiceImpl implements RimcommonService {

   @Autowired
   private RimcommonRepository repository;

   @Autowired
   private RimextraccionRepository rimextraccionRepository;
   
   @Autowired
   private RimasigGrupoCamposAnalisisRepository asigGrupoAnalisisRepository;

   @Autowired
   private ModelMapper modelMapper;

   @Override
   @Transactional(readOnly = true)
   public List<TablaDinamicaDto> findAllTablaDinamica() {
      List<TablaDinamicaDto> tablaDinamicaDto = this.rimextraccionRepository
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
                           .map(this::setTotalsPropsToAsigGrupoCamposAnalisis)
                           .collect(Collectors.toList());

                     g.setAsigGrupoCamposAnalisis(assigsNew);
                  });
            });
      return tablaDinamicaDto;
   }

   @Override
   @Transactional(readOnly = true)
   public List<Map<String, Object>> findTablaDinamicaByRangoFromIds(String nombreTabla, Long rIni, Long rFin) {
      List<Tuple> tablaDinamicaDb = this.asigGrupoAnalisisRepository.findTablaDinamicaByRangoFromIds(nombreTabla, rIni, rFin);
      return  DataModelHelper.convertTuplesToJson(tablaDinamicaDb, false);
   }

   @Override
   public List<Map<String, Object>> findDynamicSelectStatement(String queryString) {
      List<Map<String, Object>> ds = DataModelHelper.convertTuplesToJson(this.repository.findDynamicSelectStatement(queryString), false);
      return ds;
   }

   //#region: Private method's ...

   public AsigGrupoCamposAnalisisDto setTotalsPropsToAsigGrupoCamposAnalisis (AsigGrupoCamposAnalisisDto assig){
      /*► Payload ...  */
      /* String nombreTabla = assig.getGrupo().getTablaDinamica().getNombre(); */
      Long rIni = (long) assig.getRegAnalisisIni();
      Long rFin = (long) assig.getRegAnalisisFin();

      /*► Data-Set ... */
      /* List<Map<String, Object>> recordsAnalisados = this.findTablaDinamicaByRangoFromIds(nombreTabla, rIni, rFin); */

      /*► Final ...  */
      Long totalAsignados = (long) ((rFin - rIni) + 1);
      Long totalAnalizados = (long) assig.getProduccionAnalisis().size();
      Long totalPendientes = totalAsignados - totalAnalizados;

      assig.setTotalAsignados(totalAsignados);
      assig.setTotalAnalizados(totalAnalizados);
      assig.setTotalPendientes(totalPendientes);
      return assig;
   }

   //#endregion

}

package com.microservicio.rimasignacion.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.helpers.RimcommonHelper;
import com.commons.utils.models.dto.AsigGrupoCamposAnalisisDto;
import com.commons.utils.models.dto.GrupoCamposAnalisisDto;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.models.entities.AsigGrupoCamposAnalisis;
import com.commons.utils.models.entities.Usuario;
import com.microservicio.rimasignacion.clients.RimcommonClientRest;
import com.microservicio.rimasignacion.repository.AsigGrupoCamposAnalisisRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RimasignacionServiceImpl implements RimasignacionService{

   @Autowired
   private AsigGrupoCamposAnalisisRepository repository;

   @Autowired
   private RimcommonClientRest rimcommonClient;

   @Autowired
   private ModelMapper modelMapper;

   @Override
   @Transactional
   public void save(AsigGrupoCamposAnalisis asigGrupoCamposAnalisis) {
      this.repository.save(asigGrupoCamposAnalisis);
   }

   @Override
   @Transactional
   public void deleteById(Long idAsign) {
      this.repository.deleteById(idAsign);
   }
   
   @Override
   @Transactional(readOnly = true)
   public AsigGrupoCamposAnalisis findById(Long id) {
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisis  = this.repository
                                                                  .findById(id)
                                                                  .orElseThrow(DataAccessEmptyWarning::new);
      return asigGrupoCamposAnalisis;
   }

   
   @Override
   @Transactional(readOnly = true)
   public AsigGrupoCamposAnalisis findAsigGrupoCamposAnalisisById(Long idAsig) {
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisis = this.repository.findById(idAsig)
                                                                       .orElseThrow(DataAccessEmptyWarning::new);
      return asigGrupoCamposAnalisis;
   }

   //#region Rest-Client method's ...

   @Override
   public List<TablaDinamicaDto> findAllTablaDinamica() {
      List<TablaDinamicaDto> tablaDinamicaDb = this.rimcommonClient.findAllTablaDinamica().getData();
      if (tablaDinamicaDb.size() == 0) throw new DataAccessEmptyWarning();
      return tablaDinamicaDb;
   }

   @Override
   public GrupoCamposAnalisisDto findGrupoCamposAnalisisById(Long idGrupo) {
      GrupoCamposAnalisisDto grupoCamposAnalisis = Optional.ofNullable(this.rimcommonClient.findGrupoCamposAnalisisById(idGrupo).getData())
                                                           .orElseThrow(DataAccessEmptyWarning::new);
      return grupoCamposAnalisis;
   }

   @Override
   public Long countTablaByNombre(String nombreTabla) {
      return this.rimcommonClient.countTablaByNombre(nombreTabla).getData();
   }

   //#endregion

}
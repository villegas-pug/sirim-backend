package com.microservicio.rimasignacion.services;

import java.util.List;
import com.commons.utils.models.dto.GrupoCamposAnalisisDto;
import com.commons.utils.models.dto.TablaDinamicaDto;
import com.commons.utils.models.entities.AsigGrupoCamposAnalisis;

public interface RimasignacionService{

   // ► Repo method's ...
   AsigGrupoCamposAnalisis findById(Long id);
   void save(AsigGrupoCamposAnalisis asigGrupoCamposAnalisis);
   void deleteById(Long idAsign);
   AsigGrupoCamposAnalisis findAsigGrupoCamposAnalisisById(Long idAsig);

   // ► Rest-Client method's ...
   List<TablaDinamicaDto> findAllTablaDinamica();
   GrupoCamposAnalisisDto findGrupoCamposAnalisisById(Long idGrupo);
   Long countTablaByNombre(String nombreTabla);
   

   // ► Custom method's ...

}
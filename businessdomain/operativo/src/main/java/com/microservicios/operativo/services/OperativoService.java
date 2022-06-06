package com.microservicios.operativo.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;

import com.commons.utils.models.dto.IntervenidoOpeDto;
import com.commons.utils.models.dto.ModalidadOpeDto;
import com.commons.utils.models.dto.NacionalidadOpeDto;
import com.commons.utils.models.dto.OperativoDto;
import com.commons.utils.models.dto.RptOperativoDto;
import com.commons.utils.models.dto.SexoOperativoDto;
import com.commons.utils.models.dto.TipoInfraccionOpeDto;
import com.commons.utils.models.dto.TipoOperativoDto;
import com.commons.utils.models.entities.Pais;
import com.commons.utils.services.CommonService;
import com.microservicios.operativo.models.entities.Operativo;

public interface OperativoService extends CommonService<Operativo> {

   Optional<Pais> findByNacionalidad(String nacionalidad);

   List<OperativoDto> countPivotedByOpeAnual();

   List<IntervenidoOpeDto> countPivotedByIntervenidos(String añoOpe, String sexo, String dependencia);

   List<TipoInfraccionOpeDto> countPivotedByTipoInfraccion(String añoOpe, String sexo, String dependencia);

   List<TipoOperativoDto> countPivotedByTipoOperativo(String añoOpe, String sexo, String dependencia);

   List<SexoOperativoDto> countPivotedBySexo(String añoOpe, String dependencia);
   
   List<NacionalidadOpeDto> countPivotedOpeByNacionalidad(String añoOpe, String sexo, String dependencia);

   List<ModalidadOpeDto> countPivotedOpeByModalidad(String añoOpe, String dependencia);

   List<?> countPivotedOpe();

   List<RptOperativoDto> findOpeByCustomFilterToExcel(Long idOpe, String fecIni, String fecFin, String dependencia, String modalidad, String sexo, String tipoOperativo);

   List<Operativo> findQueryOpeAll();

   List<Tuple> createTable();

}
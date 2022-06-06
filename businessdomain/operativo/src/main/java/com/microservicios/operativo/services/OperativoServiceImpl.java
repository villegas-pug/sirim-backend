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
import com.commons.utils.services.CommonServiceImpl;
import com.microservicios.operativo.clients.PaisClient;
import com.microservicios.operativo.models.entities.Operativo;
import com.microservicios.operativo.models.repository.OperativoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OperativoServiceImpl extends CommonServiceImpl<Operativo, OperativoRepository> implements OperativoService {
   
   @Autowired
   private PaisClient paisClient;
   
   @Override
   @Transactional(readOnly = true)
   public List<OperativoDto> countPivotedByOpeAnual() {
      return super.repository.countPivotedByOpeAnual();
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<Pais> findByNacionalidad(String nacionalidad) {
      return paisClient.findByNacionalidad(nacionalidad);
   }
   
   @Override
   @Transactional(readOnly = true)
   public List<?> countPivotedOpe() {
      return super.repository.countPivotedOpe();
   }

   @Override
   @Transactional(readOnly = true)
   public List<Operativo> findQueryOpeAll() {
      return super.repository.findQueryOpeAll();
   }
   
   @Override
   @Transactional(readOnly = true)
   public List<NacionalidadOpeDto> countPivotedOpeByNacionalidad(String añoOpe, String sexo, String dependencia) {
      return super.repository.countPivotedOpeByNacionalidad(añoOpe, sexo, dependencia);
   }
   
   @Override
   @Transactional(readOnly = true)
   public List<TipoInfraccionOpeDto> countPivotedByTipoInfraccion(String añoOpe, String sexo, String dependencia) {
      return super.repository.countPivotedByTipoInfraccion(añoOpe, sexo, dependencia);
   }
   
   @Override
   @Transactional(readOnly = true)
   public List<IntervenidoOpeDto> countPivotedByIntervenidos(String añoOpe, String sexo, String dependencia) {
      return super.repository.countPivotedByIntervenidos(añoOpe, sexo, dependencia);
   }

   @Override
   @Transactional(readOnly = true)
   public List<TipoOperativoDto> countPivotedByTipoOperativo(String añoOpe, String sexo, String dependencia) {
      return super.repository.countPivotedByTipoOperativo(añoOpe, sexo, dependencia);
   }

   @Override
   @Transactional(readOnly = true)
   public List<SexoOperativoDto> countPivotedBySexo(String añoOpe, String dependencia) {
      return super.repository.countPivotedBySexo(añoOpe, dependencia);
   }

   @Override
   @Transactional(readOnly = true)
   public List<ModalidadOpeDto> countPivotedOpeByModalidad(String añoOpe, String dependencia) {
      return super.repository.countPivotedOpeByModalidad(añoOpe, dependencia);
   }

   @Override
   @Transactional(readOnly = true)
   public List<RptOperativoDto> findOpeByCustomFilterToExcel(Long idOpe, String fecIni, String fecFin, String dependencia, String modalidad,
         String sexo, String tipoOperativo) {
      return super.repository.findOpeByCustomFilterToExcel(idOpe, fecIni, fecFin, dependencia, modalidad, sexo, tipoOperativo);
   }

   @Override
   public List<Tuple> createTable() {
      return this.repository.createTable();
   }
}
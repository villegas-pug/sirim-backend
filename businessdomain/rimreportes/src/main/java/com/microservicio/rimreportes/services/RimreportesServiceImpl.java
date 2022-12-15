package com.microservicio.rimreportes.services;

import java.util.Date;
import java.util.List;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.microservicio.rimreportes.model.dto.RptAñosControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptDependenciaControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptEdadesControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptNacionalidadControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptProduccionDiariaDto;
import com.microservicio.rimreportes.repository.RimreportesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RimreportesServiceImpl implements RimreportesService {

   @Autowired
   private RimreportesRepository repository;

   @Override
   @Transactional(readOnly = true)
   public List<RptControlMigratorioDto> getRptControlMigratorio(int año, String nacionalidad) {
      List<RptControlMigratorioDto> rptControlMigratorio = this.repository.getRptControlMigratorio(año, nacionalidad);
      if (rptControlMigratorio.size() == 0) throw new DataAccessEmptyWarning();
      return rptControlMigratorio;
   }

   @Override
   @Transactional(readOnly = true)
   public List<RptAñosControlMigratorioDto> getRptAñosControlMigratorio() {
      List<RptAñosControlMigratorioDto> rptAñosControlMigratorio = this.repository.getRptAñosControlMigratorio();
      if (rptAñosControlMigratorio.size() == 0) throw new DataAccessEmptyWarning();
      return rptAñosControlMigratorio;
   }

   @Override
   @Transactional(readOnly = true)
   public List<RptDependenciaControlMigratorioDto> getRptDependenciaControlMigratorio(int año, String nacionalidad) {
      List<RptDependenciaControlMigratorioDto> rptDependenciaControlMigratorio = this.repository.getRptDependenciaControlMigratorio(año, nacionalidad);
      if (rptDependenciaControlMigratorio.size() == 0) throw new DataAccessEmptyWarning();
      return rptDependenciaControlMigratorio;
   }

   @Override
   @Transactional(readOnly = true)
   public List<RptEdadesControlMigratorioDto> getRptEdadesControlMigratorio(int año, String nacionalidad) {
      List<RptEdadesControlMigratorioDto> rptEdadesControlMigratorio = this.repository.getRptEdadesControlMigratorio(año, nacionalidad);
      if (rptEdadesControlMigratorio.size() == 0) throw new DataAccessEmptyWarning();
      return rptEdadesControlMigratorio;
   }

   @Override
   @Transactional(readOnly = true)
   public List<RptNacionalidadControlMigratorioDto> getRptNacionalidadControlMigratorio(int año) {
      List<RptNacionalidadControlMigratorioDto> rptNacionalidadControlMigratorio = this.repository.getRptNacionalidadControlMigratorio(año);
      if (rptNacionalidadControlMigratorio.size() == 0) throw new DataAccessEmptyWarning();
      return rptNacionalidadControlMigratorio;
   }

   @Override
   @Transactional(readOnly = true)
   public List<RptProduccionDiariaDto> getRptProduccionDiaria(String fecIni, String fecFin) {
      List<RptProduccionDiariaDto> rptProduccionDiaria = this.repository.getRptProduccionDiaria(fecIni, fecFin);
      if (rptProduccionDiaria.size() == 0) throw new DataAccessEmptyWarning();
      return rptProduccionDiaria;
   }
   
}

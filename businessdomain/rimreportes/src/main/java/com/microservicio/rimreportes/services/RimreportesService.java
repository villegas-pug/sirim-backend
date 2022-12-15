package com.microservicio.rimreportes.services;

import java.util.List;
import com.microservicio.rimreportes.model.dto.RptAñosControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptDependenciaControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptEdadesControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptNacionalidadControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptProduccionDiariaDto;

public interface RimreportesService {
   
   // ► Repo method's ...
   List<RptControlMigratorioDto> getRptControlMigratorio(int año, String nacionalidad);
   List<RptAñosControlMigratorioDto> getRptAñosControlMigratorio();
   List<RptDependenciaControlMigratorioDto> getRptDependenciaControlMigratorio(int año, String nacionalidad);
   List<RptEdadesControlMigratorioDto> getRptEdadesControlMigratorio(int año, String nacionalidad);
   List<RptNacionalidadControlMigratorioDto> getRptNacionalidadControlMigratorio(int año);
   List<RptProduccionDiariaDto> getRptProduccionDiaria(String fecIni, String fecFin);

}

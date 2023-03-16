package com.microservicio.rimreportes.controllers;

import java.util.List;
import com.commons.utils.constants.Messages;
import com.commons.utils.utils.Response;
import com.microservicio.rimreportes.model.dto.RptAñosControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptDependenciaControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptEdadesControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptNacionalidadControlMigratorioDto;
import com.microservicio.rimreportes.model.dto.RptProduccionDiariaDto;
import com.microservicio.rimreportes.model.dto.RptProyeccionAnalisisDto;
import com.microservicio.rimreportes.model.entities.ProyeccionAnalisis;
import com.microservicio.rimreportes.services.RimreportesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;


@CrossOrigin(origins = { "*" })
@RestController
public class RimreportesController {

   @Autowired
   private RimreportesService service;

   @GetMapping( path = { "/getRptControlMigratorio" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<RptControlMigratorioDto>> getRptControlMigratorio(@RequestParam int año,@RequestParam String nacionalidad) {
       return Response
                  .<List<RptControlMigratorioDto>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                  .data(this.service.getRptControlMigratorio(año, nacionalidad))
                  .build();
   }

   @GetMapping( path = { "/getRptAñosControlMigratorio" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<RptAñosControlMigratorioDto>> getRptAñosControlMigratorio() {
       return Response
                  .<List<RptAñosControlMigratorioDto>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY())
                  .data(this.service.getRptAñosControlMigratorio())
                  .build();
   }

   @GetMapping( path = { "/getRptDependenciaControlMigratorio" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<RptDependenciaControlMigratorioDto>> getRptDependenciaControlMigratorio(@RequestParam int año, @RequestParam String nacionalidad) {
       return Response
                  .<List<RptDependenciaControlMigratorioDto>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY())
                  .data(this.service.getRptDependenciaControlMigratorio(año, nacionalidad))
                  .build();
   }

   @GetMapping( path = { "/getRptEdadesControlMigratorio" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<RptEdadesControlMigratorioDto>> getRptEdadesControlMigratorio(@RequestParam int año, @RequestParam String nacionalidad) {
       return Response
                  .<List<RptEdadesControlMigratorioDto>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY())
                  .data(this.service.getRptEdadesControlMigratorio(año, nacionalidad))
                  .build();
   }
   
   @GetMapping( path = { "/getRptNacionalidadControlMigratorio" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<RptNacionalidadControlMigratorioDto>> getRptNacionalidadControlMigratorio(@RequestParam int año) {
       return Response
                  .<List<RptNacionalidadControlMigratorioDto>>builder()
                  .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY())
                  .data(this.service.getRptNacionalidadControlMigratorio(año))
                  .build();
   }

   @GetMapping( path = { "/getRptProduccionDiaria" } )
   @ResponseStatus(value = HttpStatus.OK)
   public Response<List<RptProduccionDiariaDto>> getRptProduccionDiaria(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String fecIni,
                                                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String fecFin) {
       return Response
                .<List<RptProduccionDiariaDto>>builder()
                .message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
                .data(this.service.getRptProduccionDiaria(fecIni, fecFin))
                .build();
   }
   
   @GetMapping( path = { "/getRptProyeccionAnalisis" } )
	@ResponseStatus(value = HttpStatus.OK)
   public Response<List<RptProyeccionAnalisisDto>> getRptProyeccionAnalisis(@RequestParam int año) {
		return Response
					.<List<RptProyeccionAnalisisDto>>builder()
					.message(Messages.MESSAGE_SUCCESS_LIST_ENTITY)
					.data(this.service.getRptProyeccionAnalisis(año))
					.build();
   }
   
}

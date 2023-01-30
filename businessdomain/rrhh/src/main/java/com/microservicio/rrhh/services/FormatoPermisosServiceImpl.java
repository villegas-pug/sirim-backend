package com.microservicio.rrhh.services;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.errors.NotFoundDownloadException;
import com.commons.utils.models.entities.Usuario;
import com.microservicio.rrhh.constants.JornadaConstant;
import com.microservicio.rrhh.constants.RegimenConstant;
import com.microservicio.rrhh.constants.TipoLicenciaMap;
import com.microservicio.rrhh.models.entities.FormatoPermisos;
import com.microservicio.rrhh.repository.FormatoPermisosRepository;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class FormatoPermisosServiceImpl implements FormatoPermisosService {

   @Autowired
   private FormatoPermisosRepository repository;

   @Value("classpath:static/Formato de Autorización de Permisos o Licencia.xlsx")
   private Resource permisoTemplate;

   @Override
   @Transactional(readOnly = true)
   public List<FormatoPermisos> findAllFormatoPermisos() {
      List<FormatoPermisos> formatoPermisosDb = this.repository.findAll(Sort.by(Sort.Direction.DESC, "fechaCreacion"));
      if (formatoPermisosDb.size() == 0) throw new DataAccessEmptyWarning();
      return formatoPermisosDb;
   }

   @Override
   @Transactional(readOnly = true)
   public List<FormatoPermisos> findFormatoPermisosByUsrCreador(Usuario usrCreador) {
      List<FormatoPermisos> formatoPermisosDb = this.repository.findByUsrCreador(usrCreador, Sort.by(Sort.Direction.DESC, "fechaCreacion"));
      if (formatoPermisosDb.size() == 0) throw new DataAccessEmptyWarning();
      return formatoPermisosDb;
   }

   @Override
   @Transactional
   public void saveFormatoPermisos(FormatoPermisos formatoPermisos) {

      // Dep's ...
      boolean isNewFormatoPermiso = formatoPermisos.getIdFormato() == null;

      if (!isNewFormatoPermiso) {
         
         // Repo dep's ...
         FormatoPermisos formatPermisoOld = this.repository.findById(formatoPermisos.getIdFormato())
                                                           .orElseThrow(DataAccessEmptyWarning::new);

         formatoPermisos.setFechaCreacion(formatPermisoOld.getFechaCreacion());
         formatoPermisos.setAtendido(formatPermisoOld.isAtendido());
         formatoPermisos.setActivo(formatPermisoOld.isActivo());

      }

      this.repository.save(formatoPermisos);
   }

   @Override
   @Transactional(readOnly = true)
   public FormatoPermisos findFormatoPermisosById(Long idFormato) {
      FormatoPermisos formatoPermisos = this.repository.findById(idFormato)
                                                       .orElseThrow(DataAccessEmptyWarning::new);
      return formatoPermisos;
   }

   @Override
   @Transactional
   public void deleteFormatoPermisosById(Long idFormato) {
      this.repository.deleteById(idFormato);  
   }

   @Override
   @Transactional
   public void validateFormatoPermisos(Long idFormato) {
      // » Repo dep's ...
      FormatoPermisos formatoPermisos = this.repository.findById(idFormato)
                                                       .orElseThrow(DataAccessEmptyWarning::new);

      formatoPermisos.setAtendido(!formatoPermisos.isAtendido());

      this.repository.save(formatoPermisos);
   }

   //#region other method's ...

   @Override
   public ByteArrayResource convertPermisosTemplateToByteArrResource(Long idFormato) {
      
      //» Dep's
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      FormatoPermisos formatoPermiso = this.repository.findById(idFormato)
                                                      .orElseThrow(DataAccessEmptyWarning::new);


      //» ...
      try (XSSFWorkbook wbPermiso = new XSSFWorkbook(permisoTemplate.getInputStream())) {
         
         //» Dep's
         XSSFSheet sPermiso = wbPermiso.getSheetAt(0);

         //» id
         XSSFRow rId = sPermiso.getRow(5);
         rId.getCell(4).setCellValue(formatoPermiso.getIdFormato());

         //» Jormada laboral ...
         XSSFRow rJornada = sPermiso.getRow(1);
         if (formatoPermiso.getJornadaLaboral().equals(JornadaConstant.OTRO_HORARIO_LABORAL))
            rJornada = sPermiso.getRow(2);

         rJornada.getCell(14).setCellValue("(     X     )");

         //» Régimen laboral ...
         XSSFRow rRegimen = sPermiso.getRow(5);
         if (formatoPermiso.getRegimenLaboral().equals(RegimenConstant.CAS))
            rRegimen.getCell(14).setCellValue(formatoPermiso.getRegimenLaboral().concat("( X )"));
         else
            rRegimen.getCell(12).setCellValue(formatoPermiso.getRegimenLaboral().concat("( X )"));

         //» Nombres ...
         XSSFRow rNombres = sPermiso.getRow(6);
         rNombres.getCell(2).setCellValue("APELLIDOS Y NOMBRES: ".concat(formatoPermiso.getNombres()));

         //» Gerencia ...
         XSSFRow rGerencia = sPermiso.getRow(7);
         rGerencia.getCell(2).setCellValue("GERENCIA U OFICINA: ".concat(formatoPermiso.getGerencia()));

         //» Subgerencia ...
         XSSFRow rSubgerencia = sPermiso.getRow(8);
         rSubgerencia.getCell(2).setCellValue("SUB GERENCIA: ".concat(formatoPermiso.getSubgerencia()));

         //» Unidad ...
         XSSFRow rUnidad = sPermiso.getRow(8);
         rUnidad.getCell(9).setCellValue("UNIDAD: ".concat(formatoPermiso.getUnidad()));

         //» Tipo licencia ...
         XSSFRow rTipoLicencia = sPermiso.getRow(TipoLicenciaMap.TipoLicencia.get(formatoPermiso.getTipoLicencia()));
         rTipoLicencia.getCell(2).setCellValue("X");

         //» Duración ...
         XSSFRow rDesde = sPermiso.getRow(13);
         XSSFRow rHasta = sPermiso.getRow(23);
         XSSFRow rTotal = sPermiso.getRow(31);

         if (formatoPermiso.getTipoLicencia().contains("LICENCIA")) {
            rDesde.getCell(12).setCellValue(formatoPermiso.getDesde());
            rHasta.getCell(12).setCellValue(formatoPermiso.getHasta());
            rTotal.getCell(12).setCellValue(formatoPermiso.getTotalHoras());
         } else {
            rDesde.getCell(14).setCellValue(formatoPermiso.getDesde());
            rHasta.getCell(14).setCellValue(formatoPermiso.getHasta());
            rTotal.getCell(14).setCellValue(formatoPermiso.getTotalHoras());
         }
         
         //» Justificación ...
         XSSFRow rJustificacion = sPermiso.getRow(40);
         rJustificacion.getCell(6).setCellValue(formatoPermiso.getJustificacion());

         //» Fecha formato ...
         XSSFRow rFechaFormato = sPermiso.getRow(41);
         rFechaFormato.getCell(13).setCellValue(dateFormat.format(formatoPermiso.getFechaFormato()));

         wbPermiso.write(outputStream);

      } catch (Exception e) {

         log.error(e.getMessage());
         throw new NotFoundDownloadException();

      }

      return new ByteArrayResource(outputStream.toByteArray());
   }

   //#endregion

   
}
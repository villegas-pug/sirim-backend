package com.microservicio.rrhh.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.errors.FileSaveWarnning;
import com.commons.utils.errors.NotFoundDownloadException;
import com.commons.utils.errors.UserNotFoundWarning;
import com.commons.utils.helpers.DataModelHelper;
import com.commons.utils.models.entities.Usuario;
import com.microservicio.rrhh.constants.JornadaConstant;
import com.microservicio.rrhh.constants.RegimenConstant;
import com.microservicio.rrhh.constants.TipoLicenciaMap;
import com.microservicio.rrhh.models.entities.FormatoPermisos;
import com.microservicio.rrhh.models.enums.AttachmentType;
import com.microservicio.rrhh.models.enums.ValidateType;
import com.microservicio.rrhh.repository.ControlAsistenciaRepository;
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
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class RrhhServiceImpl implements RrhhService {

   @Autowired
   private FormatoPermisosRepository formatoPermisoRepository;

   @Autowired
   private ControlAsistenciaRepository ctrlAsistenciaRepository;

   @Value("classpath:static/Formato de Autorización de Permisos o Licencia.xlsx")
   private Resource permisoTemplate;

   @Override
   @Transactional(readOnly = true)
   public List<FormatoPermisos> findAllFormatoPermisos() {
      List<FormatoPermisos> formatoPermisosDb = this.formatoPermisoRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaCreacion"));
      if (formatoPermisosDb.size() == 0) throw new DataAccessEmptyWarning();
      return formatoPermisosDb;
   }

   @Override
   @Transactional(readOnly = true)
   public List<FormatoPermisos> findFormatoPermisosByUsrCreador(Usuario usrCreador) {
      List<FormatoPermisos> formatoPermisosDb = this.formatoPermisoRepository.findByUsrCreador(usrCreador, Sort.by(Sort.Direction.DESC, "fechaCreacion"));
      if (formatoPermisosDb.size() == 0) throw new DataAccessEmptyWarning();
      return formatoPermisosDb;
   }

   @Override
   @Transactional
   public void saveFormatoPermisos(FormatoPermisos formatoPermisos) {

      // Dep's ...
      boolean isNewFormatoPermiso = formatoPermisos.getIdFormato() == null;
      String servidor = formatoPermisos.getNombres();

      if (this.ctrlAsistenciaRepository.findServidorByNombre(servidor).isEmpty())
         throw new UserNotFoundWarning(servidor);

      if (isNewFormatoPermiso) { this.formatoPermisoRepository.save(formatoPermisos); } 
      else {

         // Repo dep's ...
         FormatoPermisos formatoPermisoOld = this.formatoPermisoRepository.findById(formatoPermisos.getIdFormato())
                                                                          .orElseThrow(DataAccessEmptyWarning::new);
   
         formatoPermisoOld.setJornadaLaboral(formatoPermisos.getJornadaLaboral());
         formatoPermisoOld.setRegimenLaboral(formatoPermisos.getRegimenLaboral());
         formatoPermisoOld.setNombres(formatoPermisos.getNombres());
         formatoPermisoOld.setGerencia(formatoPermisos.getGerencia());
         formatoPermisoOld.setSubgerencia(formatoPermisos.getSubgerencia());
         formatoPermisoOld.setTipoLicencia(formatoPermisos.getTipoLicencia());
         formatoPermisoOld.setDesde(formatoPermisos.getDesde());
         formatoPermisoOld.setHasta(formatoPermisos.getHasta());
         formatoPermisoOld.setTotalHoras(formatoPermisos.getTotalHoras());
         formatoPermisoOld.setFechaFormato(formatoPermisos.getFechaFormato());
         formatoPermisoOld.setJustificacion(formatoPermisos.getJustificacion());

         this.formatoPermisoRepository.save(formatoPermisoOld);
         
      }

   }

   @Override
   @Transactional(readOnly = true)
   public FormatoPermisos findFormatoPermisosById(Long idFormato) {
      FormatoPermisos formatoPermisos = this.formatoPermisoRepository.findById(idFormato)
                                                       .orElseThrow(DataAccessEmptyWarning::new);
      return formatoPermisos;
   }

   @Override
   @Transactional
   public void deleteFormatoPermisosById(Long idFormato) {
      this.formatoPermisoRepository.deleteById(idFormato);  
   }

   @Override
   @Transactional
   public void validateFormatoPermisos(Long idFormato, ValidateType type) {
      // » Repo dep's ...
      FormatoPermisos formatoPermisosOld = this.formatoPermisoRepository.findById(idFormato)
                                                                        .orElseThrow(DataAccessEmptyWarning::new);

      switch (type) {
      case ATENDIDO:
         formatoPermisosOld.setAtendido(!formatoPermisosOld.isAtendido());
         break;
      case RECIBIDO:
         // Si estado anterior es TRUE, entonces elimina las observaciones ...
         if (!formatoPermisosOld.isRecibido()) formatoPermisosOld.setObservaciones("");
         formatoPermisosOld.setRecibido(!formatoPermisosOld.isRecibido());
         break;
      }

      this.formatoPermisoRepository.save(formatoPermisosOld);
   }

   @Override
   @Transactional
   public Long saveAllControlAsistencia(MultipartFile file) throws IOException {
      
      // Dep's ...
      String nameTable = "RrhhControlAsistencia";
      StringBuilder sqlStatement = new StringBuilder();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Long totalRecordsInserted = 0L;

      // ...
      try (XSSFWorkbook wbCtrlAsistencia = new XSSFWorkbook(file.getInputStream())) {
         
         XSSFSheet sCtrlAsistencia = wbCtrlAsistencia.getSheetAt(0);

         int totalRecords = sCtrlAsistencia.getPhysicalNumberOfRows();

         for (int i = 1; i < totalRecords; i++) {
            
            XSSFRow rCtrlA = sCtrlAsistencia.getRow(i);

            sqlStatement.append("INSERT INTO ").append(nameTable)
                        .append("(nUsuarioNro, nIdUsuario, sNombre, dFechaHoraIngreso)")
                        .append(" VALUES(").append((int)rCtrlA.getCell(0).getNumericCellValue()).append(", ")
                        .append((int)rCtrlA.getCell(1).getNumericCellValue()).append(", '")
                        .append(rCtrlA.getCell(2).getStringCellValue()).append("', '")
                        .append(dateFormat.format(rCtrlA.getCell(3).getDateCellValue())).append("');");

         }

      }

      totalRecordsInserted = this.ctrlAsistenciaRepository.saveTablaDinamica(nameTable, sqlStatement.toString());
      if (totalRecordsInserted == 0L) {
         throw new FileSaveWarnning(file.getName());
      }

      return totalRecordsInserted;

   }

   @Override
   @Transactional
   public Long deleteAllControlAsistencia() {
      return this.ctrlAsistenciaRepository.saveTablaDinamica("", "TRUNCATE TABLE RrhhControlAsistencia;");
   }

   @Override
   @Transactional(readOnly = true)
   public List<Map<String, Object>> findAllControlAsistenciaUsrs() {
      List<Map<String, Object>> controlAsistenciaUsrs = DataModelHelper.convertTuplesToJson(this.ctrlAsistenciaRepository.findAllControlAsistenciaUsrs(), true);
      if (controlAsistenciaUsrs.size() == 0) throw new DataAccessEmptyWarning();
      return controlAsistenciaUsrs;
   }

   @Override
   @Transactional(readOnly = true)
   public Long countControlAsistencias() {
      return this.ctrlAsistenciaRepository.count();
   }

   @Override
   @Transactional(readOnly = true)
   public List<Map<String, Object>> findControlPermisosByServidor(String servidor) {
      List<Map<String, Object>> controlPermisos = DataModelHelper.convertTuplesToJson(this.ctrlAsistenciaRepository.findControlPermisosByServidor(servidor), false);
      if (controlPermisos.size() == 0) throw new DataAccessEmptyWarning();
      return controlPermisos;
   }

   @Override
   @Transactional
   public void saveAttachment(MultipartFile file, AttachmentType type, Long idFormato) throws IOException {
      
      // » Dep's ...
      byte[] attachment = file.getBytes();
      FormatoPermisos formatoPermiso = this.formatoPermisoRepository.findById(idFormato)
                                                                    .orElseThrow(DataAccessEmptyWarning::new);
      switch (type) {
         case FORMATO:
            formatoPermiso.setFormato(attachment);
            break;
         case SUSTENTO:
            formatoPermiso.setSustento(attachment);
            break;
      }

      this.formatoPermisoRepository.save(formatoPermiso);
      
   }

   @Override
   @Transactional(readOnly = true)
   public Resource findAttachmentById(Long idFormato, AttachmentType type) {
      
      // » Dep's ...
      Resource attachment = null;
      FormatoPermisos formatoPermiso = this.formatoPermisoRepository.findById(idFormato)
                                                                    .orElseThrow(DataAccessEmptyWarning::new);
      switch (type) {
         case FORMATO:
            attachment = new ByteArrayResource(formatoPermiso.getFormato());
            break;
         case SUSTENTO:
            attachment = new ByteArrayResource(formatoPermiso.getSustento());
            break;
      }

      return attachment;
      
   }

   @Override
   @Transactional
   public void saveObservacionesFormatoPermisos(String observaciones, Long idFormato) {
      
         // Repo dep's ...
         FormatoPermisos formatoPermisoOld = this.formatoPermisoRepository.findById(idFormato)
                                                                          .orElseThrow(DataAccessEmptyWarning::new);
   
         formatoPermisoOld.setObservaciones(observaciones);

         this.formatoPermisoRepository.save(formatoPermisoOld);
      
   }

   //#region other method's ...

   @Override
   public ByteArrayResource convertPermisosTemplateToByteArrResource(Long idFormato) {
      
      //» Dep's
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      FormatoPermisos formatoPermiso = this.formatoPermisoRepository.findById(idFormato)
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
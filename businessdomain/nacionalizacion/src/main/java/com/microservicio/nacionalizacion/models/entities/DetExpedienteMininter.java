package com.microservicio.nacionalizacion.models.entities;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.microservicio.nacionalizacion.models.enums.EstadoDocumento;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SidDetExpedienteMininter")
@Data
@EqualsAndHashCode(of = { "idDetExpMininter" })
public class DetExpedienteMininter {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdDetExpMininter")
   private Long idDetExpMininter;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdExpMininter")
   private ExpedienteMininter expedienteMininter;
   
   @Column(name = "sNumeroOficio", length = 100)
   private String numeroOficio;
   
   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   @Column(name = "dFechaOficio")
   private Date fechaOficio;
   
   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   @Column(name = "dFechaRecepcion")
   private Date fechaRecepcion;

   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   @Column(name = "dFechaRespuesta")
   private Date fechaRespuesta;

   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   @Column(name = "dFechaVencimiento")
   private Date fechaVencimiento;

   @Enumerated(EnumType.STRING)
   @Column(name = "sEstado", length = 15)
   private EstadoDocumento estado;

   @OneToMany(mappedBy = "detExpMininter", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
   @JsonIgnoreProperties(value = { "detExpMininter" }, allowSetters = true)
   private List<MailFile> mailFiles;

   @Column(name = "sAccionesRealizadas", length = 300)
   private String accionesRealizadas;
   
   @Temporal(TemporalType.DATE)
   @Column(name = "dFechaRegistro")
   private Date fechaRegistro;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdUbicacion")
   private UbicacionExpediente ubicacion;
   
   @PrePersist
   private void prePersist(){
      this.fechaRegistro = new Date();
      this.updateEstado();
      this.updateFechaVencimiento();
   }

   @PreUpdate
   private void preUpdate(){
      this.updateEstado();
      this.updateFechaVencimiento();
   }

   public DetExpedienteMininter() {
      this.mailFiles = new ArrayList<>();
   }

   public void addMailFile(MailFile file){
      file.setDetExpMininter(this);
      this.mailFiles.add(file);
   }

   private void updateEstado(){
      if (this.getFechaRespuesta() != null) this.setEstado(EstadoDocumento.ATENDIDO);
      else this.setEstado(EstadoDocumento.PENDIENTE);
   }

   private void updateFechaVencimiento(){
      Optional<Date> fechaOficio = Optional.ofNullable(this.getFechaOficio());
      Optional<Date> fechaRecepcion = Optional.ofNullable(this.getFechaRecepcion());

      fechaOficio.ifPresent((fecOf) -> this.setFechaVencimiento(this.calcFechaVencimiento(fecOf)));
      fechaRecepcion.ifPresent((fecRec) -> this.setFechaVencimiento(this.calcFechaVencimiento(fecRec)));
   }
   
   private Date calcFechaVencimiento(Date fecIni) {
      LocalDate localDate = fecIni.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      int daysExpiration = DIAS_LIMITE_VENCIMIENTO;
      while (daysExpiration > 0) {
         /* DayOfWeek dayName = localDate.getDayOfWeek(); */
         localDate = localDate.plusDays(1);
         if (localDate.getDayOfWeek() == DayOfWeek.SATURDAY || localDate.getDayOfWeek() == DayOfWeek.SUNDAY) { continue; }
         --daysExpiration;
      }

      Date fecVenc = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
      return fecVenc;
   }

   /*» Constant's:  */
   private static final int DIAS_LIMITE_VENCIMIENTO = 25;
}

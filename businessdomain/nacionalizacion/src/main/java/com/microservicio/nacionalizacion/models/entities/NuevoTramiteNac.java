package com.microservicio.nacionalizacion.models.entities;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.commons.utils.models.entities.Etapa;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.microservicio.nacionalizacion.models.enums.EstadoTramite;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "SidNuevoTramiteNac")
@Data
@EqualsAndHashCode(of = { "idTramiteNac" })
@Builder(builderClassName = "NuevoTramiteNacBuilder", builderMethodName = "of", buildMethodName = "get")
@AllArgsConstructor
/* @JsonInclude(Include.NON_EMPTY) */
public class NuevoTramiteNac implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdTramiteNac")
   private Long idTramiteNac;
   
   @OneToOne(mappedBy = "tramiteNac", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
   @JsonIgnoreProperties(value = { "tramiteNac" })
   private EvaluarTramiteNac evaluarTramiteNac;

   @OneToMany(mappedBy = "tramiteNac", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
   @JsonIgnoreProperties(value = { "tramiteNac" })
   private List<EtapaTramiteNac> etapaTramiteNac;

   @Column(name = "sNumeroTramite", length = 25, nullable = false)
   private String numeroTramite;

   @Column(name = "nIdTipoTramite", columnDefinition = "SMALLINT NULL")
   private Long idTipoTramite;

   @Column(name = "sTipoTramite", length = 100, nullable = false)
   private String tipoTramite;

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "dFechaTramite", nullable = false)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "America/Lima")
   private Date fechaTramite;

   @Temporal(TemporalType.DATE)
   @Column(name = "dFechaRecepcionMP")
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   private Date fechaRecepcionMP;

   @Column(name = "sAdministrado", length = 150, nullable = false)
   private String administrado;

   @Column(name = "sSexo", columnDefinition = "CHAR(1) NOT NULL")
   private String sexo;

   @Enumerated(EnumType.STRING)
   @Column(name = "sEstadoTramite", nullable = false)
   private EstadoTramite estadoTramite;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdEtapa", nullable = false)
   private Etapa etapa;

   @Column(name = "sPaisNacimiento", length = 100, nullable = false)
   private String paisNacimiento;

   @Column(name = "sDependencia", length = 55, nullable = false)
   private String dependencia;

   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "America/Lima")
   @Column(name = "dFechaAud", nullable = false)
   private Date fechaAud;
   
   @Transient
   private Date fecVencAtencion;

   @Transient
   private boolean isVencido;

   public NuevoTramiteNac() {
      /* this.evaluarTramiteNac = new EvaluarTramiteNac(); */
      this.etapaTramiteNac = new ArrayList<>();
   }

   public void setEvaluarTramiteNac(EvaluarTramiteNac evaluarTramiteNac){
      evaluarTramiteNac.setTramiteNac(this);
      this.evaluarTramiteNac = evaluarTramiteNac;
   }

   public void removeEvaluarTramiteNac(EvaluarTramiteNac evaluarTramiteNac){
      evaluarTramiteNac.setTramiteNac(null);
      this.evaluarTramiteNac = null;
   }

   public void addEtapaTramiteNac(EtapaTramiteNac etapaTramiteNac){
      etapaTramiteNac.setTramiteNac(this);
      this.etapaTramiteNac.add(etapaTramiteNac);
   }

   public Date getFecVencAtencion(){ return calcFechaVencimiento(this.fechaTramite); }

   public boolean getIsVencido(){
      Date now = new Date();
      return now.after(this.getFecVencAtencion()) && this.estadoTramite == EstadoTramite.P;
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
      
      return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
   }

   /*
   * 
   */
  private static final long serialVersionUID = 1L;
  private static final int DIAS_LIMITE_VENCIMIENTO = 25;

}

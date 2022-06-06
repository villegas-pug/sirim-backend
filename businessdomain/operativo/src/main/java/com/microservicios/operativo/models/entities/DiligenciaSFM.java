package com.microservicios.operativo.models.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Entity
@Table(name = "SidDiligenciaSFM")
@Data
@Builder(builderClassName = "DiligenciaSFMBuilder", builderMethodName = "of", buildMethodName = "get")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "idDiligencia" })
@Log4j2
public class DiligenciaSFM implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdDiligencia")
   private Long idDiligencia;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdVerifExp")
   private EvaluarSolicitudSFM evaluarSolicitud;

   @OneToMany(mappedBy = "diligencia", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JsonIgnoreProperties(value = { "diligencia" })
   private List<ArchivoSFM> archivos;
   
   @Column(name = "sTipoDocumento", length = 100)
   private String tipoDocumento;

   @Column(name = "sNumeroDocumento", length = 100)
   private String numeroDocumento;

   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", locale = "America/Lima")
   @Column(name = "dFechaDocumento")
   private Date fechaDocumento;

   @Column(name = "bSolicitudExterna")
   private boolean solicitudExterna;

   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", locale = "America/Lima")
   @Column(name = "dFechaSolicitud")
   private Date fechaSolicitud;

   @Column(name = "sDependenciaDestino", length = 100)
   private String dependenciaDestino;

   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", locale = "America/Lima")
   @Column(name = "dFechaRespuesta")
   private Date fechaRespuesta;

   @Column(name = "bRespuesta")
   private boolean respuesta;

   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "America/Lima")
   @Column(name = "dFechaHoraRegistro")
   private Date fechaHoraRegistro;

   @PrePersist
   private void prePersist(){
      this.respuesta = false;
      this.fechaHoraRegistro = new Date();
      this.respuesta = false;
      log.error("prePersist: DiligenciaSFM");
   }
 
   @PostPersist
   private void postPersist(){
      log.error("postPersist: DiligenciaSFM");
   }

   @PostUpdate
   private void postUpdate(){
      if(this.getFechaRespuesta() != null)
         this.respuesta = true;
   }

   /* 
   *
   */
   private static final long serialVersionUID = 1L;
}
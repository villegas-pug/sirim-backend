package com.microservicio.nacionalizacion.models.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.microservicio.nacionalizacion.models.enums.EstadoEtapa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SidEvalRequisitoTramiteNac")
@Data
@EqualsAndHashCode(of = { "idEvalReqTramite" })
@Builder(builderClassName = "EvalRequisitoTramiteNacBuilder", builderMethodName = "of", buildMethodName = "get")
@NoArgsConstructor
@AllArgsConstructor
public class EvalRequisitoTramiteNac implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdEvalReqTramite")
   private Long idEvalReqTramite;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdVerifExp", nullable = false)
   private EvaluarTramiteNac evaluarTramiteNac;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdReqTipoTramite", nullable = false)
   private RequisitoTipoTramite requisitoTipoTramite;

   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "America/Lima")
   @Column(name = "dFechaHoraInicio", nullable = false)
   private Date fechaHoraInicio;

   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "America/Lima")
   @Column(name = "dFechaHoraFin", nullable = true)
   private Date fechaHoraFin;

   @Enumerated(EnumType.STRING)
   @Column(name = "sEstado", nullable = false, length = 1)
   private EstadoEtapa estado;

   @Column(name = "sObservaciones", length = 300)
   private String observaciones;

   @PrePersist
   private void prePersist(){
      this.fechaHoraInicio = new Date();
      this.estado = EstadoEtapa.I;
   }

   /*
   *
   */
   private static final long serialVersionUID = 1L;

}
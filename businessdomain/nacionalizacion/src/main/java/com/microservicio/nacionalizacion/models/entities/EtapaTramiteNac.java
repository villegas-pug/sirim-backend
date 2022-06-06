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

import com.commons.utils.models.entities.Etapa;
import com.commons.utils.models.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.microservicio.nacionalizacion.models.enums.EstadoEtapa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SidEtapaTramiteNac")
@Data
@EqualsAndHashCode(of = { "idEtapaTramite" })
@Builder(builderClassName = "EtapaTramiteNacBuilder", builderMethodName = "of", buildMethodName = "get")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class EtapaTramiteNac implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdEtapaTramite")
   private Long idEtapaTramite;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdTramiteNac")
   private NuevoTramiteNac tramiteNac;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdEtapa", nullable = false)
   private Etapa etapa;
   
   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "America/Lima")
   @Column(name = "dFechaHoraInicio", nullable = false)
   private Date fechaHoraInicio;
   
   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "America/Lima")
   @Column(name = "dFechaHoraFin")
   private Date fechaHoraFin;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "uIdUsrInicia", nullable = false)
   private Usuario usrInicia;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "uIdUsrFinaliza")
   private Usuario usrFinaliza;
   
   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "America/Lima")
   @Column(name = "dFechaHoraAud", nullable = false)
   private Date fechaHoraAud;
   
   @Column(name = "sObservaciones", length = 500)
   private String observaciones;

   @Enumerated(EnumType.STRING)
   @Column(name = "sEstado", nullable = false)
   private EstadoEtapa estado;

   @PrePersist
   private void prePersist(){
      this.fechaHoraAud = new Date();
      this.fechaHoraInicio = new Date();
   }

   /*
   *
   */
   private static final long serialVersionUID = 1L;
   
}
package com.microservicio.nacionalizacion.models.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.commons.utils.models.entities.TipoTramite;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SidRequisitoTipoTramite")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "RequisitoTipoTramiteBuilder", builderMethodName = "of", buildMethodName = "get")
public class RequisitoTipoTramite implements Serializable {

   @Id
   @Column(name = "nIdReqTipoTramite")
   private Long idReqTipoTramite;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdTipoTramite", nullable = false)
   private TipoTramite tipoTramite;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdRequisito", nullable = false)
   private Requisito requisito;
   
   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "America/Lima")
   @Column(name = "dFechaHoraAud", length = 55, nullable = false)
   private Date fechaHoraAud;
   
   @Column(name = "nSecuencia", columnDefinition = "TINYINT NOT NULL")
   private int secuencia;

   /*
   *
   */
   private static final long serialVersionUID = 1L;

}
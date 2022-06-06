package com.microservicios.operativo.models.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.microservicios.operativo.models.enums.TipoOpinion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder(builderClassName = "SidOpinionSFMBuilder", builderMethodName = "of", buildMethodName = "get")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "idOpinion" })
public class SidOpinionSFM implements Serializable {

   @Id
   @Column(name = "nIdOpinion")
   private Long idOpinion;

   @Column(name = "sDescripcion", length = 55, nullable = false)
   private String descripcion;

   @Enumerated(EnumType.STRING)
   @Column(name = "sTipo", columnDefinition = "CHAR(1) NOT NULL")
   private TipoOpinion tipo;

   @Column(name = "bActivo", nullable = false)
   private boolean activo;

   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   @Temporal(TemporalType.DATE)
   @Column(name = "dFechaCreación", nullable = false)
   private Date fechaCreación;

   /*
   *
   */
   private static final long serialVersionUID = 1L;

}
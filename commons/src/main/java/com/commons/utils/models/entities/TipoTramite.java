package com.commons.utils.models.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SidTipoTramite")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "TipoTramiteBuilder", builderMethodName = "of", buildMethodName = "get")
public class TipoTramite implements Serializable {

   @Id
   @Column(name = "nIdTipoTramite")
   private Long idTipoTramite;

   @Column(name = "sDescripcion", length = 100, nullable = false)
   private String descripcion;

   @Column(name = "sTipo", columnDefinition = "CHAR(1) NOT NULL")
   private String tipo;

   @Column(name = "bActivo", nullable = false)
   private boolean activo;

   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "America/Lima")
   @Column(name = "dFechaHoraAud", nullable = false)
   private Date fechaHoraAud;

   @Column(name = "sSigla", columnDefinition = "CHAR(3) NOT NULL")
   private String sigla;

   @PrePersist
   private void prePersist() {
      this.fechaHoraAud = new Date();
      this.activo = true;
   }

   /*
   *
   */
   private static final long serialVersionUID = 1L;

}
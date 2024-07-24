package com.commons.utils.models.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RimReglaNegocio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = { "idRN" })
public class ReglaNegocio {

   @Id
   @Column(name = "sIdRN")
   private String idRN;
   
   @Column(name = "sTablas", nullable = false)
   private String tablas;
   
   @Column(name = "sCampos", nullable = false)
   private String campos;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdProceso", nullable = false)
   private RNProceso proceso;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdDimensionRegla", nullable = false)
   private RNDimension dimensionRegla;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdStatusRegla", nullable = false)
   private RNStatus statusRegla;
   
   @Column(name = "dFechaCreacion", nullable = false)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", locale = "America/Lima")
   private Date fechaCreacion;
   
   @Column(name = "bActivo", nullable = false)
   private boolean activo;

   @PrePersist
   private void prePersist() {
      this.activo = true;
      this.fechaCreacion = new Date();
   }

}
package com.commons.utils.models.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.commons.utils.models.enums.RimGrupo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RimTipoLogico")
@Data
@Builder(builderClassName = "TipoLogicoBuilder", builderMethodName = "of", buildMethodName = "get")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "idTipo" })
public class TipoLogico {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdTipo")
   private Integer idTipo;
   
   @Column(name = "sNombre", unique = true, length = 100, nullable = false)
   private String nombre;
   
   @Enumerated(EnumType.STRING)
   @Column(name = "sGrupo", length = 15, nullable = false)
   private RimGrupo grupo;
   
   @Column(name = "sValoresCsv", columnDefinition = "VARCHAR(MAX) NULL")
   private String valoresCsv;
   
   @Column(name = "nLongitud", unique = true)
   private int longitud;
   
   @Column(name = "bActivo", nullable = false)
   private boolean activo;
   
   @Temporal(TemporalType.DATE)
   @Column(name = "dFechaRegistro", nullable = false)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   private Date fechaRegistro;

   @PrePersist
   private void prePersist(){
      this.activo = true;
      this.fechaRegistro = new Date();
   }

   @PreUpdate
   private void preUpdate(){
      this.fechaRegistro = new Date();
      this.activo = true;
   }
   
}
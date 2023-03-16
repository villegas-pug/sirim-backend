package com.microservicio.rimreportes.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.commons.utils.models.enums.RimGrupo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RimProyeccionAnalisis")
@Data
@Builder(builderClassName = "RimProyeccionAnalisisBuilder", builderMethodName = "of", buildMethodName = "get")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "idProyeccion" })
public class ProyeccionAnalisis {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdProyeccion")
   private int idProyeccion;

   @Enumerated(EnumType.STRING)
   @Column(name = "sGrupo", length = 55, nullable = false)
   private RimGrupo grupo;

   @Column(name = "nAño", columnDefinition = "SMALLINT NOT NULL")
   private int año;

   @Column(name = "nMes", columnDefinition = "TINYINT NOT NULL")
   private int mes;

   @Column(name = "nTotal", nullable = false)
   private int total;

   @Column(name = "bActivo", nullable = false)
   private boolean activo;

   @PrePersist
   private void prePersist(){
      this.activo = true;
   }

}

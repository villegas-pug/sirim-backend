package com.commons.utils.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.commons.utils.models.enums.TipoScript;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "RimRNTipoScript")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = { "idTipoScript" })
public class RNTipoScript {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdTipoScript", nullable = false)
   private int idTipoScript;
   
   @Enumerated(EnumType.STRING)
   @Column(name = "sDescripcion", nullable = false)
   private TipoScript descripcion;
   
   @Column(name = "bActivo", nullable = false)
   private boolean activo;

   @PrePersist
   private void prePersist() {
      this.activo = true;
   }
   
}
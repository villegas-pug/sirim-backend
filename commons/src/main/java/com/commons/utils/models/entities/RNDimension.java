package com.commons.utils.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "RimRNDimension")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = { "idDimension" })
public class RNDimension {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdDimension")
   private Long idDimension;
   
   @Column(name = "sNombre", nullable = false)
   private String nombre;
   
   @Column(name = "bActivo", nullable = false)
   private @Builder.Default boolean activo = true;

   @PrePersist
   private void prePersist() {
      this.activo = true;
   }
   
}
package com.microservicio.nacionalizacion.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Entity
@Table(name = "SidUbicacionExpediente")
@Data
@EqualsAndHashCode(of = { "idUbicacion" })
@NoArgsConstructor
@RequiredArgsConstructor
public class UbicacionExpediente {

   @NonNull
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdUbicacion")
   private Long idUbicacion;
   
   @Column(name = "sDescripcion", length = 100)
   private String descripcion;
   
   @Column(name = "bActivo")
   private boolean activo;

   @PrePersist
   private void prePersist(){
      this.activo = true;
   }
   
}

package com.microservicio.rimreglanegocio.models.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "RimRNProceso")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = { "idProceso" })
public class RNProceso {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdProceso")
   private Long idProceso;
   
   @Column(name = "sNombre", nullable = false)
   private String nombre;
   
   @Column(name = "sDescripcion", nullable = false)
   private String descripcion;
   
   @Column(name = "bActivo", nullable = false)
   private @Builder.Default boolean activo = true;
   
   @Column(name = "dFechaCreacion", nullable = false)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", locale = "America/Lima")
   private @Builder.Default Date fechaCreacion = new Date();

   @PrePersist
   private void prePersist() {
      this.activo = true;
      this.fechaCreacion = new Date();
   }
   
}
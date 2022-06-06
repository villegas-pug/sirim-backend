package com.commons.utils.models.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SidDependencia")
@Data
@EqualsAndHashCode(of = { "idDependencia" })
public class Dependencia implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdDependencia")
   private Long idDependencia;

   @Column(name = "sNombre", length = 100, nullable = false)
   private String nombre;

   @Column(name = "bActivo", nullable = false)
   private boolean activo;

   @PrePersist
   private void prePersist(){
      this.activo = true;
   }

   /*
   * 
   */
   private static final long serialVersionUID = 1L;   
}
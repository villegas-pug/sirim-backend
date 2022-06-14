package com.commons.utils.models.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SimDependencia")
@Data
@EqualsAndHashCode(of = { "idDependencia" })
public class Dependencia implements Serializable {

   @Id
   @Column(name = "sIdDependencia")
   private String idDependencia;

   @Column(name = "sNombre", length = 50, nullable = false)
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
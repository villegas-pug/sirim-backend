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
@Table(name = "SimPais")
@Data
@EqualsAndHashCode(of = { "idPais" })
public class Pais implements Serializable {

   @Id
   @Column(name = "sIdPais")
   private String idPais;

   @Column(name = "sNombre", length = 50, nullable = false)
   private String nombre;

   @Column(name = "sNacionalidad", length = 30, nullable = false)
   private String nacionalidad;

   @Column(name = "bActivo", nullable = false)
   private boolean activo;

   @PrePersist
   private void prePersist() {
      this.activo = true;
   }

   /**
    *
    */
   private static final long serialVersionUID = 1L;

}

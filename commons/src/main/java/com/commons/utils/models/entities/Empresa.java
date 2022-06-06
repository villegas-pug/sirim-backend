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
@Table(name = "SidEmpresa")
@Data
@EqualsAndHashCode(of = { "idEmpresa" })
public class Empresa implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdEmpresa")
   private Long idEmpresa;

   @Column(name = "sDescripcion", length = 300, nullable = false)
   private String descripcion;

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

package com.commons.utils.models.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SidComentarioNormativo")
@Data
@EqualsAndHashCode(of = { "idComentarioNorma" })
public class ComentarioNormativo implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdComentarioNorma")
   private String idComentarioNorma;

   @Column(name = "sDescripcion", length = 1500, nullable = true)
   private String descripcion;

   @Column(name = "bActivo", nullable = false)
   private boolean activo;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdInfNorma", nullable = false)
   private InformacionNormativa informacionNormativa;

   @PrePersist
   private void prePersist() {
      this.activo = true;
   }

   /**
   	 *
   	 */
   private static final long serialVersionUID = 1L;

}

package com.commons.utils.models.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SidInformacionNormativa")
@Data
@EqualsAndHashCode(of = { "idInfNorma" })
public class InformacionNormativa implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int idInfNorma;

   @Column(name = "sNombre", length = 100, nullable = false)
   private String nombre;

   @Column(name = "sDescripcion", length = 500, nullable = true)
   private String descripcion;

   @Column(name = "dFechaRegistro")
   @Temporal(TemporalType.DATE)
   @DateTimeFormat(pattern = "dd-mm-yyyy")
   private Date fechaRegistro;

   @Column(name = "xArchivo")
   @Lob
   @JsonIgnore
   private Byte[] archivo;

   /* » ManyToOne... */
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdGrupo", nullable = false)
   private GrupoNormativo grupo;

   @PrePersist
   private void prePersist() {
      this.fechaRegistro = new Date();
   }

   /**
    *
    */
   private static final long serialVersionUID = 1L;

}

package com.microservicio.produccion.models.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.commons.utils.models.enums.TipoActividad;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SidActividad")
@Data
@EqualsAndHashCode(of = { "idActividad" })
public class Actividad implements Serializable {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdActividad")
   private Long idActividad;
   
   @Enumerated(value = EnumType.STRING)
   @Column(name = "sTipo", length = 15, nullable = false)
   private TipoActividad tipo;
   
   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "dFechaAuditoria", nullable = false, columnDefinition = "DATETIME default GETDATE()")
   private Date fechaAuditoria = new Date();
   
   @Column(name = "sDescripcion", length = 500, nullable = false)
   private String descripcion;
   
   @Column(name = "bActivo", nullable = false, columnDefinition = "BIT default 1")
   private boolean activo = true;

   @PrePersist
   private void prePersist(){
      this.fechaAuditoria = new Date();
      this.activo = true;
   }

   /**
    * 
   */
   private static final long serialVersionUID = 1L;

}
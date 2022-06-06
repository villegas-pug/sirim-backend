package com.microservicio.produccion.models.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SidDetProduccion")
@Data
@EqualsAndHashCode(of = { "idDetProduccion" })
public class DetalleProduccion implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdDetProduccion")
   private Long idDetProduccion;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JsonIgnoreProperties(value = { "detalleProduccion" })
   @JoinColumn(name = "nIdProduccion", nullable = false)
   private Produccion produccion;
   
   @Column(name = "dFechaRegistro", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date fechaRegistro;
   
   @Column(name = "sDescripcionActividad", length = 500, nullable = false)
   private String descripcionActividad;
   
   @Column(name = "sAccionDesarrollada", length = 500, nullable = false)
   private String accionDesarrollada;
   
   @Column(name = "sObservaciones", length = 255, nullable = true)
   private String observaciones;

   @PrePersist
   private void prePersist(){
      this.fechaRegistro = new Date();
   }

   /**
    * 
    */
    private static final long serialVersionUID = 1L;
}
package com.microservicio.produccion.models.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.commons.utils.models.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SidProduccion")
@Data
@EqualsAndHashCode(of = {"idProduccion"})
public class Produccion implements Serializable {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdProduccion")
   private Long idProduccion;
   
   @JoinColumn(name = "uIdUsuario", nullable = false)
   @ManyToOne(fetch = FetchType.EAGER)
   private Usuario usuario;

   @OneToMany(mappedBy = "produccion", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
   @JsonIgnoreProperties(value = { "produccion" }, allowSetters = true)
   private List<DetalleProduccion> detalleProduccion;
   
   @Column(name = "dFechaRegistro", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date fechaRegistro;
   
   @Column(name = "nGradoAvanceDiarioEjecutado", nullable = false, precision = 1, scale = 1)
   private float gradoAvanceDiarioEjecutado;
   
   @Column(name = "sCumplimiento", length = 2, nullable = false)
   private String cumplimiento;
   
   @Column(name = "dFechaAuditoria", nullable = false)
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechaAuditoria;
   
   public Produccion() {
      this.detalleProduccion = new ArrayList<>();
   }

   @PrePersist
   private void prePersist(){
      this.fechaRegistro = new Date();
      this.fechaAuditoria = new Date();
      this.cumplimiento = "No";
   }
 
   @PostUpdate
   private void postUpdate(){
      this.fechaAuditoria = new Date();
   }

   public void addDetalleProduccion(DetalleProduccion detalleProduccion){
      detalleProduccion.setProduccion(this);
      this.detalleProduccion.add(detalleProduccion);
   }

   public void setDetalleProduccion(List<DetalleProduccion> lstDetalleProduccion){
      lstDetalleProduccion.forEach(this::addDetalleProduccion);
   }

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

}

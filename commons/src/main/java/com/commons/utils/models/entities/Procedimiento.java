package com.commons.utils.models.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.commons.utils.models.enums.TipoProcedimiento;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SidProcedimiento")
@Data
@EqualsAndHashCode(of = { "idProcedimiento" })
public class Procedimiento implements Serializable {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdProcedimiento")
   private Long idProcedimiento;
   
   @Enumerated(value = EnumType.STRING)
   @Column(name = "sTipo", length = 15, nullable = false)
   private TipoProcedimiento tipo;
   
   @Column(name = "sNombre", length = 55, nullable = false, unique = true)
   private String nombre;

   @Column(name = "sInformacion", length = 55)
   private String informacion;
   
   @Column(name = "sDescripcion", length = 200)
   private String descripcion;
   
   @Column(name = "sIcono", length = 50)
   private String icono;
   
   @Column(name = "bActivo", nullable = false)
   private boolean activo;
   
   @Column(name = "sRutaMod", length = 25, nullable = false)
   private String rutaMod;
   
   @Column(name = "sRutaSubmod", length = 25)
   private String rutaSubmod;

   @Column(name = "sRefItem", length = 55)
   private String refItem;
   
   @Column(name = "sDisposicion", length = 15)
   private String disposicion;

   @Column(name = "nSecuencia", nullable = true)
   private int secuencia;

   @Transient
   private String rutaPrincipal;

   @PrePersist
   private void prePersist(){
      this.activo = true;
   }

   public String getRutaPrincipal() {
      this.setRutaPrincipal(this.getRutaMod().concat(this.getRutaSubmod()));
      return this.rutaPrincipal;
   }

   public void setRutaPrincipal(String rutaPrincipal) {
      this.rutaPrincipal = rutaPrincipal;
   }

   /**
   *
   */
   private static final long serialVersionUID = 1L;
}
package com.microservicios.operativo.models.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SidOperativoJZ")
@Data
@EqualsAndHashCode(of = { "idJZOperativo" })
public class OperativoJZ implements Serializable {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdJZOperativo")
   private Long idJZOperativo;
   
   @Column(name = "sJefaturaZonal", length = 100, nullable = false)
   private String jefaturaZonal;

   @Column(name = "nAñoOpe", nullable = false, columnDefinition = "SMALLINT")
   private int añoOpe;
   
   @Column(name = "nTotalOperativos", nullable = false)
   private int totalOperativos;
   
   @Column(name = "nTotalIntervenidos", nullable = false)
   private int totalIntervenidos;
   
   @Column(name = "nTotalDisposicionPNP", nullable = false)
   private int totalDisposicionPNP;
   
   @Column(name = "bActivo", columnDefinition = "BIT DEFAULT 1")
   private boolean activo;
   
   @Column(name = "dFechaRegistro", columnDefinition = "DATETIME DEFAULT GETDATE()")
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechaRegistro;

   @PrePersist
   private void prePersist(){
      this.fechaRegistro = new Date();
      this.activo = true;
   }

   /**
   *
   */
   private static final long serialVersionUID = 1L;
}

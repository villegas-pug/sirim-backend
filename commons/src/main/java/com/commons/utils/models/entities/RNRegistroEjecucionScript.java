package com.commons.utils.models.entities;

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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "RimRNRegistroEjecucionScript")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = { "idRegistroEjecucion" })
public class RNRegistroEjecucionScript {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdRegistroEjecucion", nullable = false)
   private Long idRegistroEjecucion;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdRNControlCambio", nullable = false)
   private RNControlCambios controlCambio;
   
   @Column(name = "nResultado", nullable = false)
   private Long resultado;
   
   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "dFechaEjecucion", nullable = false)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "America/Lima")
   private Date fechaEjecucion;
   
   @Column(name = "bActivo", nullable = false)
   private boolean activo;

   @PrePersist
   private void prePersist() {
      this.activo = true;
   }
   
}
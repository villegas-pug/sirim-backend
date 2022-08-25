package com.microservicio.rimextraccion.models.entities;

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
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RimProduccionAnalisis")
@Data
@Builder(builderClassName = "RimProduccionAnalisisBuilder", builderMethodName = "of", buildMethodName = "get")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "idProdAnalisis" })
public class ProduccionAnalisis implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdProdAnalisis")
   private Long idProdAnalisis;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdAsigGrupo", nullable = false)
   private AsigGrupoCamposAnalisis asigGrupo;
   
   @Column(name = "nIdRegistroAnalisis", nullable = false)
   private Long idRegistroAnalisis;
   
   @Column(name = "bCompleto",  nullable = false)
   private boolean completo;
   
   @Column(name = "bRevisado", columnDefinition = "VARCHAR(MAX) NULL")
   private @Builder.Default boolean revisado = false;
   
   @Column(name = "sObservacionesCtrlCal", columnDefinition = "VARCHAR(MAX) NULL")
   private String observacionesCtrlCal;
   
   @Column(name = "sMetaFieldIdErrorCsv", columnDefinition = "VARCHAR(MAX) NULL")
   private String metaFieldIdErrorCsv;

   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   @Column(name = "dFechaFin", nullable = false)
   private Date fechaFin;

   @PrePersist
   private void prePersist(){
      this.completo = true;
      this.fechaFin = new Date();
   }
   
   @PreUpdate
   private void preUpdate(){
      this.fechaFin = new Date();
   }

   /*
   *
   */
   private static final long serialVersionUID = 1L;
   
}
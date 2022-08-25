package com.microservicio.rimextraccion.models.entities;

import java.util.Date;
import javax.persistence.CascadeType;
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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RimCtrlCalCamposAnalisis")
@Data
@Builder(builderClassName = "CtrlCalCamposAnalisisBuilder", builderMethodName = "of", buildMethodName = "get")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "idCtrlCal" })
public class CtrlCalCamposAnalisis {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdCtrlCal")
   private Long idCtrlCal;
   
   @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   @JoinColumn(name = "nIdAsigGrupo")
   private AsigGrupoCamposAnalisis asigGrupoCamposAnalisis;
   
   @Column(name = "sIdsCtrlCalCsv", columnDefinition = "VARCHAR(MAX) NULL")
   private String idsCtrlCalCsv;
   
   @Column(name = "bCompleto", nullable = false)
   private boolean completo;
   
   @Column(name = "nTotalRevisados", columnDefinition = "INT NOT NULL DEFAULT 0")
   private Integer totalRevisados;
   
   @Column(name = "nTotalRegistros", columnDefinition = "BIGINT NOT NULL DEFAULT 0")
   private Long totalRegistros;
   
   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", locale = "America/Lima")
   @Column(name = "dFechaRegistro", nullable = false)
   private Date fechaRegistro;

   @PrePersist
   private void prePersist(){
      this.completo = false;
      this.totalRevisados = 0;
      this.fechaRegistro = new Date();
   }
   
}
package com.commons.utils.models.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "RimRNControlCambios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = { "idRNControlCambio" })
public class RNControlCambios {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdRNControlCambio")
   private Long idRNControlCambio;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "sIdRN", nullable = false)
   @JsonIgnoreProperties(value = { "proceso" })
   private ReglaNegocio reglaNegocio;
   
   @OneToMany(mappedBy = "controlCambio", fetch = FetchType.LAZY, orphanRemoval = true)
   @JsonIgnoreProperties(value = { "controlCambio" })
   private List<RNRegistroEjecucionScript> registrosEjecucionScript;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "uIdUsuarioCreador", nullable = false)
   @JsonIgnoreProperties(value = { "usrProcedimiento" })
   private Usuario usuarioCreador;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdTipoScript", nullable = false)
   private RNTipoScript tipoScript;

   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "America/Lima")
   @Column(name = "dFechaCreacion", nullable = false)
   private Date fechaCreacion;

   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "America/Lima")
   @Column(name = "dFechaModificacion", nullable = false)
   private Date fechaModificacion;
   
   @Column(name = "sScript", nullable = false, columnDefinition = "VARCHAR(5000)")
   private String script;

   @Column(name = "jResultSet", columnDefinition = "TEXT NULL")
   private String resultSet;
   
   @Column(name = "bActivo", nullable = false)
   private boolean activo;
   
   @Column(name = "sObservaciones", nullable = false, columnDefinition = "VARCHAR(500)")
   private String observaciones;

   @PrePersist
   private void prePersist() {
      this.fechaCreacion = new Date();
      this.fechaModificacion = new Date();
   }

   @PreUpdate
   private void preUpdate() {
      this.fechaModificacion = new Date();
   }
   
}
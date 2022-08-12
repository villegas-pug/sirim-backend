package com.microservicio.rimextraccion.models.entities;

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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.commons.utils.models.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "RimTablaDinamica")
@Data
@Builder(builderClassName = "TablaDinamicaBuilder", builderMethodName = "of", buildMethodName = "get")
@AllArgsConstructor
@EqualsAndHashCode(of = { "idTabla" })
public class TablaDinamica implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdTabla")
   private Long idTabla;
   
   @Column(name = "sNombre", length = 55, nullable = false)
   private String nombre;
   
   @Column(name = "sMetaFieldsCsv", columnDefinition = "VARCHAR(MAX) NULL")
   private String metaFieldsCsv;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "uIdUsrCreador", nullable = false)
   private Usuario usrCreador;

   @OneToMany(mappedBy = "tablaDinamica", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
   @JsonIgnoreProperties(value = { "tablaDinamica" }, allowSetters = true)
   private List<GrupoCamposAnalisis> lstGrupoCamposAnalisis;
   
   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "dFechaCreacion")
   private Date fechaCreacion;
   
   @Column(name = "bActivo", nullable = false)
   private boolean activo;

   public TablaDinamica() {
      this.lstGrupoCamposAnalisis = new ArrayList<GrupoCamposAnalisis>();
   }

   @PrePersist
   private void prePersist(){
      this.fechaCreacion = new Date();
      this.activo = true;
   }

   public void addGrupoCamposAnalisis(GrupoCamposAnalisis grupoCamposAnalisis){
      grupoCamposAnalisis.setTablaDinamica(this);
      this.lstGrupoCamposAnalisis.add(grupoCamposAnalisis);
   }

   /*
   *
   */
   private static final long serialVersionUID = 1L;
   
}
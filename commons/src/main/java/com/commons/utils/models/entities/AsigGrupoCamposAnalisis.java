package com.commons.utils.models.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "RimAsigGrupoCamposAnalisis")
@Data
@Builder(builderClassName = "AsigGrupoCamposAnalisisBuilder", builderMethodName = "of", buildMethodName = "get")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "idAsigGrupo" })
public class AsigGrupoCamposAnalisis implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdAsigGrupo")
   private Long idAsigGrupo;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdGrupo", nullable = false)
   private GrupoCamposAnalisis grupo;

   @OneToMany(mappedBy = "asigGrupo", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
   @JsonIgnoreProperties(value = { "asigGrupo" })
   private @Builder.Default List<ProduccionAnalisis> produccionAnalisis = new ArrayList<>();
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "uIdUsrAnalista", nullable = false)
   @JsonIgnoreProperties(value = { "usrProcedimiento", "foto" })
   private Usuario usrAnalista;
   
   @Builder.Default
   @OneToMany(mappedBy = "asigGrupoCamposAnalisis", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
   @JsonIgnoreProperties(value = { "asigGrupoCamposAnalisis" })
   private List<CtrlCalCamposAnalisis> ctrlsCalCamposAnalisis = new ArrayList<>();

   @Column(name = "nRegAnalisisIni", nullable = false)
   private int regAnalisisIni;
   
   @Column(name = "nRegAnalisisFin", nullable = false)
   private int regAnalisisFin;
   
   @Column(name = "bCtrlCalConforme", nullable = true)
   private boolean ctrlCalConforme;

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "dFechaAsignacion", length = 55, nullable = false)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "America/Lima")
   private Date fechaAsignacion;

   @PrePersist
   private void prePersist(){
      this.ctrlCalConforme = false;
   }

   public void addProduccionAnalisis(ProduccionAnalisis prod){
      prod.setAsigGrupo(this);
      this.produccionAnalisis.add(prod);
   }

   public void addCtrlsCalCamposAnalisis(CtrlCalCamposAnalisis ctrlCalCA){
      ctrlCalCA.setAsigGrupoCamposAnalisis(this);
      this.ctrlsCalCamposAnalisis.add(ctrlCalCA);
   }

   /*
   *
   */
   private static final long serialVersionUID = 1L;
   
}
package com.microservicio.rimextraccion.models.entities;

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

import com.commons.utils.models.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
   private Usuario usrAnalista;
   
   @Column(name = "nRegAnalisisIni", nullable = false)
   private int regAnalisisIni;
   
   @Column(name = "nRegAnalisisFin", nullable = false)
   private int regAnalisisFin;
   
   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "dFechaAsignacion", length = 55, nullable = false)
   private Date fechaAsignacion;

   @PrePersist
   private void prePersist(){
      this.fechaAsignacion = new Date();
   }

   /*
   *
   */
   private static final long serialVersionUID = 1L;

}
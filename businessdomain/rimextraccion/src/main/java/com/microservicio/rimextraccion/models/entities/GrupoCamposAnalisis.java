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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RimGrupoCamposAnalisis")
@Builder(builderClassName = "GrupoCamposAnalisisBuilder", builderMethodName = "of", buildMethodName = "get")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "idGrupo" })
public class GrupoCamposAnalisis implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdGrupo")
   private Long idGrupo;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdTabla", nullable = false)
   private TablaDinamica tablaDinamica;

   @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
   @JsonIgnoreProperties(value = { "grupo" })
   private @Builder.Default List<AsigGrupoCamposAnalisis> asigGrupoCamposAnalisis = new ArrayList<>();

   @Column(name = "sNombre", length = 100, nullable = false)
   private String nombre;

   @Column(name = "sMetaFieldsCsv", columnDefinition = "VARCHAR(MAX) NULL")
   private String metaFieldsCsv;

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "dFechaCreacion", nullable = false)
   private Date fechaCreacion;

   @Column(name = "bActivo", nullable = false)
   private boolean activo;

   @PrePersist
   private void prePersist(){
      this.fechaCreacion = new Date();
      this.activo = true;
   }

   /*
   *
   */
   private static final long serialVersionUID = 1L;

}
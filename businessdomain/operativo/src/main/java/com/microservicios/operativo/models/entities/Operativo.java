package com.microservicios.operativo.models.entities;

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
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.commons.utils.models.entities.Dependencia;
import com.commons.utils.models.entities.Empresa;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Table(name = "SidOperativo")
@Data
@EqualsAndHashCode(of = { "idOperativo" })
@NamedNativeQuery(
   name = "query_ope_find_all",
   query = "SELECT * FROM SidOperativo",
   resultClass = Operativo.class
)
public class Operativo implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdOperativo")
   private Long idOperativo;

   @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "operativo", orphanRemoval = true)
   @JsonIgnoreProperties(value = { "operativo" }, allowSetters = true)
   private List<DetalleOperativo> detOperativo;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdDependencia", nullable = true)
   private Dependencia dependencia;

   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   @Column(name = "dFechaOperativo", nullable = false)
   private Date fechaOperativo;

   @Column(name = "sNumeroOperativo", length = 55, nullable = false)
   private String numeroOperativo;

   @Column(name = "sModalidadOperativo", length = 55, nullable = false)
   private String modalidadOperativo;

   @Column(name = "sNumeroInforme", length = 55)
   private String numeroInforme;

   @JoinColumn(name = "nIdEmpresa", nullable = false)
   @ManyToOne(fetch = FetchType.EAGER)
   private Empresa entidadSolicitaOperativo;

   @Column(name = "sEstablecimientoVisitado", length = 500)
   private String establecimientoVisitado;

   @Column(name = "dFechaRegistro", nullable = false)
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechaRegistro;

   @PrePersist
   private void prePersist() {
      this.fechaRegistro = new Date();
   }

   public Operativo() {
      this.detOperativo = new ArrayList<>();
   }

   public void setDetOperativo(List<DetalleOperativo> detOperativos) {
      detOperativos.forEach(this::addDetOperativo);
   }

   public void addDetOperativo(DetalleOperativo detOperativo) {
      detOperativo.setOperativo(this);
      this.detOperativo.add(detOperativo);
   }

   /**
   *
   */
   private static final long serialVersionUID = 1L;

}
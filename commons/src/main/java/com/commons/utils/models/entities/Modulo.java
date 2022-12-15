package com.commons.utils.models.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "RimModulo")
@Data
@Builder(builderClassName = "ModuloBuilder", builderMethodName = "of", buildMethodName = "get")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "idMod" })
public class Modulo implements Serializable {
   
   @Id
   @Column(name = "nIdMod")
   private Long idMod;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdBD", nullable = false)
   private BaseDatos baseDatos;

   @OneToMany(mappedBy = "modulo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
   @JsonIgnoreProperties(value = { "modulo" })
   private List<Tabla> tablas;

   @OneToMany(mappedBy = "modulo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
   @JsonIgnoreProperties(value = { "modulo" })
   private List<QueryString> queryStrings;
   
   @Column(name = "sNombre", length = 55, nullable = false)
   private String nombre;
   
   @Column(name = "bActivo", nullable = false)
   private boolean activo;
   
   @Temporal(TemporalType.TIME)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   @Column(name = "dFechaRegistro", length = 55, nullable = false)
   private Date fechaRegistro;

   @PrePersist
   private void prePersist(){
      this.activo = true;
      this.fechaRegistro = new Date();
   }

   /*
   *
   */
   private static final long serialVersionUID = 1L;
}

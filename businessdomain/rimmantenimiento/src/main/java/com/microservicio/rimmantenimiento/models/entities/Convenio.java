package com.microservicio.rimmantenimiento.models.entities;

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
@Table(name = "RimConvenio")
@Data
@Builder(builderClassName = "ConvenioBuilder", builderMethodName = "of", buildMethodName = "get")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "idConvenio" })
public class Convenio implements Serializable {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdConvenio")
   private Long idConvenio;
   
   @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "convenio", orphanRemoval = true)
   @JsonIgnoreProperties(value = "convenio", allowSetters = true)
   private @Builder.Default List<DetConvenio> detConvenio = new ArrayList<>();

   @Column(name = "sNombre", length = 255, nullable = false)
   private String nombre;
   
   @Column(name = "bCompleto", nullable = false)
   private boolean completo;

   @Column(name = "bActivo", nullable = false)
   private boolean activo;
   
   @Column(name = "dFechaCreacion", length = 255, nullable = false)
   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   private Date fechaCreacion;

   public void addDetConvenio(DetConvenio detConvenio){
      detConvenio.setConvenio(this);
      this.detConvenio.add(detConvenio);
   }

   public void removeDetConvenio(DetConvenio detConvenio){
      this.detConvenio.remove(detConvenio);
   }

   @PrePersist
   private void prePersist(){
      this.completo = false;
      this.activo = true;
      this.fechaCreacion = new Date();
   }

   /*
   * 
   */
   private static final long serialVersionUID = 1L;

}

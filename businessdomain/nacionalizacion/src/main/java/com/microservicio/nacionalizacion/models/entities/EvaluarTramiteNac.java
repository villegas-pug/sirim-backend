package com.microservicio.nacionalizacion.models.entities;

import java.io.Serializable;
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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "SidEvaluarTramiteNac")
@Data
@EqualsAndHashCode(of = { "idVerifExp" })
@Builder(builderClassName = "EvaluarTramiteNacBuilder", builderMethodName = "of", buildMethodName = "get")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class EvaluarTramiteNac implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdVerifExp")
   private Long idVerifExp;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdTramiteNac")
   private NuevoTramiteNac tramiteNac;

   @OneToMany(mappedBy = "evaluarTramiteNac", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
   @JsonIgnoreProperties(value = { "evaluarTramiteNac" }, allowSetters = true)
   private List<EvalRequisitoTramiteNac> evalRequisitoTramiteNac;
   
   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "America/Lima")
   @Column(name = "dFechaRegistro", nullable = false)
   private Date fechaRegistro;
   
   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "America/Lima")
   @Column(name = "dFechaDerivacion", nullable = false)
   private Date fechaDerivacion;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "uIdOperadorDesig", nullable = false)
   private Usuario operadorDesig;
   
   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "America/Lima")
   @Column(name = "fechaHoraFin")
   private Date fechaHoraFin;
   
   @Column(name = "bLeido", nullable = false)
   private boolean leido;
   
   @Column(name = "bActivo", nullable = false)
   private boolean activo;
   
   @Column(name = "bCompletado", nullable = false)
   private boolean completado;

   @PrePersist
   private void prePersist(){
      this.activo = true;
      this.leido = false;
      this.completado = false;
      this.fechaRegistro = new Date();
      this.fechaDerivacion = new Date();
   }

   /*
   *
   */
   private static final long serialVersionUID = 1L;
   
}
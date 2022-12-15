package com.microservicio.rimmantenimiento.models.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RimDetConvenio")
@Data
@Builder(builderClassName = "DetConvenioBuilder", builderMethodName = "of", buildMethodName = "get")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "idDetConvenio" })
public class DetConvenio implements Serializable {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdDetConvenio")
   private Long idDetConvenio;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdConvenio", nullable = false)
   private Convenio convenio;

   @Column(name = "sDescripcion", length = 255, nullable = false)
   private String descripcion;
   
   @Column(name = "bActivo", nullable = false)
   private boolean activo;

   @Column(name = "dFechaDocumento")
   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   private Date fechaDocumento;

   @Column(name = "sNombreAnexo", length = 100)
   private String nombreAnexo;
   
   @Lob
   @JsonIgnore
   @Column(name = "xAnexo")
   private byte[] anexo;

   @Column(name = "dFechaRegistro", nullable = false)
   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
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

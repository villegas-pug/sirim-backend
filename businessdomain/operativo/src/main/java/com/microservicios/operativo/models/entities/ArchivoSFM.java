package com.microservicios.operativo.models.entities;

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
@Table(name = "SidArchivoSFM")
@Data
@Builder(builderClassName = "ArchivoSFMBuilder", builderMethodName = "of", buildMethodName = "get")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "idArchivoDiligencia" })
public class ArchivoSFM implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdArchivoDiligencia")
   private Long idArchivoDiligencia;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdDiligencia")
   private DiligenciaSFM diligencia;
   
   @Column(name = "sNombre", length = 100, nullable = false)
   private String nombre;

   @JsonIgnore
   @Lob
   @Column(name = "xArchivo", nullable = true, columnDefinition = "VARBINARY(MAX)")
   private byte[] archivo;
   
   @Column(name = "sTipoArchivo", length = 15, nullable = false)
   private String tipoArchivo;
   
   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", locale = "America/Lima")
   @Column(name = "dFechaRegistro")
   private Date fechaRegistro;
   
   @Column(name = "bActivo", nullable = false)
   private boolean activo;

   @PrePersist
   private void prePersist(){
      this.fechaRegistro = new Date();
      this.activo = true;
   }
 
   /*
   * 
   */
   private static final long serialVersionUID = 1L;
}
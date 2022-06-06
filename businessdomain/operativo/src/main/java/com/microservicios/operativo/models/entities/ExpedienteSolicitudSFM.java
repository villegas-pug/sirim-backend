package com.microservicios.operativo.models.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SidExpedienteSolicitudSFM")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "SidExpedienteSolicitudSFMBuilder", builderMethodName = "of", buildMethodName = "get")
@EqualsAndHashCode(of = { "idExpedienteSolicitud" })
public class ExpedienteSolicitudSFM implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long idExpedienteSolicitud;
   
   @Column(name = "sNumeroExpediente", length = 55, nullable = false)
   private String numeroExpediente;
   
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   @Temporal(TemporalType.DATE)
   @Column(name = "dFechaAprobacion", nullable = false)
   private Date fechaAprobacion;
   
   @Column(name = "sProcedimiento", length = 100, nullable = false)
   private String procedimiento;
   
   @Column(name = "sAdministrado", length = 150, nullable = false)
   private String administrado;
   
   @Column(name = "sNacionalidad", length = 40, nullable = false)
   private String nacionalidad;
   
   @Column(name = "sDomicilio", length = 200, nullable = false)
   private String domicilio;
   
   @Column(name = "sDistrito", length = 100, nullable = false)
   private String distrito;

   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   @Temporal(TemporalType.DATE)
   @Column(name = "dFechaRegistro")
   private Date fechaRegistro;

   @Column(name = "sFileName", length = 55, nullable = false)
   private String fileName;
 
   @PrePersist
   private void prePersist(){
      this.fechaRegistro = new Date();
   }

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
}
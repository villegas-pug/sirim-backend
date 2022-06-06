package com.microservicio.nacionalizacion.models.entities;

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
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SidMailFile")
@Data
@EqualsAndHashCode(of = { "idMailFile" })
public class MailFile {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdMailFile")
   private Long idMailFile;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdDetExpMininter", nullable = false)
   private DetExpedienteMininter detExpMininter;
   
   @Column(name = "sDescripcion", length = 120, nullable = false)
   private String descripcion;
   
   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   @Column(name = "dFechaRegistro")
   private Date fechaRegistro;
   
   @Lob
   @JsonIgnore
   @Column(name = "xFile", nullable = false)
   private byte[] file;
   
   @Column(name = "bActivo")
   private boolean activo;

   @PrePersist
   private void prePersist(){
      this.fechaRegistro = new Date();
      this.activo = true;
   }

}

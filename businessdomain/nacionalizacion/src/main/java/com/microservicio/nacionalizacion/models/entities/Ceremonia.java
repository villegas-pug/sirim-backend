package com.microservicio.nacionalizacion.models.entities;

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
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SidCeremonia")
@Data
@EqualsAndHashCode(of = { "idCereminia" })
public class Ceremonia implements Serializable {

   private static final String SEÑOR = "SEÑOR";
   private static final String SEÑORA = "SEÑORA";
   private static final String SEÑORITA = "SEÑORITA";
   private static final String MASCULINO = "M";
   private static final String FEMENINO = "F";

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdCereminia")
   private Long idCereminia;

   @Column(name = "sLugarNacimiento", length = 55)
   private String lugarNacimiento;

   @Column(name = "sNacionalidad", length = 55)
   private String nacionalidad;

   @Column(name = "sReligion", length = 55)
   private String religion;

   @Column(name = "sCargo", length = 15)
   private String cargo;

   @Column(name = "sNombres", length = 100)
   private String nombres;

   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   @Column(name = "dFechaNacimiento")
   private Date fechaNacimiento;

   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   @Column(name = "dFechaCeremonia")
   private Date fechaCeremonia;

   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "America/Lima")
   @Column(name = "dFechaRegistro")
   private Date fechaRegistro;

   @Column(name = "sEdad", length = 3)
   private String edad;

   @Column(name = "sGenero", length = 1)
   private String genero;

   @Column(name = "sMotivo", length = 100)
   private String motivo;

   @Column(name = "sEmail", length = 100)
   private String email;

   @Column(name = "sTelefono", length = 25)
   private String telefono;

   @Column(name = "sNumeroExpediente", length = 15)
   private String numeroExpediente;

   @Column(name = "sAñosResidenciaPeru", length = 55)
   private String añosResidenciaPeru;

   @Column(name = "sProfesion", length = 55)
   private String profesion;

   @Column(name = "sConyuge", length = 100)
   private String conyuge;

   @Column(name = "sDni", length = 15)
   private String dni;

   @Column(name = "sNumeroHijosPeruano", length = 2)
   private String numeroHijosPeruano;

   @Column(name = "sEmpresa", length = 100)
   private String empresa;

   @PrePersist
   private void prePersist(){
      if(this.getCargo().toUpperCase().trim().equals(SEÑOR)) this.setGenero(MASCULINO);
      else if (this.getCargo().toUpperCase().trim().equals(SEÑORA)) this.setGenero(FEMENINO);
      else if (this.getCargo().toUpperCase().trim().equals(SEÑORITA)) this.setGenero(FEMENINO);

      this.fechaRegistro = new Date();
   }
 
}
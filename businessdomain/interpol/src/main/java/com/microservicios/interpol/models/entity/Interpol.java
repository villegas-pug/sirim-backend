package com.microservicios.interpol.models.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
@Entity
@Table(name = "SidInterpol")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = { "idInterpol" })
public class Interpol implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdInterpol")
   private Long idInterpol;
   
   @NonNull
   @Column(name = "sNombres", length = 55)
   private String nombres;
   
   @NonNull
   @Column(name = "sApellidos", length = 55)
   private String apellidos;

   @NonNull
   @Column(name = "sSexo", length = 10)
   private String sexo;

   @NonNull
   @Column(name = "sCedula", length = 55)
   private String cedula;

   @NonNull
   @Column(name = "sPasaporte", length = 50)
   private String pasaporte;

   @NonNull
   @Column(name = "sNacionalidad", length = 55)
   private String nacionalidad;

   @NonNull
   @Column(name = "dFechaEmision", length = 15)
   private String fechaEmision;

   @NonNull
   @Column(name = "dFechaLugarNacimiento", length = 100)
   private String fechaLugarNacimiento;

   @NonNull
   @Column(name = "sMotivo", length = 100)
   private String motivo;

   @NonNull
   @Column(name = "sProcedencia", length = 50)
   private String procedencia;

   @NonNull
   @Column(name = "sSede", length = 55)
   private String sede;

   @Column(name = "sArchivo", length = 55)
   private String archivo;

   @Lob
   @JsonIgnore
   @Column(name = "xScreenshot")
   private byte[] screenShot;

   @Transient
   private boolean isScreenshot;

   public boolean getIsScreenshot() {
      return this.screenShot != null ? true : false;
   }

   /**
   *
   */
   private static final long serialVersionUID = 1L;

}

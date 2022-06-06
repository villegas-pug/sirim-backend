package com.microservicios.operativo.models.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SidPreInscripcion")
@Data
@EqualsAndHashCode(of = { "idCitaVerifica" })
public class PreInscripcion implements Serializable {

   @Id
   @Column(name = "nIdCitaVerifica")
   private Long idCitaVerifica;

   @Column(name = "sTipoTramite", length = 150)
   private String tipoTramite;

   @Temporal(TemporalType.DATE)
   @Column(name = "dFechaInscripcion")
   private Date fechaInscripcion;

   @Column(name = "sDocBeneficiario", length = 100)
   private String docBeneficiario;

   @Column(name = "sNumDocBeneficiario", length = 100)
   private String numDocBeneficiario;

   @Column(name = "sNomBeneficiario", length = 100)
   private String nomBeneficiario;

   @Column(name = "sPriApeBeneficiario", length = 100)
   private String priApeBeneficiario;

   @Column(name = "sSegApeBeneficiario", length = 100)
   private String segApeBeneficiario;

   @Temporal(TemporalType.DATE)
   @Column(name = "dFecNacBeneficiario")
   private Date fecNacBeneficiario;

   @Column(name = "sSexo", length = 25)
   private String sexo;

   @Column(name = "sPaisNacimiento", length = 100)
   private String paisNacimiento;

   @Temporal(TemporalType.DATE)
   @Column(name = "dFechaIngreso")
   private Date fechaIngreso;

   @Column(name = "sIdDocRepLegal", length = 15)
   private String idDocRepLegal;

   @Column(name = "sNumDocRepLegal", length = 100)
   private String numDocRepLegal;

   @Column(name = "sNomRepLegal", length = 100)
   private String nomRepLegal;

   @Column(name = "sPriApeRepLegal", length = 100)
   private String priApeRepLegal;

   @Column(name = "sSegApeRepLegal", length = 100)
   private String segApeRepLegal;

   @Column(name = "sSexoRepLegal", length = 25)
   private String sexoRepLegal;

   @Column(name = "sNacionalidadRepLegal", length = 100)
   private String nacionalidadRepLegal;

   @Column(name = "sUbigeoBeneficiario", length = 100)
   private String ubigeoBeneficiario;

   @Column(name = "sDireccionBeneficiario", length = 500)
   private String direccionBeneficiario;

   @Column(name = "bEmbarazada", length = 15)
   private String embarazada;

   @Column(name = "bDiscapacitado", length = 15)
   private String discapacitado;

   @Column(name = "bEnfermedad", length = 15)
   private String enfermedad;

   @Column(name = "sDescripcionEnfermedad", length = 300)
   private String descripcionEnfermedad;
  
   /**
   *
   */
   private static final long serialVersionUID = 1L;
}
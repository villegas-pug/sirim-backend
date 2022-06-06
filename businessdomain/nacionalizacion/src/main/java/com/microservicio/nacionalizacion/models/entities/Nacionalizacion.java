package com.microservicio.nacionalizacion.models.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SidNacionalizacion")
@Data
@EqualsAndHashCode(of = { "idNacionalizacion" })
public class Nacionalizacion implements Serializable {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdNacionalizacion", length = 55)
   private Long idNacionalizacion;
   
   @Column(name = "sNumeroTramite", length = 55)
   private String numeroTramite;

   @Column(name = "dFechaTramite")
   @Temporal(TemporalType.DATE)
   private Date fechaTramite;
   
   @Column(name = "nAñoTramite", columnDefinition = "SMALLINT")
   private int añoTramite;
   
   @Column(name = "sTipoTramite", length = 200)
   private String tipoTramite;
   
   @Column(name = "sEstadoTramite", length = 55)
   private String estadoTramite;
   
   @Column(name = "sEtapaTramite", length = 200)
   private String etapaTramite;
   
   @Column(name = "dFechaAud")
   @Temporal(TemporalType.DATE)
   private Date fechaAud;
   
   @Column(name = "sDependencia", length = 25)
   private String dependencia;
   
   @Column(name = "sAdministrado", length = 150)
   private String administrado;
   
   @Column(name = "sSexo", length = 15)
   private String sexo;
   
   @Column(name = "sPaisNacimiento", length = 25)
   private String paisNacimiento;
}
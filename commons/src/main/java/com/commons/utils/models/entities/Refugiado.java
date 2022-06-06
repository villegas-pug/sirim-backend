package com.commons.utils.models.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "SidRefugiado", indexes = { 
   @Index(name = "ix_SidRefugiado", columnList = "sNombres, sPaterno, sMaterno") 
})
@Data
@ToString(exclude = { "idRefugiado", "condicion", "paisNacimiento", "fechaNacimiento" })
public class Refugiado implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdRefugiado")
   private Long idRefugiado;
   
   @Column(name = "sCondicion", length = 55)
   private String condicion;
   
   @Column(name = "sNombres", length = 250)
   private String nombres;
   
   @Column(name = "sPaterno", length = 250)
   private String paterno;
   
   @Column(name = "sMaterno", length = 250)
   private String materno;
   
   @Column(name = "sPaisNacimiento", length = 55)
   private String paisNacimiento;
   
   @Column(name = "sFechaNacimiento", length = 55)
   private String fechaNacimiento;

   /**
    * 
   */
   private static final long serialVersionUID = 1L;
}
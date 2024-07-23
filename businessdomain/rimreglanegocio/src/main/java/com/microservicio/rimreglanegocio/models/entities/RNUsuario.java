package com.microservicio.rimreglanegocio.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
 
 
@Entity
@Table(name = "RimRNUsuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = { "idOperador" })
public class RNUsuario {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdOperador")
   private int idOperador;

   @Column(name = "sLogin", nullable = false, unique = true)
   private String login;

   @Column(name = "xPassword", nullable = false)
   private String password;

   @Column(name = "sNombre", nullable = false)
   private String nombre;

   @Column(name = "sDni", nullable = false)
   private String dni;

   @Column(name = "bActivo", nullable = false)
   private boolean activo;

}

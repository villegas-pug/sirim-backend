package com.microservicio.rrhh.models.entities;

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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RrhhControlAsistencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "ControlAsistenciaBuilder", builderMethodName = "of", buildMethodName = "get")
@EqualsAndHashCode(of = { "idControl" })
public class ControlAsistencia implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdControl")
   private Long idControl;
   
   @Column(name = "nUsuarioNro", nullable = false)
   private Long usuarioNro;
   
   @Column(name = "nIdUsuario", nullable = false)
   private Long idUsuario;
   
   @Column(name = "sNombre", length = 120, nullable = false)
   private String nombre;
   
   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "America/Lima")
   @Column(name = "dFechaHoraIngreso", nullable = false)
   private Date fechaHoraIngreso;


   /*
    * 
   */
   private static final long serialVersionUID = 1L;
   
}
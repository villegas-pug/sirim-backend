package com.microservicio.rimextraccion.models.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "RimQueryString")
@Data
@Builder(builderClassName = "QueryStringBuilder", builderMethodName = "of", buildMethodName = "get")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "idQStr" })
public class QueryString implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdQStr")
   private Long idQStr;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "idMod", nullable = false)
   private Modulo modulo;

   @Column(name = "sNombre", length = 100, nullable = false)
   private String nombre;

   @Column(name = "sQueryString", columnDefinition = "VARCHAR(MAX) NOT NULL")
   private String queryString;
   
   @Column(name = "bActivo", nullable = false)
   private boolean activo;
   
   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", locale = "America/Lima")
   @Column(name = "dFechaRegistro", nullable = false)
   private Date fechaRegistro;

   @PrePersist
   private void prePersist(){
      this.activo = true;
      this.fechaRegistro = new Date();
   }

   /* 
   *
   */
   private static final long serialVersionUID = 1L;
}

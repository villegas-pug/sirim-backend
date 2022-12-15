package com.commons.utils.models.entities;

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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RimEvento")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "EventoBuilder", builderMethodName = "of", buildMethodName = "get")
@EqualsAndHashCode(of = { "event_id" })
public class Evento implements Serializable {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdEvent")
   private Long event_id;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "uIdUsuario", nullable = false)
   @JsonIgnoreProperties(value = { "usrProcedimiento" })
   private Usuario usuario;
   
   @Column(name = "sTitle", length = 255, nullable = false)
   private String title;
   
   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "America/Lima")
   @Column(name = "dStart", nullable = false)
   private Date start;
   
   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "America/Lima")
   @Column(name = "dEnd", nullable = false)
   private Date end;
   
   @Column(name = "bActivo", nullable = false)
   private boolean activo;
   
   @Temporal(TemporalType.DATE)
   @Column(name = "dFechaRegistro", nullable = false)
   private Date fechaRegistro;

   @PrePersist
   private void prePersist(){
      this.fechaRegistro = new Date();
      this.activo = true;
   }

   /*
    * 
    */
   private static final long serialVersionUID = 1L;
}

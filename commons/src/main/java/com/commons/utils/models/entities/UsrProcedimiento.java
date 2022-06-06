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
import javax.persistence.UniqueConstraint;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(
   name = "SidUsuarioProcedimiento",
   uniqueConstraints = @UniqueConstraint(columnNames = { "uIdUsuario", "nIdProcedimiento" })
)
@Data
@EqualsAndHashCode(of = { "idUsrProcedimiento" })
public class UsrProcedimiento implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdUsrProcedimiento")
   private Long idUsrProcedimiento;

   @ManyToOne(fetch = FetchType.EAGER)
   @JsonIgnoreProperties(value = { "usrProcedimiento" })
   @JoinColumn(name = "uIdUsuario")
   private Usuario usuario;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdProcedimiento")
   private Procedimiento procedimiento;

   @Column(name = "bDenegado")
   private boolean denegado;

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "dFechaRegistro", nullable = false)
   private Date fechaRegistro;

   @PrePersist
   private void prePersist(){
      this.fechaRegistro = new Date();
      this.denegado = false;
   }

   /**
   *
   */
   private static final long serialVersionUID = 1L;
}
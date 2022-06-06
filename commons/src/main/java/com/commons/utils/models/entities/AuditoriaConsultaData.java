package com.commons.utils.models.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import com.commons.utils.models.enums.TipoConsulta;
import lombok.Data;

@Entity
@Table(name = "SidAuditoriaConsultaData")
@Data
public class AuditoriaConsultaData implements Serializable {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdAuditoriaConsultaData")
   private Long idAuditoriaConsultaData;
   
   @JoinColumn(name = "uIdUsuario")
   @ManyToOne(fetch = FetchType.EAGER)
   private Usuario usuario;
   
   @Enumerated(EnumType.STRING)
   @Column(name = "sTipoConsulta", length = 15)
   private TipoConsulta tipoConsulta;
   
   @Column(name = "dFechaConsulta")
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechaConsulta;
   
   @JoinColumn(name = "nIdProcedimiento")
   @ManyToOne(fetch = FetchType.EAGER)
   private Procedimiento procedimiento;
   
   @Column(name = "sPayload", length = 500)
   private String payload;

   @PrePersist
   private void prePersist(){
      this.fechaConsulta = new Date();
   }

   /**
    * 
   */
   private static final long serialVersionUID = 1L;
}

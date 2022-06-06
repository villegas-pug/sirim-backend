package com.commons.utils.models.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SidEtapa")
@Data
@Builder(builderClassName = "EtapaBuilder", builderMethodName = "of", buildMethodName = "get")
@NoArgsConstructor
@EqualsAndHashCode(of = { "idEtapa" })
public class Etapa implements Serializable {

   @Id
   @Column(name = "nIdEtapa")
   private Long idEtapa;
   
   @Column(name = "sDescripcion", length = 100)
   private String descripcion;

   @Column(name = "bActivo")
   private boolean activo;
   
   @Temporal(TemporalType.DATE)
   @Column(name = "dFechaHoraAud")
   private Date fechaHoraAud;

   @PrePersist
   private void prePersist(){
      this.activo = true;
      this.fechaHoraAud = new Date();
   }

   public Etapa(Long idEtapa, String descripcion, boolean activo, Date fechaHoraAud) {
      this.idEtapa = idEtapa;
      this.descripcion = descripcion;
      this.activo = activo;
      this.fechaHoraAud = fechaHoraAud;
   }

   /*
   *
   */
   private static final long serialVersionUID = 1L;
   
}
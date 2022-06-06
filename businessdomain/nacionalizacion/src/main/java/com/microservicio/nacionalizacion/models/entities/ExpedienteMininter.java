package com.microservicio.nacionalizacion.models.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.commons.utils.models.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SidExpedienteMininter")
@Data
@EqualsAndHashCode(of = { "idExpMininter" })
public class ExpedienteMininter implements Serializable {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdExpMininter")
   private Long idExpMininter;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "uIdUsuarioDigita", nullable = true)
   private Usuario usuarioDigita;

   @Column(name = "sNumeroExpediente", length = 25)
   private String numeroExpediente;
   
   @Column(name = "sNombres", length = 100)
   private String nombres;
   
   @Column(name = "sDependencia", length = 55)
   private String dependencia;
   
   @Column(name = "sTipoProcedimiento", length = 100)
   private String tipoProcedimiento;
   
   @Temporal(TemporalType.DATE)
   @Column(name = "dFechaRegistro")
   private Date fechaRegistro;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nIdUbicacion", nullable = true)
   private UbicacionExpediente ubicacion = new UbicacionExpediente(6L);

   @OneToMany(mappedBy = "expedienteMininter", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
   @JsonIgnoreProperties(value = { "expedienteMininter" })
   @OrderBy("fechaRecepcion DESC")
   private List<DetExpedienteMininter> detExpedienteMininter;

   @PrePersist
   private void prePersist(){
      this.fechaRegistro = new Date();
   }

   public ExpedienteMininter() {
      this.detExpedienteMininter = new ArrayList<>();
   }

   public void addDetExpedienteMininter(DetExpedienteMininter detExpedienteMininter){
      detExpedienteMininter.setExpedienteMininter(this);
      this.detExpedienteMininter.add(detExpedienteMininter);
   }

   public void replaceDetExpedienteMiniter(DetExpedienteMininter detExpMininter){
      this.detExpedienteMininter
                           .stream()
                           .filter(record -> record.equals(detExpMininter))
                           .forEach(record -> {
                                 record.setNumeroOficio(detExpMininter.getNumeroOficio());
                                 record.setFechaOficio(detExpMininter.getFechaOficio());
                                 record.setFechaRecepcion(detExpMininter.getFechaRecepcion());
                                 record.setFechaRespuesta(detExpMininter.getFechaRespuesta());
                                 record.setEstado(detExpMininter.getEstado());
                                 record.setUbicacion(detExpMininter.getUbicacion());
                                 record.setAccionesRealizadas(detExpMininter.getAccionesRealizadas());
                           });
   }

   public void removeDetExpedienteMininter(DetExpedienteMininter detExpedienteMininter){
      detExpedienteMininter.setExpedienteMininter(null);
      this.detExpedienteMininter.remove(detExpedienteMininter);
   }

}

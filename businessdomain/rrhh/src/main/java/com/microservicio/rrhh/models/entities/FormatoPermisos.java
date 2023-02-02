package com.microservicio.rrhh.models.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.commons.utils.models.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RrhhFormatoPermisos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "FormatoPermisosBuilder", builderMethodName = "of", buildMethodName = "get")
@EqualsAndHashCode(of = { "idFormato" })
public class FormatoPermisos implements Serializable {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdFormato")
   private Long idFormato;
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "uIdUsrCreador", nullable = false)
   @JsonIgnoreProperties(value = { "usrProcedimiento", "foto" })
   private Usuario usrCreador;
   
   @Column(name = "sJornadaLaboral", length = 55, nullable = false)
   private String jornadaLaboral;
   
   @Column(name = "sRegimenLaboral", length = 55, nullable = false)
   private String regimenLaboral;
   
   @Column(name = "sNombres", length = 255, nullable = false)
   private String nombres;
   
   @Column(name = "sGerencia", length = 55)
   private String gerencia;
   
   @Column(name = "sSubgerencia", length = 55)
   private String subgerencia;
   
   @Column(name = "sUnidad", length = 55)
   private String unidad;
   
   @Column(name = "sTipoLicencia", length = 255, nullable = false)
   private String tipoLicencia;
   
   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "America/Lima")
   @Column(name = "dDesde", nullable = false)
   private Date desde;
   
   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "America/Lima")
   @Column(name = "dHasta", nullable = false)
   private Date hasta;
   
   @Column(name = "sTotalHoras", length = 55)
   private String totalHoras;
   
   @Column(name = "sJustificacion", length = 255)
   private String justificacion;
   
   @Temporal(TemporalType.DATE)
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
   @Column(name = "dFechaFormato")
   private Date fechaFormato;
   
   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "dFechaCreacion")
   @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "America/Lima")
   private Date fechaCreacion;
   
   @Column(name = "bAtendido")
   private boolean atendido;

   @Column(name = "bRecibido")
   private boolean recibido;
   
   @Column(name = "sObservaciones", length = 255)
   private String observaciones;
   
   @Lob
   @JsonIgnore
   @Column(name = "xFormato")
   private byte[] formato;
   
   @Lob
   @JsonIgnore
   @Column(name = "xSustento")
   private byte[] sustento;

   @Column(name = "bActivo", nullable = false)
   private boolean activo;

   @PrePersist
   private void prePersist(){
      this.fechaCreacion = new Date();
      this.atendido = false;
      this.activo = true;
   }

   /*
   *
    */
   private static final long serialVersionUID = 1L; 
}

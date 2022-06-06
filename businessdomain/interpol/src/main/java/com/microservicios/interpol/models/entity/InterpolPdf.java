package com.microservicios.interpol.models.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SidInterpolPdf")
@Data
@EqualsAndHashCode(of = { "idArchivoInterpolPdf" })
public class InterpolPdf implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nIdArchivoInterpolPdf")
    private Long idArchivoInterpolPdf;

    @Column(name = "sNombre", length = 55, nullable = false)
    private String nombre;

    @Lob
    @JsonIgnore
    @Column(name = "xArchivo", nullable = false)
    private byte[] archivo;

    @Column(name = "dFechaRegistro", length = 55, nullable = false)
    private Date fechaRegistro;

    @Column(name = "bActivo", nullable = false)
    private boolean activo;

    @PrePersist
    private void prePersist(){
        this.fechaRegistro = new Date();
        this.activo = true;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
}

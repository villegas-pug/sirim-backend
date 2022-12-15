package com.microservicio.rimreportes.model.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "RimTablaDinamica")
@Data
public class TablaDinamica implements Serializable {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "nIdTabla")
   private Long idTabla;
   
   private static final long serialVersionUID = 1L;

}
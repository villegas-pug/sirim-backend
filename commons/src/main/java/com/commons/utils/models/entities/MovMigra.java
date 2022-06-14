package com.commons.utils.models.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "SimMovMigra")
@Data
public class MovMigra implements Serializable {

   @Id
   @Column(name = "sIdMovMigratorio")
   private String idMovMigratorio;

   /*
   *
   */
   private static final long serialVersionUID = 1L;
}

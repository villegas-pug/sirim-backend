package com.microservicio.rimctrlcalidad.repositories;

import com.commons.utils.models.entities.AsigGrupoCamposAnalisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsigGrupoCamposAnalisisRepository extends JpaRepository<AsigGrupoCamposAnalisis, Long> {
   

}

package com.microservicio.rimextraccion.repository;

import com.microservicio.rimextraccion.models.entities.CtrlCalCamposAnalisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RimctrlCalCamposAnalisisRepository extends JpaRepository<CtrlCalCamposAnalisis, Long> {

}
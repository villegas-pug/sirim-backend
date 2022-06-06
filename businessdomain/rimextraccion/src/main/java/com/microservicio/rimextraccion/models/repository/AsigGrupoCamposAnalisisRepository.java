package com.microservicio.rimextraccion.models.repository;

import com.microservicio.rimextraccion.models.entities.AsigGrupoCamposAnalisis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsigGrupoCamposAnalisisRepository extends JpaRepository<AsigGrupoCamposAnalisis, Long> {

}

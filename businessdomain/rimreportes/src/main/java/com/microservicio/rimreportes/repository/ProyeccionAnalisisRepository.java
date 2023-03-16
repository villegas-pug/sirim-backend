package com.microservicio.rimreportes.repository;

import java.util.List;

import com.microservicio.rimreportes.model.entities.ProyeccionAnalisis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProyeccionAnalisisRepository extends JpaRepository<ProyeccionAnalisis, Integer> {

   List<ProyeccionAnalisis> findByAño(int año);
   
}
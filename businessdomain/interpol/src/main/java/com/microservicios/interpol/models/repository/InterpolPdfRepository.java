package com.microservicios.interpol.models.repository;

import java.util.Optional;

import com.microservicios.interpol.models.entity.InterpolPdf;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InterpolPdfRepository extends JpaRepository<InterpolPdf, Long> {
    
    Optional<InterpolPdf> findByNombre(String nombre);
    
}

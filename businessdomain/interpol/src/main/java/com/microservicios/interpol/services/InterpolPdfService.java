package com.microservicios.interpol.services;

import java.util.Optional;
import com.microservicios.interpol.models.entity.InterpolPdf;

public interface InterpolPdfService {

    Optional<InterpolPdf> findById(Long id);

    InterpolPdf save(InterpolPdf interpolPdf);

    void deleteById(Long id);

    Long count();
    
    Optional<InterpolPdf> findByNombre(String nombre);
}

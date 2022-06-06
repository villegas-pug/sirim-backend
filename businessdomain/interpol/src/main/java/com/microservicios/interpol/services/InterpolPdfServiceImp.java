package com.microservicios.interpol.services;

import java.util.Optional;
import com.microservicios.interpol.models.entity.InterpolPdf;
import com.microservicios.interpol.models.repository.InterpolPdfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterpolPdfServiceImp implements InterpolPdfService {

    @Autowired
    private InterpolPdfRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Optional<InterpolPdf> findByNombre(String nombre) {
        return repository.findByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InterpolPdf> findById(Long id) {
        return repository.findById(id);
    }
    
    @Override
    @Transactional
    public InterpolPdf save(InterpolPdf interpolPdf) {
        return repository.save(interpolPdf);
    }
    
    @Override
    @Transactional(readOnly = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long count() {
        return repository.count();
    }
    
}

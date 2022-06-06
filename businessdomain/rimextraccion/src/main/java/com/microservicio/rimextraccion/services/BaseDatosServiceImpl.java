package com.microservicio.rimextraccion.services;

import java.util.List;
import com.microservicio.rimextraccion.models.entities.BaseDatos;
import com.microservicio.rimextraccion.models.repository.BaseDatosRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BaseDatosServiceImpl implements BaseDatosService  {

   @Autowired
   private BaseDatosRepository repository;

   @Override
   @Transactional(readOnly = true)
   public List<BaseDatos> findAll() {
      return this.repository.findAll();
   }

   
}
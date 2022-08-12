package com.microservicio.rimextraccion.services;

import java.util.List;
import com.commons.utils.models.entities.Usuario;
import com.microservicio.rimextraccion.dto.AsigGrupoCamposAnalisisDto;
import com.microservicio.rimextraccion.models.entities.AsigGrupoCamposAnalisis;

public interface RimasigGrupoCamposAnalisisService{

   AsigGrupoCamposAnalisis findById(Long id);
   List<AsigGrupoCamposAnalisisDto> findByUsrAnalista(Usuario usuario);
   void save(AsigGrupoCamposAnalisis asigGrupoCamposAnalisis);
   void deleteById(Long idAsign);

}
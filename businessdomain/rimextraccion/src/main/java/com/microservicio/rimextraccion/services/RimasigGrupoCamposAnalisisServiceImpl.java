package com.microservicio.rimextraccion.services;

import java.util.List;
import java.util.stream.Collectors;
import com.commons.utils.models.entities.Usuario;
import com.microservicio.rimextraccion.models.dto.AsigGrupoCamposAnalisisDto;
import com.microservicio.rimextraccion.models.entities.AsigGrupoCamposAnalisis;
import com.microservicio.rimextraccion.repository.RimasigGrupoCamposAnalisisRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RimasigGrupoCamposAnalisisServiceImpl implements RimasigGrupoCamposAnalisisService{

   @Autowired
   private RimcommonService rimcommonService;

   @Autowired
   private RimasigGrupoCamposAnalisisRepository repository;

   @Autowired
   private ModelMapper modelMapper;

   @Override
   @Transactional
   public void save(AsigGrupoCamposAnalisis asigGrupoCamposAnalisis) {
      this.repository.save(asigGrupoCamposAnalisis);
   }

   @Override
   @Transactional
   public void deleteById(Long idAsign) {
      this.repository.deleteById(idAsign);
   }
   
   @Override
   @Transactional(readOnly = true)
   public List<AsigGrupoCamposAnalisisDto> findByUsrAnalista(Usuario usuario) {
      return this.repository
                     .findByUsrAnalista(usuario)
                     .stream()
                     .map(assig -> modelMapper.map(assig, AsigGrupoCamposAnalisisDto.class))
                     .map(rimcommonService::setTotalsPropsToAsigGrupoCamposAnalisis)
                     .collect(Collectors.toList());
   }

   @Override
   @Transactional(readOnly = true)
   public AsigGrupoCamposAnalisis findById(Long id) {
      AsigGrupoCamposAnalisis asigGrupoCamposAnalisis  = this.repository
                                                                  .findById(id)
                                                                  .orElse(null);
      return asigGrupoCamposAnalisis;
   }

}
package com.microservicio.nacionalizacion.services;

import com.microservicio.nacionalizacion.models.entities.MailFile;
import com.microservicio.nacionalizacion.models.repository.MailFileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MailFileServiceImpl implements MailFileService {

   @Autowired
   private MailFileRepository repository;

   @Override
   @Transactional
   public void deleteById(Long idMailFile) {
      this.repository.deleteById(idMailFile);
   }

   @Override
   @Transactional
   public void delete(MailFile mailFile) {
      this.repository.delete(mailFile);
   }
}
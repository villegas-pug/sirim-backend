package com.microservicio.nacionalizacion.services;

import com.microservicio.nacionalizacion.models.entities.MailFile;

public interface MailFileService {

   void deleteById(Long idMailFile);
   void delete(MailFile mailFile);
   
}
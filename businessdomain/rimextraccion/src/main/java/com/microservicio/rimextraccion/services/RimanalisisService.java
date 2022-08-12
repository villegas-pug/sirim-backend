package com.microservicio.rimextraccion.services;

import java.io.IOException;
import java.util.Date;

import com.microservicio.rimextraccion.dto.AnalizadosDto;

import org.springframework.core.io.ByteArrayResource;

public interface RimanalisisService {

   /*► Repository - Method's ... */

   /*► Custom - Method's ... */
   ByteArrayResource convertProduccionAnalisisToByteArrResource(AnalizadosDto analizadosDto) throws IOException;

}

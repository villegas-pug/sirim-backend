package com.microservicio.rimextraccion.services;

import java.util.List;
import com.microservicio.rimextraccion.models.entities.TipoLogico;

public interface RimdepurarExtraccionService {

   // ► Repo method's ...
   void saveTipoLogico(TipoLogico tipoLogico);
   List<TipoLogico> findAllTipoLogico();
   TipoLogico findTipoLogicoById(Integer idTipo);
   TipoLogico findTipoLogicoByLongitud(int longitud);

   // ► Custom method's ...
   int generateTipoLogicoLongitud();
}

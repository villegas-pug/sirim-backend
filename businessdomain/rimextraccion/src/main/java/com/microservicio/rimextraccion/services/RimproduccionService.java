package com.microservicio.rimextraccion.services;

import java.util.List;
import java.util.Map;

public interface RimproduccionService {

   List<Map<String, Object>> findRecordsAnalisadosByDates(String queryString);

}
package com.microservicio.rimmantenimiento.repositories;

import java.util.Optional;
import com.commons.utils.models.entities.TipoLogico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoLogicoRepository extends JpaRepository<TipoLogico, Integer> {

   Optional<TipoLogico> findByLongitud(int longitud);
   
}

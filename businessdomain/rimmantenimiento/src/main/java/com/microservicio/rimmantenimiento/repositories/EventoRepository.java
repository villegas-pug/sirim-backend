package com.microservicio.rimmantenimiento.repositories;

import java.util.List;
import com.commons.utils.models.entities.Evento;
import com.commons.utils.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

   List<Evento> findByUsuario(Usuario usuario);
   
}
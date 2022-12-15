package com.microservicio.rimasignacion.repository;

import java.util.List;
import com.commons.utils.models.entities.AsigGrupoCamposAnalisis;
import com.commons.utils.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsigGrupoCamposAnalisisRepository extends JpaRepository<AsigGrupoCamposAnalisis, Long> {

   List<AsigGrupoCamposAnalisis> findByUsrAnalista(Usuario usuario);

}

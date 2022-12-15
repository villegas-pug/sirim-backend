package com.microservicio.rimcommon.repositories;

import java.util.List;
import com.commons.utils.models.entities.AsigGrupoCamposAnalisis;
import com.commons.utils.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsigGrupoCamposAnalisisRepository extends JpaRepository<AsigGrupoCamposAnalisis, Long> {

   List<AsigGrupoCamposAnalisis> findByUsrAnalista(Usuario usuario);

}

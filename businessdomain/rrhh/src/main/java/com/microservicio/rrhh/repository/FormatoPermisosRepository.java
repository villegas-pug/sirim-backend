package com.microservicio.rrhh.repository;

import java.util.List;

import com.commons.utils.models.entities.Usuario;
import com.microservicio.rrhh.models.entities.FormatoPermisos;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormatoPermisosRepository extends JpaRepository<FormatoPermisos, Long> {

   List<FormatoPermisos> findByUsrCreador(Usuario usrCreador, Sort sort);
   
}

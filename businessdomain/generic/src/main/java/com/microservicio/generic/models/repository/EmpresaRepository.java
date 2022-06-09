package com.microservicio.generic.models.repository;

import com.commons.utils.models.entities.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

}
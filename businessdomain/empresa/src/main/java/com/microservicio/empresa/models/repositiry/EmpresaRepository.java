package com.microservicio.empresa.models.repositiry;

import com.commons.utils.models.entities.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

}
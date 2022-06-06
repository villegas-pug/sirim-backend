package com.commons.utils.models.repositories;

import java.util.List;
import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommonRepository<E, ID> extends JpaRepository<E, ID> {
   
   @Query(value = "SELECT COLUMN_NAME name FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ?", nativeQuery = true)
   List<Tuple> findTableMetaByName(String nombreTabla);

}

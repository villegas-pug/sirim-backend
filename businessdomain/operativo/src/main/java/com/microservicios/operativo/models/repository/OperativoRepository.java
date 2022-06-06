package com.microservicios.operativo.models.repository;

import java.util.List;

import javax.persistence.Tuple;

import com.commons.utils.models.dto.IntervenidoOpeDto;
import com.commons.utils.models.dto.ModalidadOpeDto;
import com.commons.utils.models.dto.NacionalidadOpeDto;
import com.commons.utils.models.dto.OperativoDto;
import com.commons.utils.models.dto.RptOperativoDto;
import com.commons.utils.models.dto.SexoOperativoDto;
import com.commons.utils.models.dto.TipoInfraccionOpeDto;
import com.commons.utils.models.dto.TipoOperativoDto;
import com.microservicios.operativo.models.entities.Operativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface OperativoRepository extends JpaRepository<Operativo, Long> {

    @Query(value = "{CALL dbo.spu_Sid_Rpt_ContarOpeAnual}", nativeQuery = true)
    List<OperativoDto> countPivotedByOpeAnual();
    
    @Query(value = "{CALL dbo.spu_Sid_Rpt_ContarOpe}", nativeQuery = true)
    List<?> countPivotedOpe();

    @Query(value = "{CALL dbo.spu_Sid_Rpt_ContarIntervenidos(:añoOpe, :sexo, :dependencia)}", nativeQuery = true)
    List<IntervenidoOpeDto> countPivotedByIntervenidos(
        @Param("añoOpe") String añoOpe, 
        @Param("sexo") String sexo,
        @Param("dependencia") String dependencia);

    @Query(value = "{CALL dbo.spu_Sid_Rpt_ContarTipoInfraccion(:añoOpe, :sexo, :dependencia)}", nativeQuery = true )
    List<TipoInfraccionOpeDto> countPivotedByTipoInfraccion(
        @Param("añoOpe") String año, 
        @Param("sexo") String sexo,
        @Param("dependencia") String dependencia);

    @Query(value = "{CALL dbo.spu_Sid_Rpt_ContarTipoOperativo(:añoOpe, :sexo, :dependencia)}", nativeQuery = true)
    List<TipoOperativoDto> countPivotedByTipoOperativo(
        @Param("añoOpe") String año, 
        @Param("sexo") String sexo,
        @Param("dependencia") String dependencia);

    @Query(value = "{CALL dbo.spu_Sid_Rpt_ContarSexoOperativo(:añoOpe, :dependencia)}", nativeQuery = true)
    List<SexoOperativoDto> countPivotedBySexo(
        @Param("añoOpe") String año, 
        @Param("dependencia") String dependencia);

    @Query(value = "{CALL dbo.spu_Sid_Rpt_ContarOpeNacionalidad(:añoOpe, :sexo, :dependencia)}", nativeQuery = true)
    List<NacionalidadOpeDto> countPivotedOpeByNacionalidad(
        @Param("añoOpe") String año, 
        @Param("sexo") String sexo,
        @Param("dependencia") String dependencia);
    

    @Query(value = "{CALL spu_Sid_Rpt_ContarOpePorModalidad(:añoOpe, :dependencia)}", nativeQuery = true)
    List<ModalidadOpeDto> countPivotedOpeByModalidad(@Param("añoOpe") String añoOpe, @Param("dependencia") String dependencia);

    @Query(value = "{CALL dbo.spu_Sid_Rpt_OperativoPorFiltroAvanzado(:idOpe, :fecIni, :fecFin, :dependencia, :modalidad, :sexo, :tipoOperativo)}", nativeQuery = true)
    List<RptOperativoDto> findOpeByCustomFilterToExcel(
            @Param("idOpe") Long idOpe,
            @Param("fecIni") String fecIni,
            @Param("fecFin") String fecFin,
            @Param("dependencia") String dependencia,
            @Param("modalidad") String modalidad,
            @Param("sexo") String sexo,
            @Param("tipoOperativo") String tipoOperativo);

    @Query(name = "query_ope_find_all", nativeQuery = true)
    List<Operativo> findQueryOpeAll();

    @Query(value = "{CALL dbo.usp_create_table}", nativeQuery = true)
    List<Tuple> createTable();

}
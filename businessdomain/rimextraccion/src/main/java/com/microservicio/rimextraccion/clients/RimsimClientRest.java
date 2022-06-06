package com.microservicio.rimextraccion.clients;

import java.util.List;
import java.util.Map;
import com.commons.utils.models.dto.QueryClauseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservicio-rimsim")
public interface RimsimClientRest {

   @GetMapping( path = { "/findTableMetaByNameSim" })
   public List<Map<String, String>> findTableMetaByNameSim(String nombreTabla);

   @PostMapping(path = { "/dynamicJoinStatementSim" })
   public List<Object[]> dynamicJoinStatementSim(@RequestBody QueryClauseDto queryClauseDto);
   
}

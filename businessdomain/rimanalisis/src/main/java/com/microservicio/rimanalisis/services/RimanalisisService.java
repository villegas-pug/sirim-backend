package com.microservicio.rimanalisis.services;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.commons.utils.models.dto.RecordsBetweenDatesDto;
import com.commons.utils.models.dto.RptMensualProduccionDto;
import com.commons.utils.models.dto.RptProduccionHoraLaboralDto;
import com.commons.utils.models.dto.RptTiempoPromedioAnalisisDto;
import com.commons.utils.models.entities.AsigGrupoCamposAnalisis;
import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;

import org.springframework.core.io.ByteArrayResource;

public interface RimanalisisService {

   // ► Repository - Method's ...
   List<Map<String, Object>> findRecordsAnalisadosByDates(String queryString);
   LinkedList<RptMensualProduccionDto> findReporteMensualProduccionByParams(UUID idUsrAnalista, int month, int year);

   List<RptTiempoPromedioAnalisisDto> getRptTiempoPromedioAnalisisByParms(Date fecIni, Date fecFin);
   List<RptProduccionHoraLaboralDto> getRptProduccionHorasLaboralesPorAnalista(Date fechaAnalisis, String grupo);
   List<Map<String, Object>> getRptS10DRCMFR001Produccion(String nombreTabla, Date fecIni, Date fecFin, boolean isRoot, Long idAsig);

   AsigGrupoCamposAnalisis findAsigGrupoCamposAnalisisById(Long id);

   // ► Client method's ...
   Response<Usuario> findUsuarioByLogin(String login);

   // ► Custom - Method's ...
   ByteArrayResource convertProduccionAnalisisToByteArrResource(RecordsBetweenDatesDto recordsBetweenDatesDto) throws IOException;
   ByteArrayResource convertReporteMensualProduccionToByteArrResource(String login, int month, int year) throws IOException;

}

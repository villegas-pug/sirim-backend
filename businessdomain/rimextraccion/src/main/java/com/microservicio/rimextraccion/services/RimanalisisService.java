package com.microservicio.rimextraccion.services;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.commons.utils.models.entities.Usuario;
import com.commons.utils.utils.Response;
import com.microservicio.rimextraccion.models.dto.RecordAssignedDto;
import com.microservicio.rimextraccion.models.dto.RecordsBetweenDatesDto;
import com.microservicio.rimextraccion.models.dto.ReporteMensualProduccionDto;

import org.springframework.core.io.ByteArrayResource;

public interface RimanalisisService {

   // ► Repository - Method's ...
   void saveRecordAssigned(RecordAssignedDto recordAssignedDto);
   List<Map<String, Object>> findRecordsAnalisadosByDates(String queryString);
   LinkedList<ReporteMensualProduccionDto> findReporteMensualProduccionByParams(UUID idUsrAnalista, int month, int year);

   // ► Client method's ...
   Response<Usuario> findUsuarioByLogin(String login);

   // ► Custom - Method's ...
   ByteArrayResource convertProduccionAnalisisToByteArrResource(RecordsBetweenDatesDto recordsBetweenDatesDto) throws IOException;
   ByteArrayResource convertReporteMensualProduccionToByteArrResource(String login, int month, int year) throws IOException;

}

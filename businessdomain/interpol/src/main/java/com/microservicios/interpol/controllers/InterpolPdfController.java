package com.microservicios.interpol.controllers;

import java.io.IOException;
import java.util.Arrays;
import com.commons.utils.constants.Messages;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.commons.utils.utils.Response;
import com.microservicios.interpol.models.entity.InterpolPdf;
import com.microservicios.interpol.services.InterpolPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RefreshScope
@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
public class InterpolPdfController {

    @Autowired
    private InterpolPdfService service;

    @PostMapping(path = "/saveAll")
    @SuppressWarnings(value = { "null" })
    public ResponseEntity<?> saveAll(@RequestPart MultipartFile archivo) throws IOException {

        String fileName = archivo.getOriginalFilename().split("\\.")[0];
        byte[] file = archivo.getBytes();
        
        InterpolPdf interpolPdfDb = service.findByNombre(fileName).orElse(new InterpolPdf());

        if (interpolPdfDb.getIdArchivoInterpolPdf() != null) {
            interpolPdfDb.setArchivo(file);
        }else{
            interpolPdfDb.setArchivo(file);
            interpolPdfDb.setNombre(fileName);
        }
        
        service.save(interpolPdfDb);

        return ResponseEntity.ok().body(
                Response
                    .builder()
                    .message(Messages.MESSAGE_SUCCESS_CREATE)
                    .data(Arrays.asList()).build());
    }

    @GetMapping(path = "/downloadInterpol/{nombre}", produces = { MediaType.APPLICATION_PDF_VALUE })
    public ResponseEntity<?> findByName(@PathVariable String nombre) {
        InterpolPdf interpolPdfDb = service.findByNombre(nombre).orElseThrow(() -> new DataAccessEmptyWarning());
        Resource pdf = new ByteArrayResource(interpolPdfDb.getArchivo());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdf);
    }

}

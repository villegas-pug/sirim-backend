package com.microservicio.rimextraccion.services;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import com.commons.utils.errors.DataAccessEmptyWarning;
import com.google.common.primitives.Ints;
import com.microservicio.rimextraccion.models.entities.TipoLogico;
import com.microservicio.rimextraccion.repository.TipoLogicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RimdepurarExtraccionServiceImpl implements RimdepurarExtraccionService {

   @Autowired
   private TipoLogicoRepository tipoLogicoRepository;

   //#region Repo method's ...

   @Override
   @Transactional
   public void saveTipoLogico(TipoLogico tipoLogico) {

      // ► Dep's ...
      int idTipo = Objects.isNull(tipoLogico.getIdTipo()) || tipoLogico.getIdTipo() == 0 ? -1 : tipoLogico.getIdTipo();

      // ► Repo dep's ...

      // Validación: ...
      if (idTipo == -1) { // ► Nuevo ...
         tipoLogico.setLongitud(this.generateTipoLogicoLongitud());
      } else { // ► Actualizar ...
         tipoLogico.setValoresCsv(tipoLogico.getValoresCsv());
      }

      // ► Save ...
      tipoLogicoRepository.save(tipoLogico);
   }

   @Override
   @Transactional(readOnly = true)
   public List<TipoLogico> findAllTipoLogico() {
      List<TipoLogico> tipoLogicoDb = this.tipoLogicoRepository.findAll();
      if(tipoLogicoDb.size() == 0) throw new DataAccessEmptyWarning();
      return tipoLogicoDb;
   }

   @Override
   @Transactional(readOnly = true)
   public TipoLogico findTipoLogicoByLongitud(int longitud) {
      TipoLogico tipoLogico = this.tipoLogicoRepository
                                          .findByLongitud(longitud)
                                          .orElseThrow(DataAccessEmptyWarning::new);
      return tipoLogico;
   }

   @Override
   @Transactional(readOnly = true)
   public TipoLogico findTipoLogicoById(Integer idTipo) {
      TipoLogico tipoLogico = this.tipoLogicoRepository.findById(idTipo)
                                                       .orElseThrow(DataAccessEmptyWarning::new);
      return tipoLogico;
   }

   //#region

   //#region Custom method's ...

   @Override
   public int generateTipoLogicoLongitud() {

      // ► Dep's ...
      int minLen = 100,
          maxLen = 255;

      Random rnd = new Random();
      int len = (rnd.nextInt(maxLen - minLen) + 1) + minLen;

      // ► Repo dep's ...
      int[] usedLens = this.tipoLogicoRepository
                                    .findAll()
                                    .stream()
                                    .mapToInt(t -> t.getLongitud())
                                    .toArray();

      // ► ...
      while (Ints.indexOf(usedLens, len) >= 0) {
         len = (rnd.nextInt(maxLen - minLen) + 1) + minLen;
      }

      return len;
   }

   //#endregion
   
}

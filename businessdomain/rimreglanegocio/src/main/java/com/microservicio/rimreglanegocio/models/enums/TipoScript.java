package com.microservicio.rimreglanegocio.models.enums;

public enum TipoScript {
   DETECCION("Detección"),
   VALIDACION("Validación"),
   CORRECCION("Corrección");

   private String value;

   TipoScript(String value) {
      this.value = value;
   }
}

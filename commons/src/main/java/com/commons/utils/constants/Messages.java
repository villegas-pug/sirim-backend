package com.commons.utils.constants;

import com.commons.utils.utils.LogAndResponse;

public class Messages {
   /*-> SUCCESS: */
   public static final String MESSAGE_SUCCESS_NEW_ID = "¡El código %d, ha sido generado con exito!";
   public static final String MESSAGE_SUCCESS_LIST_ENTITY = "¡La entidad fué listada con exito!";
   public static final String MESSAGE_SUCCESS_CREATE = "¡Registro exitoso!";
   public static final String MESSAGE_SUCCESS_UPDATE = "¡La entidad fué actualizada con exito!";
   public static final String MESSAGE_SUCCESS_DOWNLOAD = "¡El anexo, se descargo exitosamente!";
   public static final String MESSAGE_SUCCESS_AUTH = "!Acceso permitido¡";
   public static final String MESSAGE_SUCCESS_SEARCH_RESULT = "¡Se encontraron resultados, para : %s!";
   public static final String MESSAGE_SUCCESS_CREATE_USER = "¡El usuario %s, fué creado con exito!";
   public static final String MESSAGE_SUCCESS_UPDATE_USER = "¡El usuario %s, fué actualizado con exito!";
   public static final String MESSAGE_SUCCESS_DELETE_BY_ID = "¡El registro con código %s, fué eliminado exitosamente!";
   public static final String MESSAGE_SUCCESS_SAVE = "¡%s, fué guardado existosamente!";
   public static final String SUCCESS_SAVE_TO_ASSIGN_REVISOR = "¡Revisor: %s, asignado exitosamente!";
   public static final String SUCCESS_SAVE_TO_UNASSIGN_REVISOR = "¡Asignación eliminada!";
   public static final String SUCCESS_SAVE_TO_READ_ASSIGNMENT_REVISOR = "¡El expediente recibido exitosamente!";
   public static final String SUCCESS_UPLOAD_FILE = "¡Archivo guardado exitosamente!";
   public static final String SUCCESS_BULK_FILE = "¡Se insertaron: %d registros en la tabla: %s!";
   public static final String SUCCESS_CREATE_TABLE = "¡Tabla %s, creada exitosamente!";
   public static final String SUCCESS_ALTER_TABLE = "¡Tabla %s, modificada exitosamente!";
   public static final String SUCCESS_ALTER_TABLE_ADD_COLUMN = "¡Se agregaron los campos exitosamente en la tabla: %s!";
   public static final String SUCCESS_ALTER_TABLE_EDIT_COLUMN = "¡Tipo de campo cambiado exitosamente!";
   public static final String SUCCESS_ALTER_META_NAME = "¡Nombre cambiado exitosamente!";
   public static final String SUCCESS_CREATE_GROUP_OF_ANALISIS = "¡Grupo:  %s, creado exitosamente!";
   public static final String SUCCESS_ASIGN_REG_ANALISIS = "¡Rangos asignados exitosamente!";
   public static final String SUCCESS_DELETE_TABLA = "¡Tabla: %s, eliminada exitosamente!";
   public static final String SUCCESS_DELETE_RECORD = "¡Registro: %s, eliminado exitosamente!";
   public static final String SUCCESS_SAVE_DILIGENCIA = "¡Diligencia guardada exitosamente!";
   public static final String SUCCESS_SAVE_OPINION = "¡Opinión guardada exitosamente!";
   public static final String SUCCESS_SAVE_DATA_MODEL = "¡Model de datos: %s, guardada exitosamente!";
   public static final String SUCCESS_SAVE_ANALISIS_EXTRACCION = "¡Registro analizado exitosamente!";
   public static final String SUCCESS_DELETE_GRUPO_ANALISIS = "¡Grupo eliminado existosamente!";
   public static final String SUCCESS_CREATE_RECORDS_FOR_CTRLCAL = "¡Registros para control de calidad generedos exitosamente!";
   public static final String SUCCESS_RESULT_CONFORMITY_CTRLCAL = "¡Resultado de la conformidad del control de calidad, fué registrado exitosamente!";
   public static final String SUCCESS_VALIDATE_RECORD_CTRLCAL = "¡Validación registrada exitosamente!";

   /*-> WARNING: */
   public static final String MESSAGGE_WARNING_EMPTY = "¡No hay registros para mostrar!";
   public static final String MESSAGE_WARNING_ENTITY_FIND_BY_ID = "¡El código %s, no existe en la tabla!";
   public static final String MESSAGE_WARNING_USER_AUTH = "¡Usuario o clave incorrecta!";
   public static final String MESSAGE_WARNING_USER_NOTFOUND = "¡Usuario %s no existe!";
   public static final String MESSAGE_WARNING_DATA_SAVE = "¡Se ha poducido un error inesperado, vuelva intentarlo!";
   public static final String MESSAGE_WARNING_FILE_SAVE = "¡Extension de archivo `.%s`, no soportado!";
   public static final String WARNING_CREATE_TABLE = "¡Ya existe una tabla con el nombre: %s!";
   public static final String WARNING_UPLOAD_TABLE = "¡Archivo: %s, no es compatible!";
   public static final String WARNING_ALTER_TABLE_ADD_COLUMN = "¡Nombre de campo: %s, en uso!";
   public static final String WARNING_ASIGN_REG_ANALISIS = "¡Los rangos ingresados, ya fueron asignados!";
   public static final String WARNING_ADD_GROUP_ANALISIS = "¡Nombre de grupo: %s, en uso!";
   public static final String WARNING_DUPLICATE_MODEL_DATA = "¡Nombre de modelo de datos: %s, en uso!";
   public static final String WARNING_RECORDS_NOT_FOUND_TO_SEGMENT = "¡No existen registros, para segmentar!";
   public static final String WARNING_IS_IN_CTRLCAL = "¡Asignación actualmente se encuentra en control de calidad!";
   public static final String WARNING_USER_NOT_EXISTS = "¡Usuario ingresado no existe!";

   /*-> ERROR: */
   public final static String MESSAGGE_ERROR_DATA_ACCESS = "¡Ocurrió un error, contacte a sitemas y proporcione este código %s!";
   public static final String ERROR_FILE_SAVE = "¡Ocurrió un error al guardar el archivo: %s!";

   /*-> -----------------------------METHOD'S--------------------------- */
   /*-> SUCCESS: */
   public static String MESSAGE_SUCCESS_CREATE() {
      return MESSAGE_SUCCESS_CREATE;
   }

   public static String MESSAGE_SUCCESS_UPDATE() {
      return MESSAGE_SUCCESS_UPDATE;
   }

   public static String MESSAGE_SUCCESS_SEARCH_RESULT(String value) {
      return String.format(MESSAGE_SUCCESS_SEARCH_RESULT, value);
   }

   public static String MESSAGE_SUCCESS_CREATE_USER(String newUser) {
      return String.format(MESSAGE_SUCCESS_CREATE_USER, newUser);
   }

   public static String MESSAGE_SUCCESS_UPDATE_USER(String userName){
      return String.format(MESSAGE_SUCCESS_UPDATE_USER, userName);
   }

   public static String MESSAGE_SUCCESS_NEW_ID(Long newId) {
      return String.format(MESSAGE_SUCCESS_NEW_ID, newId);
   }

   public static String MESSAGE_SUCCESS_LIST_ENTITY() {
      return MESSAGE_SUCCESS_LIST_ENTITY;
   }

   public static String MESSAGE_SUCCESS_DELETE_BY_ID(Long id){
      return String.format(MESSAGE_SUCCESS_DELETE_BY_ID, id);
   }

   public static String MESSAGE_SUCCESS_SAVE(String entity){
      return String.format(MESSAGE_SUCCESS_SAVE, entity);
   }

   public static String SUCCESS_SAVE_TO_ASSIGN_REVISOR(String usrRevisor){
      return String.format(SUCCESS_SAVE_TO_ASSIGN_REVISOR, usrRevisor);
   }

   public static String SUCCESS_CREATE_TABLE(String nombreTabla){
      return String.format(SUCCESS_CREATE_TABLE, nombreTabla);
   }

   public static String SUCCESS_ALTER_TABLE(String nombreTabla){
      return String.format(SUCCESS_ALTER_TABLE, nombreTabla);
   }

   public static String SUCCESS_ALTER_TABLE_ADD_COLUMN(String nombreTabla){
      return String.format(SUCCESS_ALTER_TABLE_ADD_COLUMN, nombreTabla);
   }

   public static String SUCCESS_CREATE_GROUP_OF_ANALISIS(String nombreGrupo){
      return String.format(SUCCESS_CREATE_GROUP_OF_ANALISIS, nombreGrupo);
   }

   public static String SUCCESS_DELETE_TABLA(String nombreTabla){
      return String.format(SUCCESS_DELETE_TABLA, nombreTabla);
   }

   public static String SUCCESS_DELETE_RECORD(Long id){
      return String.format(SUCCESS_DELETE_RECORD, id);
   }

   public static String SUCCESS_BULK_FILE(Long filas, String nombreTabla){
      return String.format(SUCCESS_BULK_FILE, filas, nombreTabla);
   }

   public static String SUCCESS_SAVE_DATA_MODEL(String modelName){
      return String.format(SUCCESS_SAVE_DATA_MODEL, modelName);
   }

   /*-> WARNING */
   public static String MESSAGE_WARNING_ENTITY_FIND_BY_ID(long id) {
      return String.format(MESSAGE_WARNING_ENTITY_FIND_BY_ID, id);
   }

   public static String MESSAGGE_WARNING_EMPTY() {
      return MESSAGGE_WARNING_EMPTY;
   }

   public static String MESSAGE_WARNING_USER_NOTFOUND(String login){
      return String.format(MESSAGE_WARNING_USER_NOTFOUND, login);
   }

   public static String MESSAGE_WARNING_FILE_SAVE(String fileName){
      return String.format(MESSAGE_WARNING_FILE_SAVE, fileName);
   }

   public static String WARNING_CREATE_TABLE(String nombreTabla){
      return String.format(WARNING_CREATE_TABLE, nombreTabla);
   }

   public static String WARNING_UPLOAD_TABLE(String fileName){
      return String.format(WARNING_UPLOAD_TABLE, fileName);
   }

   public static String WARNING_ALTER_TABLE_ADD_COLUMN(String fieldName){
      return String.format(WARNING_ALTER_TABLE_ADD_COLUMN, fieldName);
   }

   public static String WARNING_ADD_GROUP_ANALISIS(String nombreGrupo){
      return String.format(WARNING_ADD_GROUP_ANALISIS, nombreGrupo);
   }

   public static String WARNING_DUPLICATE_MODEL_DATA(String modelName){
      return String.format(WARNING_DUPLICATE_MODEL_DATA, modelName);
   }

   /*-> ERROR */
   public static String MESSAGGE_ERROR_DATA_ACCESS() {
      return String.format(MESSAGGE_ERROR_DATA_ACCESS, LogAndResponse.getIdLog());
   }

   public static String ERROR_FILE_SAVE(String fileName){
      return String.format(ERROR_FILE_SAVE, fileName);
   }

}

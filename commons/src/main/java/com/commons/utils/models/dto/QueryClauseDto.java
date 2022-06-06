package com.commons.utils.models.dto;

import lombok.Data;

@Data
public class QueryClauseDto {

   private String mod;
   private String fields;
   private String where;

}

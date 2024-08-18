package com.backend.testdata.domain.constants;

import java.util.Arrays;
import java.util.List;

public enum ExportFileType {

  CSV,
  TSV,
  JSON,
  SQL_INSERT,
  ;


  private static final List<ExportFileType> OBJECTS = Arrays.stream(ExportFileType.values())
      .toList();


  public static List<ExportFileType> toObjects() {
    return OBJECTS;
  }
}

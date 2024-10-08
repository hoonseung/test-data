package com.backend.testdata.domain.constants;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MockDataType {

  STRING(Set.of("minLength", "maxLength"), null),
  NUMBER(Set.of("min", "max", "decimal"), null),
  BOOLEAN(Set.of(), null),
  DATETIME(Set.of("from", "to"), null),
  ENUM(Set.of("elements"), null),

  SENTENCE(Set.of("minSentences", "maxSentences"), STRING),
  PARAGRAPH(Set.of("minParagraphs", "maxParagraphs"), STRING),
  UUID(Set.of(), STRING),
  EMAIL(Set.of(), STRING),
  CAR(Set.of(), STRING),
  ROW_NUMBER(Set.of("start, step"), NUMBER),
  NAME(Set.of(), STRING),

  ;


  private final Set<String> options;
  private final MockDataType baseType;
  private static final List<MockDataTypeObject> OBJECTS = Arrays.stream(MockDataType.values())
      .map(MockDataType::toObject).toList();


  public boolean isBaseType() {
    return this.baseType == null;
  }

  public MockDataTypeObject toObject() {
    return new MockDataTypeObject(
        this.name(),
        this.options,
        this.baseType == null ? null : this.baseType.name()
    );
  }

  public static Object toObjects() {
    return OBJECTS;
  }


  public record MockDataTypeObject(String name, Set<String> options, String baseType) {

  }
}

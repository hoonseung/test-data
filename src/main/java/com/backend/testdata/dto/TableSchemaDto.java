package com.backend.testdata.dto;

import com.backend.testdata.domain.TableSchemaEntity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;


public record TableSchemaDto(
        Long id,
        String schemaName,
        String userId,
        LocalDateTime exportedAt,
        Set<SchemaFieldDto> schemaFields,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy

) {

    public static TableSchemaDto of(Long id, String schemaName, String userId, LocalDateTime exportedAt, Set<SchemaFieldDto> schemaFields, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new TableSchemaDto(id, schemaName, userId, exportedAt, schemaFields, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static TableSchemaDto of(String schemaName, String userId, LocalDateTime exportedAt, Set<SchemaFieldDto> schemaFields) {
        return new TableSchemaDto(null, schemaName, userId, exportedAt, schemaFields, null, null, null, null);
    }


    public static TableSchemaDto toDto(TableSchemaEntity entity) {
        return new TableSchemaDto(
                entity.getId(),
                entity.getSchemaName(),
                entity.getUserId(),
                entity.getExportedAt(),
                entity.getSchemaFields().stream()
                        .map(SchemaFieldDto::toDto)
                        .collect(Collectors.toUnmodifiableSet()),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }


    public TableSchemaEntity createEntity() {
        TableSchemaEntity tableSchemaEntity = TableSchemaEntity.of(this.schemaName, this.userId);
        tableSchemaEntity.addAllSchemaFieldAndSetRelation(this.schemaFields.stream()
                        .map(SchemaFieldDto::createEntity)
                        .toList()
        );

        return tableSchemaEntity;
    }


}
package com.backend.testdata.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 식별자 {@link #userId} 로 특정한 회원이 소유한다.
 *
 * @author hoonseung
 */

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "schemaFields", callSuper = true)
@Getter
@Table(name = "\"table_schema\"")
@Entity
public class TableSchemaEntity extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "schema_name", nullable = false)
    private String schemaName;
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "exported_at")
    private LocalDateTime exportedAt;

    @OneToMany(mappedBy = "tableSchema", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<SchemaFieldEntity> schemaFields = new LinkedHashSet<>();


    private TableSchemaEntity(String schemaName, String userId) {
        this.schemaName = schemaName;
        this.userId = userId;
    }

    public static TableSchemaEntity of(String schemaName, String userId) {
        return new TableSchemaEntity(
                schemaName,
                userId
        );
    }


    public void addSchemaFieldAndSetRelation(SchemaFieldEntity schemaField) {
        this.schemaFields.add(schemaField);
        schemaField.setRelationEntity(this);
    }

    public void addAllSchemaFieldAndSetRelation(Collection<SchemaFieldEntity> schemaFields) {
        schemaFields.forEach(this::addSchemaFieldAndSetRelation);
    }

    public void clearSchemaFields() {
        this.schemaFields.clear();
    }

    public boolean isExported() {
        return this.exportedAt != null;
    }

    public void markExported() {
        this.exportedAt = LocalDateTime.now();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableSchemaEntity that)) return false;
        if (this.getId() == null || that.getId() == null) {
            return Objects.equals(this.getSchemaName(), that.getSchemaName())
                    && Objects.equals(this.getUserId(), that.getUserId())
                    && Objects.equals(this.getExportedAt(), that.getExportedAt())
                    && Objects.equals(this.getSchemaFields(), that.getSchemaFields());
        }
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return this.getId() == null ? Objects.hash(getSchemaName(), getUserId(), getExportedAt(), getSchemaFields())
                : Objects.hash(getId());
    }
}

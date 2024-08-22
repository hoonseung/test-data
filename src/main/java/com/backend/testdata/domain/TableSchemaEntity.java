package com.backend.testdata.domain;


import com.backend.testdata.dto.SchemaFieldDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 식별자 {@link #userId} 로 특정한 회원이 소유한다.
 *
 * @author hoonseung
 */

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "schemaFields", callSuper = true)
@Getter
@Table(name = "\"table_schema\"", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "schema_name"})
},
    indexes = {
        @Index(columnList = "created_at"),
        @Index(columnList = "modified_at")
    })
@Entity
public class TableSchemaEntity extends AuditingField {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Column(name = "schema_name", nullable = false)
  private String schemaName;
  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(name = "exported_at")
  private LocalDateTime exportedAt;

  @OrderBy("fieldOrder asc")
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


  public void updateTableSchema(String schemaName, String userId, LocalDateTime exportedAt,
      Collection<SchemaFieldDto> schemaFields) {
    if (!StringUtils.hasText(schemaName)) {
      this.schemaName = schemaName;
    }
    if (!StringUtils.hasText(userId)) {
      this.userId = userId;
    }

    this.exportedAt = exportedAt;

    if (!CollectionUtils.isEmpty(schemaFields)) {
      clearSchemaFields();
      addAllSchemaFieldAndSetRelation(
          schemaFields.stream().map(SchemaFieldDto::createEntity).toList());
    }

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
    if (this == o) {
      return true;
    }
    if (!(o instanceof TableSchemaEntity that)) {
      return false;
    }
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
    return this.getId() == null ? Objects.hash(getSchemaName(), getUserId(), getExportedAt(),
        getSchemaFields())
        : Objects.hash(getId());
  }


}

package com.backend.testdata.domain;

import com.backend.testdata.domain.constants.MockDataType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

/**
 * @author hoonseung
 */
@ToString(callSuper = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"schema_field\"}")
@Entity
public class SchemaFieldEntity extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "table_schema_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TableSchemaEntity tableSchema;

    @Column(name = "field_name", nullable = false)
    private String fieldName;
    @Column(name = "mock_data_type", nullable = false)
    private MockDataType mockDataType;
    @Column(name = "field_order", nullable = false)
    private Integer fieldOrder;
    @Column(name = "blank_percent", nullable = false)
    private Integer blankPercent;

    @Column(name = "type_option_json")
    private String typeOptionJson;
    @Column(name = "force_value")
    private String forceValue;


    private SchemaFieldEntity(String fieldName, MockDataType mockDataType, Integer fieldOrder, Integer blankPercent, String typeOptionJson, String forceValue) {
        this.fieldName = fieldName;
        this.mockDataType = mockDataType;
        this.fieldOrder = fieldOrder;
        this.blankPercent = blankPercent;
        this.typeOptionJson = typeOptionJson;
        this.forceValue = forceValue;
    }


    public static SchemaFieldEntity of(String fieldName, MockDataType mockDataType, Integer fieldOrder, Integer blankPercent, String typeOptionJson, String forceValue) {
        return new SchemaFieldEntity(
                fieldName,
                mockDataType,
                fieldOrder,
                blankPercent,
                typeOptionJson,
                forceValue
        );
    }

    public void setRelationEntity(TableSchemaEntity tableSchema) {
        this.tableSchema = tableSchema;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SchemaFieldEntity that)) return false;
        if (this.getId() == null || that.getId() == null) {
            return Objects.equals(this.getFieldName(), that.getFieldName())
                    && Objects.equals(this.getMockDataType(), that.getMockDataType())
                    && Objects.equals(this.getFieldOrder(), that.getFieldOrder())
                    && Objects.equals(this.getBlankPercent(), that.getBlankPercent())
                    && Objects.equals(this.getTypeOptionJson(), that.getTypeOptionJson())
                    && Objects.equals(this.getForceValue(), that.getForceValue());
        }
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return this.getId() == null ? Objects.hash(getFieldName(), getMockDataType(), getFieldOrder(), getBlankPercent(), getTypeOptionJson(), getForceValue())
                : Objects.hash(getId());
    }
}

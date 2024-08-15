package com.backend.testdata.domain;


import com.backend.testdata.domain.constants.MockDataType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

/**
 * 바로 만들 수 없는 가짜 데이터 자료형 {@link MockDataType} 에 대응하는 데이터
 * 문자열, 숫자경우 랜덤으로 만들 수 있지만 자동차, 학교, 도시 이런 경우는 미리 저장했다가 뽑아서 사용해야함
 *
 * @author hoonseung
 */
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"mock_data\"",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"mock_data_type", "mock_data_value"})
        }
)
@Entity
public class MockDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "mock_data_type", nullable = false)
    private MockDataType mockDataType;
    @Column(name = "mock_data_value", nullable = false)
    private String mockDataValue;


    private MockDataEntity(MockDataType mockDataType, String mockDataValue) {
        this.mockDataType = mockDataType;
        this.mockDataValue = mockDataValue;
    }

    public static MockDataEntity of(MockDataType mockDataType, String mockDataValue) {
        return new MockDataEntity(mockDataType, mockDataValue);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MockDataEntity that)) return false;
        if (getId() == null || that.getId() == null) {
            return this.getMockDataType() == that.getMockDataType() && Objects.equals(this.getMockDataValue(), that.getMockDataValue());
        }
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? Objects.hash(getMockDataType(), getMockDataValue()) : Objects.hash(getId());
    }
}

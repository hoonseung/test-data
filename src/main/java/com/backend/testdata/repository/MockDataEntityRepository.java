package com.backend.testdata.repository;

import com.backend.testdata.domain.MockDataEntity;
import com.backend.testdata.domain.constants.MockDataType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MockDataEntityRepository extends JpaRepository<MockDataEntity, Long> {

    List<MockDataEntity> findByMockDataType(MockDataType mockDataType);
}


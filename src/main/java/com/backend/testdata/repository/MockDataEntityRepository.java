package com.backend.testdata.repository;

import com.backend.testdata.domain.MockDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MockDataEntityRepository extends JpaRepository<MockDataEntity, Long> {
}


package com.backend.testdata.repository;

import com.backend.testdata.domain.SchemaFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchemaFieldEntityRepository extends JpaRepository<SchemaFieldEntity, Long> {
}

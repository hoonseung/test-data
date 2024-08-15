package com.backend.testdata.repository;

import com.backend.testdata.domain.TableSchemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableSchemaEntityRepository extends JpaRepository<TableSchemaEntity, Long> {
}

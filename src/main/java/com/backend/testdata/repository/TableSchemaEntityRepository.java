package com.backend.testdata.repository;

import com.backend.testdata.domain.TableSchemaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TableSchemaEntityRepository extends JpaRepository<TableSchemaEntity, Long> {

    Page<TableSchemaEntity> findAllByUserId(String userId, Pageable pageable);

    Optional<TableSchemaEntity> findByUserIdAndSchemaName(String userId, String schemaName);

    void deleteByUserIdAndSchemaName(String userId, String schemaName);
}

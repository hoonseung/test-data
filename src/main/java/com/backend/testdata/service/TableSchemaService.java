package com.backend.testdata.service;

import com.backend.testdata.dto.TableSchemaDto;
import com.backend.testdata.repository.TableSchemaEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TableSchemaService {

    private final TableSchemaEntityRepository tableSchemaEntityRepository;


    public List<TableSchemaDto> loadTableSchemas(String userId) {
        return tableSchemaEntityRepository.findAllByUserId(userId, Pageable.unpaged())
            .map(TableSchemaDto::toDto)
            .toList();
    }


    public Page<TableSchemaDto> loadTableSchemas(String userId, Pageable pageable) {
        return tableSchemaEntityRepository.findAllByUserId(userId, pageable)
            .map(TableSchemaDto::toDto);
    }

    public TableSchemaDto loadMyTableSchema(String userId, String schemaName) {
        return tableSchemaEntityRepository.findByUserIdAndSchemaName(userId, schemaName)
            .map(TableSchemaDto::toDto)
            .orElseThrow(
                () -> new EntityNotFoundException(
                    String.format("해당 스키마를 찾을 수 없습니다. id : %s", userId)));
    }

    @Transactional
    public void upsertMySchema(TableSchemaDto tableSchemaDto) {
        tableSchemaEntityRepository.findByUserIdAndSchemaName(tableSchemaDto.userId(),
                tableSchemaDto.schemaName())
            .ifPresentOrElse(tableSchemaDto::updateEntity,
                () -> tableSchemaEntityRepository.save(tableSchemaDto.createEntity()));
    }


    @Transactional
    public void deleteMySchema(String userId, String schemaName){
        tableSchemaEntityRepository.deleteByUserIdAndSchemaName(userId, schemaName);
    }
}

package com.backend.testdata.service.exporter;

import com.backend.testdata.domain.constants.ExportFileType;
import com.backend.testdata.dto.TableSchemaDto;

/**
 * 특정 파일 유형 {@link ExportFileType} 에 맞는 포맷을 제공하는 인터페이스
 */
public interface MockDataFileExporter {

    /**
     * @return 이 구현체가 다루는 파일 유형
     */
    ExportFileType getType();

    /**
     * @param dto      특정 포맷으로 변환될 테이블 스키마
     * @param rowCount 행 카운트 수
     * @return 특정 파일 포맷으로 변환되는 문자열 데이터
     */
    String export(TableSchemaDto dto, Integer rowCount);

}

package com.backend.testdata.domain;


import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;


@ToString
@Getter
public class TableSchemaEntity {


    private String schemaName;

    private String userId;

    private LocalDateTime exportedAt;




}

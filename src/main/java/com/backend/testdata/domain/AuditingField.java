package com.backend.testdata.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@ToString
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditingField {

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;


    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime modifiedAt;
    @LastModifiedBy
    @Column(nullable = false)
    private String modifiedBy;
}

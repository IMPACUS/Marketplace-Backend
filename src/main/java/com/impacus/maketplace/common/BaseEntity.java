package com.impacus.maketplace.common;

import com.impacus.maketplace.common.utils.TimestampConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Convert(converter = TimestampConverter.class)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Convert(converter = TimestampConverter.class)
    private LocalDateTime modifyAt;

    @CreatedBy
    @Column(updatable = false)
    private String registerId;

    @LastModifiedBy
    private String modifyId;
}
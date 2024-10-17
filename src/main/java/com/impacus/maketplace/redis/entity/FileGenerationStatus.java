package com.impacus.maketplace.redis.entity;

import com.impacus.maketplace.common.enumType.FileStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash(value = "fileStatus", timeToLive = 86400L)
public class FileGenerationStatus {
    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileStatus status;

    @Column(nullable = false)
    private String extend;

    @Column(unique = true)
    private String saveURL;

    public FileGenerationStatus(String extend) {
        this.status = FileStatus.NOT_STARTED;
        this.extend = extend;
    }

    public void updateStatus(FileStatus status, String saveURL) {
        this.status = status;
        this.saveURL = saveURL;
    }

    public static FileGenerationStatus toEntity(String extend) {
        return new FileGenerationStatus(extend);
    }
}

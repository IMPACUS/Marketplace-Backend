package com.impacus.maketplace.repository;

import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;

import java.util.List;

public interface AttachFileCustomRepository {
    List<AttachFileDTO> findAllAttachFileByReferencedId(Long referencedId, ReferencedEntityType referencedEntityType);
}

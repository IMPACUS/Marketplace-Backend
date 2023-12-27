package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.entity.common.AttachFileGroup;
import com.impacus.maketplace.repository.AttachFileGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachFileGroupService {
    private final AttachFileGroupRepository attachFileGroupRepository;

    /**
     * AttachFileGroup 을 저장하는 함수
     *
     * @param attachFileId
     * @param referencedId AttachFile를 참조하는 객체의 id
     * @param entityType   AttachFile를 참조하는 객체 Class (각 entity 별로 1부터 id 저장됨. 같은 entity가 아니라면 동일한 id를 가질 수 있음)
     */
    @Transactional
    public void saveAttachFileGroup(Long attachFileId, Long referencedId, ReferencedEntityType entityType) {
        AttachFileGroup newAttachFileGroup = AttachFileGroup.builder()
                .attachFileId(attachFileId)
                .referencedId(referencedId)
                .referencedEntity(entityType)
                .build();

        attachFileGroupRepository.save(newAttachFileGroup);
    }
}

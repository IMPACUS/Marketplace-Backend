package com.impacus.maketplace.repository.notice;

import com.impacus.maketplace.entity.notice.NoticeManage;
import com.impacus.maketplace.repository.notice.querydsl.NoticeManageCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeManageRepository extends JpaRepository<NoticeManage, Long>, NoticeManageCustomRepository {
}

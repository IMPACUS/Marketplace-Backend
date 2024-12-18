package com.impacus.maketplace.repository.notice;

import com.impacus.maketplace.entity.notice.Notice;
import com.impacus.maketplace.repository.notice.querydsl.NoticeCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeCustomRepository {
}

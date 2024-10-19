package com.impacus.maketplace.repository.common.querydsl;

import com.impacus.maketplace.common.enumType.common.InfoType;
import com.impacus.maketplace.dto.common.response.BaseInfoDTO;
import com.impacus.maketplace.dto.common.response.BaseInfoDetailDTO;
import com.impacus.maketplace.entity.common.QBaseInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BaseInfoCustomRepositoryImpl implements BaseInfoCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    QBaseInfo baseInfo = QBaseInfo.baseInfo;

    @Override
    public BaseInfoDTO findRewardBaseInfo() {
        List<InfoType> infoTypes = List.of(InfoType.GREEN_TAG_COUPON, InfoType.USER_COUPON, InfoType.SNS_TAG_COUPON,
                InfoType.POINT_REWARD, InfoType.LEVEL_POINT);

        // 1. 쿠폰/포인트 기준 정보에 대한 BaseInfo 조회
        List<BaseInfoDetailDTO> baseInfoDetailDTOS = queryFactory
                .select(
                        Projections.fields(
                                BaseInfoDetailDTO.class,
                                baseInfo.title,
                                baseInfo.detail,
                                baseInfo.infoType
                        )
                )
                .from(baseInfo)
                .where(baseInfo.infoType.in(infoTypes))
                .fetch();

        // BaseInfo 생성
        return new BaseInfoDTO(baseInfoDetailDTOS);
    }

    @Override
    public void updateRewardBaseInfo(BaseInfoDTO dto) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        queryFactory.update(baseInfo)
                .set(baseInfo.title, dto.getGreenTagCoupon().getTitle())
                .set(baseInfo.detail, dto.getGreenTagCoupon().getDetail())
                .set(baseInfo.modifyAt, LocalDateTime.now())
                .set(baseInfo.modifyId, currentAuditor)
                .where(baseInfo.infoType.eq(InfoType.GREEN_TAG_COUPON))
                .execute();

        queryFactory.update(baseInfo)
                .set(baseInfo.title, dto.getUserCoupon().getTitle())
                .set(baseInfo.detail, dto.getGreenTagCoupon().getDetail())
                .set(baseInfo.modifyAt, LocalDateTime.now())
                .set(baseInfo.modifyId, currentAuditor)
                .where(baseInfo.infoType.eq(InfoType.USER_COUPON))
                .execute();

        queryFactory.update(baseInfo)
                .set(baseInfo.title, dto.getSnsTagCoupon().getTitle())
                .set(baseInfo.detail, dto.getSnsTagCoupon().getDetail())
                .set(baseInfo.modifyAt, LocalDateTime.now())
                .set(baseInfo.modifyId, currentAuditor)
                .where(baseInfo.infoType.eq(InfoType.SNS_TAG_COUPON))
                .execute();

        queryFactory.update(baseInfo)
                .set(baseInfo.title, dto.getPointReward().getTitle())
                .set(baseInfo.detail, dto.getPointReward().getDetail())
                .set(baseInfo.modifyAt, LocalDateTime.now())
                .set(baseInfo.modifyId, currentAuditor)
                .where(baseInfo.infoType.eq(InfoType.POINT_REWARD))
                .execute();

        queryFactory.update(baseInfo)
                .set(baseInfo.title, dto.getLevelPoint().getTitle())
                .set(baseInfo.detail, dto.getLevelPoint().getDetail())
                .set(baseInfo.modifyAt, LocalDateTime.now())
                .set(baseInfo.modifyId, currentAuditor)
                .where(baseInfo.infoType.eq(InfoType.LEVEL_POINT))
                .execute();
    }
}

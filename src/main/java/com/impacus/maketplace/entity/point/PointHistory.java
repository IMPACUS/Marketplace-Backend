package com.impacus.maketplace.entity.point;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.PointTypeEnumConverter;
import com.impacus.maketplace.common.enumType.PointType;
import com.impacus.maketplace.common.utils.TimestampConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString(callSuper = true)
@Table(name = "point_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PointHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_history_id")
    private Long id;

    @Column(nullable = false)
    private Long pointMasterId;


//    private String productNumber; // 상품 번호

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PointType pointType; // POINT_TYPE [10 : 적립, 20 : 사용, 30 : 소멸]

    @Column(nullable = false)
    private Integer changePoint; // 변동 포인트

    @Builder.Default
    private Boolean isManual = null; // 수동여부 [null : System, true : 관리자, false : 사용자)
    // 추후 관리자 페이지에서 오류,보상 지급을 위해 관리자가 직접 포인트를 부여하는 경우를 대비해서 만들어 놓았습니다!

    @Convert(converter = TimestampConverter.class)
    private LocalDateTime expiredAt; // 소멸 시간

    @Builder.Default
    @Setter
    private Boolean expiredCheck = false;
}

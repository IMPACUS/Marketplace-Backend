package com.impacus.maketplace.entity.address;

import com.impacus.maketplace.entity.order.DeliveryAddress;
import com.impacus.maketplace.entity.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "my_delivery_address")
@NoArgsConstructor
public class MyDeliveryAddress extends Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 외래키 제약조건 제거
     * 소프트 삭제를 통해 삭제된 사용자의 배송지를 조회하기 위해 외래키 제약조건을 제거합니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Builder
    public MyDeliveryAddress(String receiver, String connectNumber, String address, String detailAddress, String postalCode, String memo, Long id, User user) {
        super(receiver, connectNumber, address, detailAddress, postalCode, memo);
        this.id = id;
        this.user = user;
    }

    public DeliveryAddress toDeliveryAddress() {
        return DeliveryAddress.builder()
                .receiver(this.getReceiver())
                .connectNumber(this.getConnectNumber())
                .address(this.getAddress())
                .detailAddress(this.getDetailAddress())
                .postalCode(this.getPostalCode())
                .memo(this.getMemo())
                .build();
    }
}

package com.impacus.maketplace.service.order;

import com.impacus.maketplace.dto.order.request.OrderReqDTO;
import com.impacus.maketplace.dto.order.response.OrderResDTO;
import com.impacus.maketplace.entity.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.CustomUserDetails;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderService {

    private final EntityManager em;

    @Transactional
    public OrderResDTO createOrder(OrderReqDTO orderReqDTO, CustomUserDetails user) {
        User proxyUser = getProxyUser(user.getId());
        // TODO : Implement createOrder
        // 1. 주문 생성
        // 2. 주문 상품 생성
        // 3. 주문 상품 재고 차감
        // 4. 쿠폰 사용
        // 5. 포인트 차감
        // 6. 결제 금액 및 OrderResDTO 반환 => 결제는 다른 api 활용
        return new OrderResDTO();
    }


    private User getProxyUser(Long id) {
        return em.getReference(User.class, id);
    }
}

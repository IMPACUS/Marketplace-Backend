package com.impacus.maketplace.service.payment;

import com.impacus.maketplace.dto.payment.model.PaymentProductInfoIdDTO;
import com.impacus.maketplace.repository.payment.checkout.CheckoutCustomRepository;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductInfoDTO;
import com.impacus.maketplace.service.payment.utils.PaymentTestDataInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CheckoutCustomRepositoryTest {

    @Autowired
    CheckoutCustomRepository checkoutCustomRepository;

    @Autowired
    PaymentTestDataInitializer paymentTestDataInitializer;

    @BeforeEach
    void setup() {
        paymentTestDataInitializer.deleteAllData();
        paymentTestDataInitializer.initializeTestData();
    }

    @Test
    @DisplayName("12개의 상품 개별 조회 테스트")
    void getPaymentProductInfoTest() {
        // given
        Long sellerId = 1L;

        List<Long> productIds = getProductIds();
        Map<Long, List<Long>> productOptionIdMap = getProductOptionIdMap();

        Set<Long> keys = productOptionIdMap.keySet();

        // when
        List<CheckoutProductInfoDTO> result = keys.stream().flatMap(productId -> {
            List<Long> productOptionIds = productOptionIdMap.get(productId);
            return productOptionIds.stream().map(productOptionId -> checkoutCustomRepository.getPaymentProductInfo(productId, productOptionId, sellerId, false, null));
        }).toList();

        // then
        for (CheckoutProductInfoDTO productInfoDTO : result) {
            System.out.println("result: " + productInfoDTO);
        }

        assertThat(result.size()).isEqualTo(12);
    }

    @Test
    @DisplayName("12개의 상품 통합 조회 테스트")
    void getPaymentProductInfosTest() {
        // given
        List<PaymentProductInfoIdDTO> paymentProductInfoIds = new ArrayList<>();
        for (long i = 1L; i <= 4L; i++) {
            for (long j = 3 * (i - 1) + 1; j < 3 * (i - 1) + 4; j++) {
                paymentProductInfoIds.add(new PaymentProductInfoIdDTO(i, j, 1L));
            }
        }

        // when
        List<CheckoutProductInfoDTO> result = checkoutCustomRepository.getPaymentProductInfos(paymentProductInfoIds, false, null);

        // then
        for (CheckoutProductInfoDTO productInfoDTO : result) {
            System.out.println("result: " + productInfoDTO);
        }

        assertThat(result.size()).isEqualTo(12);
    }

    private Map<Long, List<Long>> getProductOptionIdMap() {
        Map<Long, List<Long>> map = new HashMap<>();
        for (int i = 1; i <= 4; i++) {
            List<Long> productOptionIds = new ArrayList<>();
            for (int j = 1; j <= 3; j++) {
                productOptionIds.add((long) (3 * (i - 1) + j));
            }
            map.put((long) i, productOptionIds);
        }

        return map;
    }

    private List<Long> getProductIds() {
        List<Long> productIds = new ArrayList<>();
        productIds.add(1L);
        productIds.add(2L);
        productIds.add(3L);
        productIds.add(4L);

        return productIds;
    }

}